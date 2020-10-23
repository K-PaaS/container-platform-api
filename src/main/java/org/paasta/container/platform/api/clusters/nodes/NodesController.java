package org.paasta.container.platform.api.clusters.nodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Nodes Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.14
 */
@RestController
@RequestMapping(value = "/clusters/{cluster:.+}/nodes")
public class NodesController {
    private final NodesService nodesService;

    /**
     * Instantiates a new Nodes controller
     *
     * @param nodesService the nodes service
     */
    @Autowired
    public NodesController(NodesService nodesService) {
        this.nodesService = nodesService;
    }

    /**
     * Nodes 목록 조회(Get Nodes list)
     *
     * @return the nodes list
     */
    @GetMapping
    public NodesList getNodesList() {
        return nodesService.getNodesList();
    }


    /**
     * Nodes 상세 조회(Get Nodes detail)
     *
     * @param resourceName the resource name
     * @return the nodes detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Nodes getNodes(@PathVariable(value = "cluster") String cluster,
                          @PathVariable(value = "resourceName") String resourceName) {
        return nodesService.getNodes(resourceName);
    }

    /**
     * Nodes YAML 조회(Get Nodes yaml)
     *
     * @param resourceName the resource name
     * @return the nodes yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Nodes getNodesYaml(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "resourceName") String resourceName){
        return nodesService.getNodesYaml(resourceName, new HashMap<>());
    }
}
