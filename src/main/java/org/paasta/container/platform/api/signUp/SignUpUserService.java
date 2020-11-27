package org.paasta.container.platform.api.signUp;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.Constants.*;

/**
 * Sign Up User Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class SignUpUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpUserService.class);

    private final CommonService commonService;
    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;
    private final AccessTokenService accessTokenService;
    private final UsersService usersService;
    private final ResourceYamlService resourceYamlService;

    /**
     * Instantiates a new SignUpUserService service
     *
     * @param commonService the common service
     * @param propertyService the property service
     * @param restTemplateService the rest template service
     * @param accessTokenService the access token service
     * @param usersService the users service
     * @param resourceYamlService the resource yaml service
     */
    @Autowired
    public SignUpUserService(CommonService commonService, PropertyService propertyService, RestTemplateService restTemplateService, AccessTokenService accessTokenService, UsersService usersService, ResourceYamlService resourceYamlService) {
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
        this.accessTokenService = accessTokenService;
        this.usersService = usersService;
        this.resourceYamlService = resourceYamlService;
    }


    /**
     * 회원가입(Sign Up)
     *
     * @param users the users
     * @return the resultStatus
     */
    public ResultStatus signUpUsers(Users users) {
        String namespace = propertyService.getDefaultNamespace();
        String username = users.getUserId();

        // default namespace
        ResultStatus rsK8s = resourceYamlService.createServiceAccount(username, namespace);

        if(Constants.RESULT_STATUS_FAIL.equals(rsK8s.getResultCode())) {
            return rsK8s;
        }

        String saSecretName = restTemplateService.getSecretName(namespace, username);

        users.setCpNamespace(propertyService.getDefaultNamespace());
        users.setServiceAccountName(username);
        users.setRoleSetCode(NOT_ASSIGNED_ROLE);
        users.setSaSecret(saSecretName);
        users.setSaToken(accessTokenService.getSecrets(namespace, saSecretName).getUserAccessToken());
        users.setUserType("USER");

        ResultStatus rsDb = usersService.createUsers(usersService.commonSaveClusterInfo(propertyService.getCpClusterName(), users));

        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", propertyService.getDefaultNamespace()).replace("{name}", users.getUserId()), HttpMethod.DELETE, null, Object.class, true);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, "/");
    }

    /**
     * Users 이름 목록 조회(Get Users names list)
     *
     * @return the Map
     */
    public Map<String, List<String>> getUsersNameList() {
        return restTemplateService.send(TARGET_COMMON_API, "/users/names", HttpMethod.GET, null, Map.class);
    }
}
