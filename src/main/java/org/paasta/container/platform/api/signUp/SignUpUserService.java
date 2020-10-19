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

import static org.paasta.container.platform.api.common.CommonUtils.yamlMatch;
import static org.paasta.container.platform.api.common.Constants.*;

/**
 * User Service 클래스
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
    private final TemplateService templateService;
    private final RestTemplateService restTemplateService;
    private final AccessTokenService accessTokenService;
    private final UsersService usersService;

    @Autowired
    public SignUpUserService(CommonService commonService, PropertyService propertyService, TemplateService templateService, RestTemplateService restTemplateService, AccessTokenService accessTokenService, UsersService usersService) {
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.templateService = templateService;
        this.restTemplateService = restTemplateService;
        this.accessTokenService = accessTokenService;
        this.usersService = usersService;
    }


    /**
     * 사용자를 등록한다. (회원가입)
     *
     * @param users  the users
     * @return       the result status
     */
    public ResultStatus signUpUsers(Users users) {
        String namespace = Constants.DEFAULT_NAMESPACE_NAME;
        String username = users.getUserId();

        // (1) ::: service account 생성. 타겟은 temp-namespace.
        String saYaml = templateService.convert("create_account.ftl", yamlMatch(username, namespace));
        Object saResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, saYaml, Object.class);

        ResultStatus rsK8s = (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(saResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, "/");

        // (2) ::: service account 생성 완료 시 아래 Common API 호출
        if(Constants.RESULT_STATUS_FAIL.equals(rsK8s.getResultCode())) {
            return rsK8s;
        }

        String saSecretName = restTemplateService.getSecretName(namespace, username);

        users.setCpNamespace(Constants.DEFAULT_NAMESPACE_NAME);
        users.setServiceAccountName(username);
        users.setRoleSetCode(NOT_ASSIGNED_ROLE);
        users.setSaSecret(saSecretName);
        users.setSaToken(accessTokenService.getSecret(namespace, saSecretName).getUserAccessToken());
        users.setUserType("USER");

        ResultStatus rsDb = usersService.createUsers(users);

        // (3) ::: DB 커밋에 실패했을 경우 k8s 에 만들어진 service account 삭제
        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", Constants.DEFAULT_NAMESPACE_NAME).replace("{name}", users.getUserId()), HttpMethod.DELETE, null, Object.class);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, "/");
    }

    /**
     * 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    public Map<String, List<String>> getUsersNameList() {
        return restTemplateService.send(TARGET_COMMON_API, "/users/names", HttpMethod.GET, null, Map.class);
    }
}
