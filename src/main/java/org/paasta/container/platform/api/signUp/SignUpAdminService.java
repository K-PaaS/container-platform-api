package org.paasta.container.platform.api.signUp;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
public class SignUpAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpAdminService.class);

    private final PropertyService propertyService;
    private final TemplateService templateService;
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final AccessTokenService accessTokenService;

    @Autowired
    public SignUpAdminService(PropertyService propertyService, TemplateService templateService, RestTemplateService restTemplateService, CommonService commonService, AccessTokenService accessTokenService) {
        this.propertyService = propertyService;
        this.templateService = templateService;
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.accessTokenService = accessTokenService;
    }

    public ResultStatus signUpAdminUsers(Users users) {
        String namespace = users.getCpNamespace();
        String username = users.getUserId();

        Map<String, Object> model1 = new HashMap();
        model1.put("spaceName", namespace);

        // (1) ::: namespace 생성 시 최저 사양의 resource quota, limit range 도 같이 생성
        String nsYaml = templateService.convert("create_namespace.ftl", model1);
        Object nameSpaceResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceCreateUrl(), HttpMethod.POST, nsYaml, Object.class);

        ResultStatus nsResult = (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(nameSpaceResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);

        if(Constants.RESULT_STATUS_FAIL.equals(nsResult.getResultCode())) {
            return nsResult;
        }

        // resource quota 생성
        createResourceQuota();

        // limit range 생성
        createLimitRange();

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

        String adminSaSecretName = restTemplateService.getSecretName(namespace, users.getUserId());

        users.setCpNamespace(namespace);
        users.setServiceAccountName(username);
        users.setRoleSetCode(DEFAULT_CLUSTER_ADMIN_ROLE);
        users.setSaSecret(adminSaSecretName);
        users.setSaToken(accessTokenService.getSecret(namespace, adminSaSecretName).getUserAccessToken());
        users.setUserType("CLUSTER_ADMIN");
        users.setIsActive("Y");

        // (4) ::: service account 생성, cluster role binding 완료 시 아래 Common API 호출
        ResultStatus rsDb = restTemplateService.send(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class);

        // (5) ::: DB 커밋에 실패했을 경우 k8s 에 만들어진 namespace, cluster role binding 삭제
        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT, CLUSTER ROLE BINDING WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceDeleteUrl().replace("{namespace}", namespace), HttpMethod.DELETE, null, Object.class);
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListClusterRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", "cluster-admin-" + username), HttpMethod.DELETE, null, Object.class);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
    }

    /**
     *  namespace에 ResourceQuota를 할당한다.
     *
     */
    public void createResourceQuota() {
        LOGGER.info("Create Resource Quota...");

        Map<String, Object> model = new HashMap<>();
        model.put("resource_quota_cpu", propertyService.getResourceQuotaLimitsCpu());
        model.put("resource_quota_memory", propertyService.getResourceQuotaLimitsMemory());
        model.put("resource_quota_disk", propertyService.getResourceQuotaRequestsStorage());
        String resourceQuotaYaml = templateService.convert("create_resource_quota.ftl", model);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListResourceQuotasCreateUrl().replace("{namespace}", Constants.DEFAULT_NAMESPACE_NAME), HttpMethod.POST, resourceQuotaYaml, Object.class);

    }

    /**
     * namespace에 LimitRange를 할당한다.
     *
     */
    public void createLimitRange() {
        LOGGER.info("Create Limit Range...");

        Map<String, Object> model = new HashMap<>();
        model.put("limit_range_cpu", propertyService.getLimitRangeCpu());
        model.put("limit_range_memory", propertyService.getLimitRangeMemory());
        String limitRangeYaml = templateService.convert("create_limit_range.ftl", model);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListLimitRangesCreateUrl().replace("{namespace}", Constants.DEFAULT_NAMESPACE_NAME), HttpMethod.POST, limitRangeYaml, Object.class);

    }
}
