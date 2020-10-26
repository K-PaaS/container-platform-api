package org.paasta.container.platform.api.endpoints;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.paasta.container.platform.api.workloads.deployments.Deployments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Endpoints Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
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
    @GetMapping
    public Object getEndpointsList(@PathVariable(value = "cluster") String cluster,
                                   @PathVariable(value = "namespace") String namespace,
                                   @RequestParam(required = false, defaultValue = "0") int limit,
                                   @RequestParam(required = false, name = "continue") String continueToken,
                                   @RequestParam(required = false) String searchParam,
                                   @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
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
    @GetMapping(value = "/{resourceName:.+}")
    public Object getEndpoints(@PathVariable(value = "namespace") String namespace
            , @PathVariable(value = "resourceName") String resourceName
            , @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

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
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Endpoints getEndpointsYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return endpointsService.getEndpointsYaml(namespace, resourceName, new HashMap<>());
    }

}
