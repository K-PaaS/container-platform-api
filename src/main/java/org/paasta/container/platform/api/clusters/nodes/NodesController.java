package org.paasta.container.platform.api.clusters.nodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Object getNodesList(@PathVariable(value = "cluster") String cluster,
                                  @RequestParam(required = false, defaultValue = "0") int limit,
                                  @RequestParam(required = false, name = "continue") String continueToken,
                                  @RequestParam(required = false) String searchParam,
                                  @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return nodesService.getNodesListAdmin(limit, continueToken, searchParam);
        }

        return nodesService.getNodesList(limit, continueToken);
    }


    /**
     * Nodes 상세 조회(Get Nodes detail)
     *
     * @param resourceName the resource name
     * @return the nodes detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getNodes(@PathVariable(value = "cluster") String cluster,
                          @PathVariable(value = "resourceName") String resourceName,
                          @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return nodesService.getNodesAdmin(resourceName);
        }

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
