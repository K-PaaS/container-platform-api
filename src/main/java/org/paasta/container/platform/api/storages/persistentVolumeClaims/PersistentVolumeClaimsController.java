package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * PersistentVolumeClaims Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.18
 */
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/persistentvolumeclaims")
public class PersistentVolumeClaimsController {
    private final PersistentVolumeClaimsService persistentVolumeClaimsService;

    /**
     * Instantiates a new persistentVolumeClaims controller.
     *
     * @param persistentVolumeClaimsService the persistentVolumeClaims service
     */
    @Autowired
    public PersistentVolumeClaimsController(PersistentVolumeClaimsService persistentVolumeClaimsService) {
        this.persistentVolumeClaimsService = persistentVolumeClaimsService;
    }

    /**
     * PersistentVolumeClaims 목록을 조회한다.
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the PersistentVolumeClaims List
     */
    @GetMapping
    public PersistentVolumeClaimsList getPersistentVolumeClaimsList(@PathVariable(value = "namespace") String namespace,
                                                                    @RequestParam(required = false, defaultValue = "0") int limit,
                                                                    @RequestParam(required = false, name = "continue") String continueToken) {
        return persistentVolumeClaimsService.getPersistentVolumeClaimsList(namespace, limit, continueToken);
    }

    /**
     * PersistentVolumeClaims 상세 정보를 조회한다.
     *
     * @param namespace the namespace
     * @param resourceName the PersistentVolumeClaims name
     * @return the PersistentVolumeClaims
     */
    @GetMapping(value = "/{resourceName:.+}")
    public PersistentVolumeClaims getPersistentVolumeClaims(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return persistentVolumeClaimsService.getPersistentVolumeClaims(namespace, resourceName);
    }

    /**
     * PersistentVolumeClaims YAML 을 조회한다.
     *
     * @param namespace the namespace
     * @param resourceName the PersistentVolumeClaims name
     * @return the PersistentVolumeClaims
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public PersistentVolumeClaims getPersistentVolumeClaimsYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return persistentVolumeClaimsService.getPersistentVolumeClaimsYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * PersistentVolumeClaims 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return                return is succeeded
     */
    @PostMapping
    public Object createPersistentVolumeClaims(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return persistentVolumeClaimsService.createPersistentVolumeClaims(namespace, yaml);
    }

    /**
     * PersistentVolumeClaims 를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deletePersistentVolumeClaims(@PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName) {

        return persistentVolumeClaimsService.deletePersistentVolumeClaims(namespace, resourceName, new HashMap<>());
    }

    /**
     * PersistentVolumeClaims 를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return the services
     */
    @PutMapping("/{resourceName:.+}")
    public Object updatePersistentVolumeClaims(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @PathVariable(value = "resourceName") String resourceName,
                                 @RequestBody String yaml) {

        return persistentVolumeClaimsService.updatePersistentVolumeClaims(namespace, resourceName, yaml);
    }

}
