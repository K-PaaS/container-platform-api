package org.paasta.container.platform.api;

import org.paasta.container.platform.api.clusters.namespaces.Namespaces;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PaasTaContainerPlatformApiApplication {

    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;

    @Autowired
    public PaasTaContainerPlatformApiApplication(PropertyService propertyService, RestTemplateService restTemplateService) {
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PaasTaContainerPlatformApiApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            String namespaceYaml = "apiVersion: v1\n" +
                    "kind: Namespace\n" +
                    "metadata:\n" +
                    "  name: " + Constants.DEFAULT_NAMESPACE_NAME;

            Namespaces namespaces = restTemplateService.send(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceGetUrl().replace("{namespace}", Constants.DEFAULT_NAMESPACE_NAME), HttpMethod.GET, null, Namespaces.class);

            if(namespaces == null) {
                // temp-namespace k8s에 생성
                Object result = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespaceCreateUrl(), HttpMethod.POST, namespaceYaml, Object.class);
            }
        };
    }
}
