package org.paasta.container.platform.api.common;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.paasta.container.platform.api.common.CommonUtils.yamlMatch;
import static org.paasta.container.platform.api.common.Constants.TARGET_CP_MASTER_API;

/**
 * setting된 yaml file로 k8s 호출
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

    @Autowired
    public ResourceYamlService(CommonService commonService, PropertyService propertyService, TemplateService templateService, RestTemplateService restTemplateService) {
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.templateService = templateService;
        this.restTemplateService = restTemplateService;
    }

    public void createNamespace(String namespace) {
        Map<String, Object> model = new HashMap<>();
        model.put("spaceName", namespace);
        String nsYaml = templateService.convert("create_namespace.ftl", model);
        Object nsResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, nsYaml, Object.class);
    }


    public ResultStatus createServiceAccount(String username, String namespace) {
        String saYaml = templateService.convert("create_account.ftl", yamlMatch(username, namespace));
        Object saResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, saYaml, Object.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(saResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, null);
    }


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

        Object rbResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, roleBindingYaml, Object.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rbResult, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, null);
    }


    public void createInitRole(String namespace) {
        // init role 생성
        Map<String, Object> map = new HashMap();
        map.put("spaceName", namespace);
        map.put("roleName", Constants.DEFAULT_INIT_ROLE);
        String initRoleYaml = templateService.convert("create_init_role.ftl", map);

        restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRolesCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, initRoleYaml, Object.class);
    }

}
