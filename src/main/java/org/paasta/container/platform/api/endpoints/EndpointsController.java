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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the endpoints list
     */
    @ApiOperation(value = "Endpoints 목록 조회(Get Endpoints list)", nickname = "getEndpointsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "continue", value = "컨티뉴 토큰", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchParam", value = "검색 매개 변수", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getEndpointsList(@PathVariable(value = "cluster") String cluster,
                                   @PathVariable(value = "namespace") String namespace,
                                   @RequestParam(required = false, defaultValue = "0") int limit,
                                   @RequestParam(required = false, name = "continue") String continueToken,
                                   @RequestParam(required = false) String searchParam,
                                   @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return endpointsService.getEndpointsListAdmin(namespace, limit, continueToken, searchParam);
        }
        return endpointsService.getEndpointsList(namespace, limit, continueToken);
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
