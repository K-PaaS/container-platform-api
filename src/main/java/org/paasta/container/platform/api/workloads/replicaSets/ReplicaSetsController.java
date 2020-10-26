package org.paasta.container.platform.api.workloads.replicaSets;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * ReplicaSets Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.09.10
 */
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/replicaSets")
public class ReplicaSetsController {

    private final ReplicaSetsService replicaSetsService;

    /**
     * Instantiates a new ReplicaSets controller
     *
     * @param replicaSetsService the replicaSets service
     */
    @Autowired
    public ReplicaSetsController(ReplicaSetsService replicaSetsService) {
        this.replicaSetsService = replicaSetsService;
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the deployments list
     */
    @GetMapping
    public Object getReplicaSetsList(@PathVariable(value = "cluster") String cluster,
                                              @PathVariable(value = "namespace") String namespace,
                                              @RequestParam(required = false, defaultValue = "0") int limit,
                                              @RequestParam(required = false, name = "continue") String continueToken,
                                              @RequestParam(required = false) String searchParam,
                                              @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return replicaSetsService.ReplicaSetsListAdmin(namespace, limit, continueToken, searchParam);
        }
        return replicaSetsService.getReplicaSetsList(namespace, limit, continueToken);
    }


    /**
     * ReplicaSets 상세 조회(Get ReplicaSets detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the deployments detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getReplicaSets(@PathVariable(value = "namespace") String namespace
            , @PathVariable(value = "resourceName") String resourceName
            , @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return replicaSetsService.getReplicaSetsAdmin(namespace, resourceName);
        }
        return replicaSetsService.getReplicaSets(namespace, resourceName);
    }

    /**
     * ReplicaSets YAML 조회(Get ReplicaSets yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the replicaSets yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public ReplicaSets getReplicaSetsYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return replicaSetsService.getReplicaSetsYaml(namespace, resourceName);
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets selector)
     *
     * @param namespace namespace
     * @param selectors selectors
     * @return the replicaSets list
     */
    @GetMapping(value = "/resources/{selector:.+}")
    public ReplicaSetsList getReplicaSetsListLabelSelector(@PathVariable("namespace") String namespace, @PathVariable("selector") String selectors ) {
        return replicaSetsService.getReplicaSetsListLabelSelector(namespace, selectors);
    }


    /**
     * ReplicaSets 생성(Create ReplicaSets)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createReplicaSets(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @RequestBody String yaml) throws Exception {
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return replicaSetsService.createReplicaSets(namespace, yaml);
    }


    /**
     * ReplicaSets 삭제(Delete ReplicaSets)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping(value = "/{resourceName:.+}")
    public ResultStatus deleteReplicaSets(@PathVariable("namespace") String namespace,
                                          @PathVariable("resourceName") String resourceName) {
        return replicaSetsService.deleteReplicaSets(namespace, resourceName);
    }


    /**
     * ReplicaSets 수정(Update ReplicaSets)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping(value = "/{resourceName:.+}")
    public Object updateReplicaSets(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName,
                                    @RequestBody String yaml) {
        return replicaSetsService.updateReplicaSets(namespace, resourceName, yaml);
    }


}