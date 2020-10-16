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
            Object namespaces = restTemplateService.send(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespacesGetUrl().replace("{namespace}", namespace), HttpMethod.GET, null, Object.class);

            if(namespaces instanceof ResultStatus) {
                LOGGER.info("CREATE TEMP NAMESPACE AND INIT ROLE...");
                // temp-namespace k8s에 생성
                restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespacesCreateUrl(), HttpMethod.POST, namespaceYaml, Object.class);

                // init role 생성
                Map<String, Object> map = new HashMap();
                map.put("spaceName", namespace);
                map.put("roleName", Constants.DEFAULT_INIT_ROLE);
                String initRoleYaml = templateService.convert("create_init_role.ftl", map);
                restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRolesCreateUrl().replace("{namespace}", namespace), HttpMethod.POST, initRoleYaml, Object.class);
            }

        };
    }

}
