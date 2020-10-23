package org.paasta.container.platform.api.storages.persistentVolumes;


import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * PersistentVolumes Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/persistentVolumes")
public class PersistentVolumesController {
    private final PersistentVolumesService persistentVolumesService;

    /**
     * Instantiates a new persistentVolumes controller
     *
     * @param persistentVolumesService the persistentVolumes service
     */
    @Autowired
    public PersistentVolumesController(PersistentVolumesService persistentVolumesService){
        this.persistentVolumesService = persistentVolumesService;
    }

    /**
     * PersistentVolumes 목록 조회(Get PersistentVolumes list)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the persistentVolumes List
     */
    @GetMapping
    public PersistentVolumesList getPersistentVolumesList(@PathVariable(value = "cluster") String cluster,
                                                          @PathVariable(value = "namespace") String namespace,
                                                          @RequestParam(required = false, defaultValue = "0") int limit,
                                                          @RequestParam(required = false, name = "continue") String continueToken) {
        return persistentVolumesService.getPersistentVolumesList(namespace, limit, continueToken);

    }

    /**
     * PersistentVolumes 상세 조회(Get PersistentVolumes detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the persistentVolumes
     */
    @GetMapping(value = "/{resourceName:.+}")
    public PersistentVolumes getPersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                                  @PathVariable(value = "namespace") String namespace,
                                                  @PathVariable(value = "resourceName") String resourceName) {
        return persistentVolumesService.getPersistentVolumes(namespace, resourceName);
    }

    /**
     * PersistentVolumes YAML 조회(Get PersistentVolumes yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the persistentVolumes
     */
    @GetMapping(value = "{resourceName:.+}/yaml")
    public PersistentVolumes getPersistentVolumesYaml(@PathVariable(value = "cluster") String cluster,
                                                      @PathVariable(value = "namespace") String namespace,
                                                      @PathVariable(value = "resourceName") String resourceName) {
        return persistentVolumesService.getPersistentVolumesYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * PersistentVolumes 생성(Create PersistentVolumes)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createPersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @RequestBody String yaml) throws Exception{
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return persistentVolumesService.createPersistentVolumes(namespace, yaml);
    }

    /**
     * PersistentVolumes 삭제(Delete PersistentVolumes)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deletePersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName) {
        return persistentVolumesService.deletePersistentVolumes(namespace, resourceName, new HashMap<>());
    }

    /**
     * PersistentVolumes 수정(Update PersistentVolumes)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updatePersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @RequestBody String yaml) {
        return persistentVolumesService.updatePersistentVolumes(namespace, resourceName,yaml);
    }
}
