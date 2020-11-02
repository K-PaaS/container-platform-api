package org.paasta.container.platform.api.workloads.replicaSets;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * ReplicaSets Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.09.10
 */
@Api(value = "ReplicaSetsController v1")
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
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the deployments list
     */
    @ApiOperation(value = "ReplicaSets 목록 조회(Get ReplicaSets list)", nickname = "getReplicaSetsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "offset", value = "목록 시작지점, 기본값 0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "정렬 기준, 기본값 creationTime(생성날짜)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "정렬 순서, 기본값 desc(내림차순)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchName", value = "리소스 명 검색", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getReplicaSetsList(@PathVariable(value = "cluster") String cluster,
                                     @PathVariable(value = "namespace") String namespace,
                                     @RequestParam(required = false, defaultValue = "0") int offset,
                                     @RequestParam(required = false, defaultValue = "0") int limit,
                                     @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                                     @RequestParam(required = false, defaultValue = "desc") String order,
                                     @RequestParam(required = false, defaultValue = "") String searchName,
                                     @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (namespace.toLowerCase().equals(Constants.ALL_NAMESPACES)) {
            if (isAdmin) {
                return replicaSetsService.getReplicaSetsListAllNamespacesAdmin(offset, limit, orderBy, order, searchName);
            } else {
                return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
            }
        }

        if (isAdmin) {
            return replicaSetsService.getReplicaSetsListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }
        return replicaSetsService.getReplicaSetsList(namespace, offset, limit, orderBy, order, searchName);
    }


    /**
     * ReplicaSets 상세 조회(Get ReplicaSets detail)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param isAdmin      the isAdmin
     * @return the deployments detail
     */
    @ApiOperation(value = "ReplicaSets 상세 조회(Get ReplicaSets detail)", nickname = "getReplicaSets")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getReplicaSets(@PathVariable(value = "namespace") String namespace,
                                 @PathVariable(value = "resourceName") String resourceName,
                                 @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return replicaSetsService.getReplicaSetsAdmin(namespace, resourceName);
        }
        return replicaSetsService.getReplicaSets(namespace, resourceName);
    }

    /**
     * ReplicaSets YAML 조회(Get ReplicaSets yaml)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the replicaSets yaml
     */
    @ApiOperation(value = "ReplicaSets YAML 조회(Get ReplicaSets yaml)", nickname = "getReplicaSetsYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public ReplicaSets getReplicaSetsYaml(@PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName) {
        return replicaSetsService.getReplicaSetsYaml(namespace, resourceName);
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets selector)
     *
     * @param namespace namespace
     * @param selector  selector
     * @return the replicaSets list
     */
    @ApiOperation(value = "ReplicaSets 목록 조회(Get ReplicaSets selector)", nickname = "getReplicaSetsListLabelSelector")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "selector", value = "셀렉터", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/resources")
    public ReplicaSetsList getReplicaSetsListLabelSelector(@PathVariable("namespace") String namespace,
                                                           @RequestParam(name = "selector", required = true, defaultValue = "") String selector) {
        return replicaSetsService.getReplicaSetsListLabelSelector(namespace, selector);
    }

    /**
     * ReplicaSets 생성(Create ReplicaSets)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "ReplicaSets 생성(Create ReplicaSets)", nickname = "createReplicaSets")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 생성 yaml", required = true, dataType = "string", paramType = "body")
    })
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
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @ApiOperation(value = "ReplicaSets 삭제(Delete ReplicaSets)", nickname = "deleteReplicaSets")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping(value = "/{resourceName:.+}")
    public ResultStatus deleteReplicaSets(@PathVariable("namespace") String namespace,
                                          @PathVariable("resourceName") String resourceName) {
        return replicaSetsService.deleteReplicaSets(namespace, resourceName);
    }


    /**
     * ReplicaSets 수정(Update ReplicaSets)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "ReplicaSets 수정(Update ReplicaSets)", nickname = "updateReplicaSets")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 수정 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PutMapping(value = "/{resourceName:.+}")
    public Object updateReplicaSets(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName,
                                    @RequestBody String yaml) {
        return replicaSetsService.updateReplicaSets(namespace, resourceName, yaml);
    }


}