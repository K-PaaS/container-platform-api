package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * PersistentVolumeClaims Admin Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.18
 */
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/persistentVolumeClaims")
public class PersistentVolumeClaimsController {
    private final PersistentVolumeClaimsService persistentVolumeClaimsService;

    /**
     * Instantiates a new PersistentVolumeClaims controller
     *
     * @param persistentVolumeClaimsService the persistentVolumeClaims service
     */
    @Autowired
    public PersistentVolumeClaimsController(PersistentVolumeClaimsService persistentVolumeClaimsService) {
        this.persistentVolumeClaimsService = persistentVolumeClaimsService;
    }

    /**
     * PersistentVolumeClaims 목록 조회(Get PersistentVolumeClaims list)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the persistentVolumeClaims list
     */
    @GetMapping
    public Object getPersistentVolumeClaimsList(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @RequestParam(required = false, defaultValue = "0") int offset,
                                                @RequestParam(required = false, defaultValue = "0") int limit,
                                                @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                                                @RequestParam(required = false, defaultValue = "desc") String order,
                                                @RequestParam(required = false, defaultValue = "") String searchName,
                                                @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return persistentVolumeClaimsService.getPersistentVolumeClaimsListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }
        return persistentVolumeClaimsService.getPersistentVolumeClaimsList(namespace, offset, limit, orderBy, order, searchName);
    }

    /**
     * PersistentVolumeClaims 상세 조회(Get PersistentVolumeClaims detail)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param isAdmin      the isAdmin
     * @return the persistentVolumeClaims detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getPersistentVolumeClaims(@PathVariable(value = "namespace") String namespace
            , @PathVariable(value = "resourceName") String resourceName
            , @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return persistentVolumeClaimsService.getPersistentVolumeClaimsAdmin(namespace, resourceName);
        }
        return persistentVolumeClaimsService.getPersistentVolumeClaims(namespace, resourceName);
    }

    /**
     * PersistentVolumeClaims YAML 조회(Get PersistentVolumeClaims yaml)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the persistentVolumeClaims yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public PersistentVolumeClaims getPersistentVolumeClaimsYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return persistentVolumeClaimsService.getPersistentVolumeClaimsYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * PersistentVolumeClaims 생성(Create PersistentVolumeClaims)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createPersistentVolumeClaims(@PathVariable(value = "cluster") String cluster,
                                               @PathVariable(value = "namespace") String namespace,
                                               @RequestBody String yaml) throws Exception {
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return persistentVolumeClaimsService.createPersistentVolumeClaims(namespace, yaml);
    }

    /**
     * PersistentVolumeClaims 삭제(Delete PersistentVolumeClaims)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deletePersistentVolumeClaims(@PathVariable(value = "namespace") String namespace,
                                                     @PathVariable(value = "resourceName") String resourceName) {

        return persistentVolumeClaimsService.deletePersistentVolumeClaims(namespace, resourceName, new HashMap<>());
    }

    /**
     * PersistentVolumeClaims 수정(Update PersistentVolumeClaims)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updatePersistentVolumeClaims(@PathVariable(value = "cluster") String cluster,
                                               @PathVariable(value = "namespace") String namespace,
                                               @PathVariable(value = "resourceName") String resourceName,
                                               @RequestBody String yaml) {

        return persistentVolumeClaimsService.updatePersistentVolumeClaims(namespace, resourceName, yaml);
    }

}
