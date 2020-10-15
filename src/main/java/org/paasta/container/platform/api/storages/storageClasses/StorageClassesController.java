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
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/storageclasses")
public class StorageClassesController {
    private StorageClassesService storageClassesService;

    /**
     * Instantiates a new persistentVolumeClaims controller.
     *
     * @param storageClassesService the storageClasses service
     */
    @Autowired
    public StorageClassesController(StorageClassesService storageClassesService){
        this.storageClassesService = storageClassesService;
    }

    /**
     * StorageClasses 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the StorageClasses List
     */
    @GetMapping
    public StorageClassesList getStorageClassesList(@PathVariable(value = "namespace") String namespace){
        return storageClassesService.getStorageClassesList(namespace);
    }

    /**
     * StorageClasses 상세 정보를 조회한다.
     *
     * @param namespace the namespace
     * @param resourceName the StorageClasses name
     * @return the StorageClasses
     */
    @GetMapping(value = "/{resourceName:.+}")
    public StorageClasses getStorageClasses(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName){
           return storageClassesService.getStorageClasses(namespace, resourceName);
    }

    /**
     * StorageClasses YAML 을 조회한다.
     *
     * @param namespace the namespace
     * @param resourceName the StorageClasses name
     * @return the StorageClasses
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public StorageClasses getStorageClassesYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName){
        return storageClassesService.getStorageClassesYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * StorageClasses 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return                return is succeeded
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
     * StorageClasses 를 삭제한다.
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
     * StorageClasses 를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return the services
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateStorageClasses(@PathVariable(value =  "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName,
                                       @RequestBody String yaml){

        return storageClassesService.updateStorageClasses(namespace, resourceName, yaml);
    }
}
