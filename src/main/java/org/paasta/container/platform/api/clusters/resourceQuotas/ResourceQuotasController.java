package org.paasta.container.platform.api.clusters.resourceQuotas;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.paasta.container.platform.api.common.Constants;
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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the resourceQuotas list
     */
    @GetMapping
    public Object getResourceQuotasList(@PathVariable(value = "cluster") String cluster,
                                        @PathVariable("namespace") String namespace,
                                        @RequestParam(required = false, defaultValue = "0") int limit,
                                        @RequestParam(required = false, name = "continue") String continueToken,
                                        @RequestParam(required = false) String searchParam,
                                        @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return resourceQuotasService.getResourceQuotasListAdmin(namespace, limit, continueToken, searchParam);
        }

        return resourceQuotasService.getResourceQuotasList(namespace, limit, continueToken);
    }

    /**
     * ResourceQuotas 상세 조회(Get ResourceQuotas detail)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the resourceQuotas detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                            @PathVariable(value = "namespace") String namespace,
                                            @PathVariable(value = "resourceName") String resourceName,
                                            @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return resourceQuotasService.getResourceQuotasAdmin(namespace, resourceName);
        }

        return resourceQuotasService.getResourceQuotas(namespace, resourceName);
    }


    /**
     * ResourceQuotas YAML 조회(Get ResourceQuotas yaml)
     * @param cluster the cluster
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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @RequestBody String yaml) throws Exception{
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return resourceQuotasService.createResourceQuotas(namespace, yaml);
    }

    /**
     * ResourceQuotas 삭제(Delete ResourceQuotas)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping(value = "/{resourceName:.+}")
    public ResultStatus deleteResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName) {
        return resourceQuotasService.deleteResourceQuotas(namespace, resourceName, new HashMap<>());
    }

    /**
     * ResourceQuotas 수정(Update ResourceQuotas)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping(value = "/{resourceName:.+}")
    public Object updateResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @RequestBody String yaml) {
        return resourceQuotasService.updateResourceQuotas(namespace, resourceName,yaml);
    }


    /**
     * ResourceQuota Default Template 목록 조회
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param isAdmin the isAdmin
     * @return the object
     * @throws JsonProcessingException
     */
    @GetMapping(value = "/template")
    public Object getResourceQuotasDefaultList(@PathVariable(value = "cluster") String cluster,
                                     @PathVariable(value = "namespace") String namespace,
                                     @RequestParam(required = false, name = "isAdmin") boolean isAdmin) throws JsonProcessingException {

        if (isAdmin) {
            return resourceQuotasService.getRqDefaultList(namespace);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


}
