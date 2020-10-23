package org.paasta.container.platform.api.managements.resourceQuotas;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * ResourceQuotas Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 **/

@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/resourceQuotas")
public class ResourceQuotasController {
    private final ResourceQuotasService resourceQuotasService;

    /**
     * Instantiates a ResourceQuotas Controller
     *
     * @param resourceQuotasService the resourceQuotas Service
     */
    @Autowired
    public ResourceQuotasController(ResourceQuotasService resourceQuotasService) {
        this.resourceQuotasService = resourceQuotasService;
    }

    /**
     * ResourceQuotas 목록 조회(Get ResourceQuotas list)
     *
     * @param namespace the namespaces
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the resourceQuotas list
     */
    @GetMapping
    public ResourceQuotasList getResourceQuotasList(@PathVariable("namespace") String namespace,
                                                    @RequestParam(required = false, defaultValue = "0") int limit,
                                                    @RequestParam(required = false, name = "continue") String continueToken) {
        return resourceQuotasService.getResourceQuotasList(namespace, limit, continueToken);
    }

    /**
     * ResourceQuotas 상세 조회(Get ResourceQuotas detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the resourceQuotas detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public ResourceQuotas getResourceQuotas(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return resourceQuotasService.getResourceQuotas(namespace, resourceName);
    }

    /**
     * ResourceQuotas YAML 조회(Get ResourceQuotas yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the resourceQuotas yaml
     */
    @GetMapping(value = "{resourceName:.+}/yaml")
    public ResourceQuotas getResourceQuotasYaml(@PathVariable(value = "cluster") String cluster,
                                                      @PathVariable(value = "namespace") String namespace,
                                                      @PathVariable(value = "resourceName") String resourceName) {
        return resourceQuotasService.getResourceQuotasYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * ResourceQuotas 생성(Create ResourceQuotas)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @RequestBody String yaml) throws Exception{
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return resourceQuotasService.createResourceQuotas(namespace, yaml);
    }

    /**
     * ResourceQuotas 삭제(Delete ResourceQuotas)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName) {
        return resourceQuotasService.deleteResourceQuotas(namespace, resourceName, new HashMap<>());
    }

    /**
     * ResourceQuotas 수정(Update ResourceQuotas)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @RequestBody String yaml) {
        return resourceQuotasService.updateResourceQuotas(namespace, resourceName,yaml);
    }
}
