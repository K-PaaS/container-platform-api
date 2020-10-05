package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import static org.paasta.container.platform.api.common.CommonUtils.yamlMatch;
import static org.paasta.container.platform.api.common.Constants.TARGET_COMMON_API;
import static org.paasta.container.platform.api.common.Constants.TARGET_CP_MASTER_API;

/**
 * User Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class AdminUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserService.class);

    private final PropertyService propertyService;
    private final TemplateService templateService;
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;

    @Autowired
    public AdminUserService(PropertyService propertyService, TemplateService templateService, RestTemplateService restTemplateService, CommonService commonService) {
        this.propertyService = propertyService;
        this.templateService = templateService;
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
    }

    public ResultStatus registerAdminUser(Users users) {
        String namespace = Constants.DEFAULT_SUPER_ADMIN_NAMESPACE;
        String username = users.getUserId();

        // (1) ::: namespace 생성.
//        String nsYaml = templateService.convert("create_namespace.ftl", model1);
//        Object nameSpaceResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceCreateUrl(), HttpMethod.POST, nsYaml, Object.class);
//
//        ResultStatus nsResult = (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(nameSpaceResult, ResultStatus.class),
//                Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
//
//        if(Constants.RESULT_STATUS_FAIL.equals(nsResult.getResultCode())) {
//            return nsResult;
//        }

        // (2) ::: service account 생성.
        String adminSaYaml = templateService.convert("create_account.ftl", yamlMatch(username, namespace));
        Object adminSaResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, adminSaYaml, Object.class);

        ResultStatus saResult = (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(adminSaResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);

        if(Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
            return saResult;
        }

        // (3) ::: cluster role binding
        String adminRoleBindingYaml = templateService.convert("create_clusterRoleBinding.ftl", yamlMatch(username, namespace));
        Object roleBindingResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListClusterRoleBindingsCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, adminRoleBindingYaml, Object.class);

        ResultStatus rbResult = (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(roleBindingResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);

        if(Constants.RESULT_STATUS_FAIL.equals(rbResult.getResultCode())) {
            LOGGER.info("CLUSTER ROLE BINDING EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", username), HttpMethod.DELETE, null, Object.class);
            return rbResult;
        }

        users.setCpNamespace(namespace);
        users.setServiceAccountName(username);

        // (4) ::: service account 생성, cluster role binding 완료 시 아래 Common API 호출
        ResultStatus rsDb = restTemplateService.send(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class);

        // (5) ::: DB 커밋에 실패했을 경우 k8s 에 만들어진 service account 삭제
        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT, CLUSTER ROLE BINDING WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", username), HttpMethod.DELETE, null, Object.class);
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListClusterRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", "cluster-admin-" + username), HttpMethod.DELETE, null, Object.class);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
    }
}
