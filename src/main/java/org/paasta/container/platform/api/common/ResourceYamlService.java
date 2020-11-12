package org.paasta.container.platform.api.common;

import org.paasta.container.platform.api.clusters.limitRanges.LimitRangesDefault;
import org.paasta.container.platform.api.clusters.limitRanges.LimitRangesDefaultList;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasDefault;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasDefaultList;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasService;
import org.paasta.container.platform.api.common.model.ResultStatus;
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
 * Resource Yaml Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.14
 **/
@Service
public class ResourceYamlService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceYamlService.class);

    private final CommonService commonService;
    private final PropertyService propertyService;
    private final TemplateService templateService;
    private final RestTemplateService restTemplateService;
    private final ResourceQuotasService resourceQuotasService;

    @Autowired
    public ResourceYamlService(CommonService commonService, PropertyService propertyService, TemplateService templateService, RestTemplateService restTemplateService, ResourceQuotasService resourceQuotasService) {
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.templateService = templateService;
        this.restTemplateService = restTemplateService;
        this.resourceQuotasService = resourceQuotasService;
    }


    /**
     * ftl 파일로 Namespace 생성(Create Namespace)
     *
     * @param namespace
     * @return the result status
     */
    public ResultStatus createNamespace(String namespace) {
        Map map = new HashMap();
        map.put("spaceName", namespace);

        String nsYaml = templateService.convert("create_namespace.ftl", map);
        Object nameSpaceResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespacesCreateUrl(), HttpMethod.POST, nsYaml, Object.class, true);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(nameSpaceResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
    }


    /**
     * ftl 파일로 Service Account 생성(Create Service Account)
     *
     * @param username
     * @param namespace
     * @return the result status
     */
    public ResultStatus createServiceAccount(String username, String namespace) {
        String saYaml = templateService.convert("create_account.ftl", yamlMatch(username, namespace));
        Object saResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, saYaml, Object.class, true);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(saResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, null);
    }


    /**
     * ftl 파일로 Role Binding 생성(Create Role Binding)
     *
     * @param username
     * @param namespace
     * @param roleName
     * @return
     */
    public ResultStatus createRoleBinding(String username, String namespace, String roleName) {
        Map map = new HashMap();
        String roleBindingYaml;

        if(roleName == null) {
            map.put("userName", username);
            map.put("spaceName", namespace);

            roleBindingYaml = templateService.convert("create_clusterRoleBinding.ftl", map);
        } else {
            map.put("userName", username);
            map.put("roleName", roleName);
            map.put("spaceName", namespace);

            roleBindingYaml = templateService.convert("create_roleBinding.ftl", map);
        }

        Object rbResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, roleBindingYaml, Object.class, true);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rbResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, null);
    }


    /**
     * ftl 파일로 init role 생성(Create init role)
     *
     * @param namespace the namespace
     */
    public void createInitRole(String namespace) {
        // init role 생성
        Map<String, Object> map = new HashMap();
        map.put("spaceName", namespace);
        map.put("roleName", Constants.DEFAULT_NAMESPACE_INIT_ROLE);
        String initRoleYaml = templateService.convert("create_init_role.ftl", map);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRolesCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, initRoleYaml, Object.class, true);
    }


    /**
     * Namespace Admin Role 생성(Create Namespace Admin Role)
     *
     * @param namespace the namespace
     */
    public void createNsAdminRole(String namespace) {
        Map<String, Object> map = new HashMap();
        map.put("spaceName", namespace);
        map.put("roleName", Constants.DEFAULT_NAMESPACE_ADMIN_ROLE);
        String nsAdminRoleYaml = templateService.convert("create_admin_role.ftl", map);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRolesCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, nsAdminRoleYaml, Object.class, true);
    }


    /**
     * namespace에 ResourceQuotas를 할당(Allocate ResourceQuotas to Namespace)
     *
     * @param reqNamespace the request namespace
     * @param rqName the request name
     */
    public void createDefaultResourceQuota(String reqNamespace, String rqName) {
        ResourceQuotasDefaultList resourceQuotasDefaultList = restTemplateService.send(Constants.TARGET_COMMON_API, "/resourceQuotas", HttpMethod.GET, null, ResourceQuotasDefaultList.class);
        String resourceQuotaYaml = "";
        String requestCpu = "";
        String requestMemory = "";
        String limitsCpu = "";
        String limitsMemory = "";

        for (ResourceQuotasDefault d:resourceQuotasDefaultList.getItems()) {
            if (rqName == null) {
                rqName = DEFAULT_LOW_RESOURCE_QUOTA_NAME;
            }

            if (DEFAULT_RESOURCE_QUOTAS_LIST.contains(rqName) && d.getName().equals(rqName)) {
                limitsCpu = d.getLimitCpu();
                limitsMemory = d.getLimitMemory();

                break;
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("name", rqName);
        model.put("namespace", reqNamespace);
        model.put("limits_cpu", limitsCpu);
        model.put("limits_memory", limitsMemory);

        resourceQuotaYaml = templateService.convert("create_resource_quota.ftl", model);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListResourceQuotasCreateUrl().replace("{namespace}", reqNamespace), HttpMethod.POST, resourceQuotaYaml, Object.class, true);

    }


    /**
     * namespace에 LimitRanges를 할당(Allocate LimitRanges to Namespace)
     *
     * @param reqNamespace the request namespace
     * @param lrName the request name
     */
    public void createDefaultLimitRanges(String reqNamespace, String lrName) {
        LimitRangesDefaultList limitRangesDefaultList = restTemplateService.send(Constants.TARGET_COMMON_API, "/limitRanges", HttpMethod.GET, null, LimitRangesDefaultList.class);
        String limitRangeYaml = "";
        String limitsCpu = "";
        String limitsMemory = "";

        for (LimitRangesDefault limitRanges:limitRangesDefaultList.getItems()) {
            if (lrName == null) {
                lrName = DEFAULT_LOW_LIMIT_RANGE_NAME;
            }

            if (DEFAULT_LIMIT_RANGES_LIST.contains(lrName) && limitRanges.getName().equals(lrName)) {
                String limitsLr = limitRanges.getDefaultLimit();
                String[] limitsLrList = limitsLr.split(",");
                limitsCpu = limitsLrList[0].split(":")[1];
                limitsMemory = limitsLrList[1].split(":")[1];

                break;
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("name", lrName);
        model.put("namespace", reqNamespace);
        model.put("limit_cpu", limitsCpu);
        model.put("limit_memory", limitsMemory);

        limitRangeYaml = templateService.convert("create_limit_range.ftl", model);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListLimitRangesCreateUrl().replace("{namespace}", reqNamespace), HttpMethod.POST, limitRangeYaml, Object.class, true);

    }

}
