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
     * Instantiates a new PersistentVolumes controller
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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the persistentVolumes list
     */
    @GetMapping
    public Object getPersistentVolumesList(@PathVariable(value = "cluster") String cluster,
                                                          @PathVariable(value = "namespace") String namespace,
                                                          @RequestParam(required = false, defaultValue = "0") int limit,
                                                          @RequestParam(required = false, name = "continue") String continueToken,
                                                          @RequestParam(required = false) String searchParam,
                                                          @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return persistentVolumesService.getPersistentVolumesListAdmin(namespace, limit, continueToken, searchParam);
        }
        return persistentVolumesService.getPersistentVolumesList(namespace, limit, continueToken);

    }

    /**
     * PersistentVolumes 상세 조회(Get PersistentVolumes detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the persistentVolumes detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getPersistentVolumes(@PathVariable(value = "namespace") String namespace
            , @PathVariable(value = "resourceName") String resourceName
            , @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return persistentVolumesService.getPersistentVolumesAdmin(namespace, resourceName);
        }
        return persistentVolumesService.getPersistentVolumes(namespace, resourceName);
    }

    /**
     * PersistentVolumes YAML 조회(Get PersistentVolumes yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the persistentVolumes yaml
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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createPersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @RequestBody String yaml) throws Exception{
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return persistentVolumesService.createPersistentVolumes(namespace, yaml);
    }

    /**
     * PersistentVolumes 삭제(Delete PersistentVolumes)
     *
     * @param cluster the cluster
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
     * @param cluster the cluster
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
