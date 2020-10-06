package org.paasta.container.platform.api;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.TemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PaasTaContainerPlatformApiApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaasTaContainerPlatformApiApplication.class);

    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;
    private final TemplateService templateService;

    @Autowired
    public PaasTaContainerPlatformApiApplication(PropertyService propertyService, RestTemplateService restTemplateService, TemplateService templateService) {
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
        this.templateService = templateService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaasTaContainerPlatformApiApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            String namespace = Constants.DEFAULT_NAMESPACE_NAME;
            String namespaceYaml = "apiVersion: v1\n" +
                    "kind: Namespace\n" +
                    "metadata:\n" +
                    "  name: " + namespace;
            Object namespaces = restTemplateService.send(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceGetUrl().replace("{namespace}", namespace), HttpMethod.GET, null, Object.class);

            if(namespaces instanceof ResultStatus) {
                LOGGER.info("CREATE TEMP NAMESPACE AND INIT ROLE...");
                // temp-namespace k8s에 생성
                restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceCreateUrl(), HttpMethod.POST, namespaceYaml, Object.class);

                // resource quota 생성
                createResourceQuota();

                // limit range 생성
                createLimitRange();

                // init role 생성
                Map<String, Object> map = new HashMap();
                map.put("spaceName", namespace);
                map.put("roleName", Constants.DEFAULT_INIT_ROLE);
                String initRoleYaml = templateService.convert("create_init_role.ftl", map);
                restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRolesCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, initRoleYaml, Object.class);
            }

        };
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
