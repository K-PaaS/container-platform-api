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
     * Instantiates a new ReplicaSets controller.
     *
     * @param replicaSetsService the replicaSets service
     */
    @Autowired
    public ReplicaSetsController(ReplicaSetsService replicaSetsService) { this.replicaSetsService = replicaSetsService;}

    /**
     * ReplicaSets 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the replicaSets list
     */
    @GetMapping
    public ReplicaSetsList getReplicaSetsList(@PathVariable("namespace") String namespace){
        return replicaSetsService.getReplicaSetsList(namespace);
    }

    /**
     * ReplicaSets 상세정보를 조회한다.
     *
     * @param namespace the namespace
     * @param replicaSetName the replicaSets name
     * @return the replicaSets
     */
    @GetMapping(value = "/{replicaSetName:.+}")
    public ReplicaSets getReplicaSets(@PathVariable("namespace") String namespace, @PathVariable("replicaSetName") String replicaSetName) {
        return replicaSetsService.getReplicaSets(namespace, replicaSetName);
    }

    /**
     * ReplicaSets YAML을 조회한다.
     *
     * @param namespace the namespace
     * @param replicaSetName the replicaSets name
     * @return the replicaSets yaml
     */
    @GetMapping(value = "/{replicaSetName:.+}/yaml")
    public ReplicaSets getReplicaSetsYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "replicaSetName") String replicaSetName) {
        return replicaSetsService.getReplicaSetsYaml(namespace, replicaSetName);
    }

    /**
     * ReplicaSets 목록을 조회한다.(Label Selector)
     * @param namespace namespace
     * @param selectors selectors
     * @return the replicaSets list
     */
    @GetMapping(value = "/resources/{selector:.+}")
    public ReplicaSetsList getReplicaSetsListLabelSelector(@PathVariable("namespace") String namespace, @PathVariable("selector") String selectors ) {
        return replicaSetsService.getReplicaSetsListLabelSelector(namespace, selectors);
    }




    /**
     * ReplicaSets 을 생성한다.
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createReplicaSets(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return replicaSetsService.createReplicaSets(namespace, yaml);
    }


    /**
     * ReplicaSets 을 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the ResultStatus
     */
    @DeleteMapping(value = "/{resourceName:.+}")
    public ResultStatus deleteReplicaSets(@PathVariable("namespace") String namespace,
                                          @PathVariable("resourceName") String resourceName) {
        return replicaSetsService.deleteReplicaSets(namespace, resourceName, new HashMap<>());
    }


    /**
     * ReplicaSets을 수정한다.
     *
     * @param namespace the namespace
     * @param replicaSetName the replicaSets name
     * @param yaml the yaml
     * @return the replicaSets
     */
    @PutMapping(value = "/{replicaSetName:.+}")
    public Object updateReplicaSets(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "replicaSetName") String replicaSetName,
                                    @RequestBody String yaml) {
        return replicaSetsService.updateReplicaSets(namespace, replicaSetName, yaml);
    }


}