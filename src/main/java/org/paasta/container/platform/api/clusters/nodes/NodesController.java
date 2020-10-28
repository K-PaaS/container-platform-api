package org.paasta.container.platform.api.clusters.nodes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * Nodes Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.14
 */
@Api(value = "NodesController v1")
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
     * @param cluster the cluster
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the nodes list
     */
    @ApiOperation(value = "Nodes 목록 조회(Get Nodes list)", nickname = "getNodesList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "continue", value = "컨티뉴 토큰", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchParam", value = "검색 매개 변수", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getNodesList(@PathVariable(value = "cluster") String cluster,
                               @RequestParam(required = false, defaultValue = "0") int limit,
                               @RequestParam(required = false, name = "continue") String continueToken,
                               @RequestParam(required = false) String searchParam,
                               @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return nodesService.getNodesListAdmin(limit, continueToken, searchParam);
        }

        return nodesService.getNodesList(limit, continueToken);
    }


    /**
     * Nodes 상세 조회(Get Nodes detail)
     *
     * @param cluster the cluster
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the nodes detail
     */
    @ApiOperation(value = "Nodes 상세 조회(Get Nodes detail)", nickname = "getNodes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getNodes(@PathVariable(value = "cluster") String cluster,
                           @PathVariable(value = "resourceName") String resourceName,
                           @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return nodesService.getNodesAdmin(resourceName);
        }

        return nodesService.getNodes(resourceName);
    }

    /**
     * Nodes YAML 조회(Get Nodes yaml)
     *
     * @param cluster the cluster
     * @param resourceName the resource name
     * @return the nodes yaml
     */
    @ApiOperation(value = "Nodes YAML 조회(Get Nodes yaml)", nickname = "getNodesYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Nodes getNodesYaml(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "resourceName") String resourceName){
        return nodesService.getNodesYaml(resourceName, new HashMap<>());
    }
}
