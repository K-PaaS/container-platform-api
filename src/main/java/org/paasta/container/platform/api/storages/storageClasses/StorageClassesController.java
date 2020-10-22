package org.paasta.container.platform.api.storages.storageClasses;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * StorageClasses Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.13
 */
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/storageClasses")
public class StorageClassesController {
    private StorageClassesService storageClassesService;

    /**
     * Instantiates a new StorageClasses controller
     *
     * @param storageClassesService the storageClasses service
     */
    @Autowired
    public StorageClassesController(StorageClassesService storageClassesService){
        this.storageClassesService = storageClassesService;
    }

    /**
     * StorageClasses 목록 조회(Get StorageClasses list)
     *
     * @param namespace the namespace
     * @return the storageClasses list
     */
    @GetMapping
    public StorageClassesList getStorageClassesList(@PathVariable(value = "namespace") String namespace){
        return storageClassesService.getStorageClassesList(namespace);
    }

    /**
     * StorageClasses 상세 조회(Get StorageClasses detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the storageClasses
     */
    @GetMapping(value = "/{resourceName:.+}")
    public StorageClasses getStorageClasses(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName){
           return storageClassesService.getStorageClasses(namespace, resourceName);
    }

    /**
     * StorageClasses YAML 조회(Get StorageClasses yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the storageClasses yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public StorageClasses getStorageClassesYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName){
        return storageClassesService.getStorageClassesYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * StorageClasses 생성(Create StorageClasses)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createStorageClasses(@PathVariable(value = "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return storageClassesService.createStorageClasses(namespace, yaml);
    }

    /**
     * StorageClasses 삭제(Delete StorageClasses)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteStorageClasses(@PathVariable(value = "namespace") String namespace,
                                             @PathVariable(value = "resourceName") String resourceName){

        return storageClassesService.deleteStorageClasses(namespace, resourceName, new HashMap<>());
    }

    /**
     * StorageClasses 수정(Update StorageClasses)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateStorageClasses(@PathVariable(value =  "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName,
                                       @RequestBody String yaml){

        return storageClassesService.updateStorageClasses(namespace, resourceName, yaml);
    }
}
