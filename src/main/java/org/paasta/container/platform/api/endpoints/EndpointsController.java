package org.paasta.container.platform.api.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.paasta.container.platform.api.workloads.deployments.Deployments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * Endpoints Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Api(value = "EndpointsController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/endpoints")
public class EndpointsController {

    private final EndpointsService endpointsService;

    /**
     * Instantiates a new Endpoints controller
     *
     * @param endpointsService the endpoints service
     */
    @Autowired
    public EndpointsController(EndpointsService endpointsService) {this.endpointsService = endpointsService;}


    /**
     * Endpoints 목록 조회(Get Endpoints list)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the endpoints list
     */
    @ApiOperation(value = "Endpoints 목록 조회(Get Endpoints list)", nickname = "getEndpointsList")
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
    public Object getEndpointsList(@PathVariable(value = "cluster") String cluster,
                                   @PathVariable(value = "namespace") String namespace,
                                   @RequestParam(required = false, defaultValue = "0") int offset,
                                   @RequestParam(required = false, defaultValue = "0") int limit,
                                   @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                                   @RequestParam(required = false, defaultValue = "") String order,
                                   @RequestParam(required = false, defaultValue = "") String searchName,
                                   @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (namespace.toLowerCase().equals(Constants.ALL_NAMESPACES)) {
            if (isAdmin) {
                return endpointsService.getEndPointsListAllNamespacesAdmin(offset, limit, orderBy, order, searchName);
            } else {
                return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
            }
        }
        if (isAdmin) {
            return endpointsService.getEndpointsListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }
        return endpointsService.getEndpointsList(namespace, offset, limit, orderBy, order, searchName);
    }


    /**
     * Endpoints 상세 조회(Get Endpoints detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the endpoints detail
     */
    @ApiOperation(value = "Endpoints 상세 조회(Get Endpoints detail)", nickname = "getEndpoints")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getEndpoints(@PathVariable(value = "namespace") String namespace,
                               @PathVariable(value = "resourceName") String resourceName,
                               @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return endpointsService.getEndpointsAdmin(namespace, resourceName);
        }

        return endpointsService.getEndpoints(namespace, resourceName);
    }

    /**
     * Endpoints YAML 조회(Get Endpoints yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the endpoints yaml
     */
    @ApiOperation(value = "Endpoints YAML 조회(Get Endpoints yaml)", nickname = "getEndpointsYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Endpoints getEndpointsYaml(@PathVariable(value = "namespace") String namespace,
                                      @PathVariable(value = "resourceName") String resourceName) {
        return endpointsService.getEndpointsYaml(namespace, resourceName, new HashMap<>());
    }

}
