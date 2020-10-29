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

import static org.paasta.container.platform.api.common.Constants.DEFAULT_CLUSTER_ADMIN_ROLE;
import static org.paasta.container.platform.api.common.Constants.TARGET_CP_MASTER_API;

/**
 * Sign Up Admin Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class SignUpAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpAdminService.class);

    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final AccessTokenService accessTokenService;
    private final UsersService usersService;
    private final ResourceYamlService resourceYamlService;

    /**
     * Instantiates a new SignUpAdminService service
     *
     * @param propertyService the property service
     * @param restTemplateService the rest template service
     * @param commonService the common service
     * @param accessTokenService the access token service
     * @param usersService the users service
     * @param resourceYamlService the resource yaml service
     */
    @Autowired
    public SignUpAdminService(PropertyService propertyService, RestTemplateService restTemplateService, CommonService commonService, AccessTokenService accessTokenService, UsersService usersService, ResourceYamlService resourceYamlService) {
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.accessTokenService = accessTokenService;
        this.usersService = usersService;
        this.resourceYamlService = resourceYamlService;
    }


    /**
     * 운영자 회원가입(Sign Up Admin)
     *
     * @param users the user
     * @return the resultStatus
     */
    public ResultStatus signUpAdminUsers(Users users) {
        String namespace = users.getCpNamespace();
        String username = users.getUserId();

        // create row spec resource quota, limit range
        ResultStatus nsResult = resourceYamlService.createNamespace(namespace);

        if(Constants.RESULT_STATUS_FAIL.equals(nsResult.getResultCode())) {
            return nsResult;
        }

        resourceYamlService.createDefaultResourceQuota();
        resourceYamlService.createDefaultLimitRanges();

        ResultStatus saResult = resourceYamlService.createServiceAccount(username, namespace);

        if(Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
            return saResult;
        }

        ResultStatus rbResult = resourceYamlService.createRoleBinding(username, namespace, null);

        if(Constants.RESULT_STATUS_FAIL.equals(rbResult.getResultCode())) {
            LOGGER.info("CLUSTER ROLE BINDING EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", username), HttpMethod.DELETE, null, Object.class);
            return rbResult;
        }

        String adminSaSecretName = restTemplateService.getSecretName(namespace, users.getUserId());

        users.setCpNamespace(namespace);
        users.setServiceAccountName(username);
        users.setRoleSetCode(DEFAULT_CLUSTER_ADMIN_ROLE);
        users.setSaSecret(adminSaSecretName);
        users.setSaToken(accessTokenService.getSecrets(namespace, adminSaSecretName).getUserAccessToken());
        users.setUserType("CLUSTER_ADMIN");
        users.setIsActive("Y");

        ResultStatus rsDb = usersService.createUsers(users);

        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT, CLUSTER ROLE BINDING WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespacesDeleteUrl().replace("{namespace}", namespace), HttpMethod.DELETE, null, Object.class);
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListClusterRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", "cluster-admin-" + username), HttpMethod.DELETE, null, Object.class);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
    }

}
