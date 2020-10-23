package org.paasta.container.platform.api.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@RestController
@RequestMapping("/namespaces/{namespace:.+}/endpoints")
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
     * @param namespace the namespace
     * @return the endpoints list
     */
    @GetMapping
    public EndpointsList getEndpointsList(@PathVariable("namespace") String namespace) {
        return endpointsService.getEndpointsList(namespace);
    }


    /**
     * Endpoints 상세 조회(Get Endpoints detail)
     *
     * @param namespace   the namespace
     * @param serviceName the service name
     * @return the endpoints detail
     */
    @GetMapping(value = "/{serviceName:.+}")
    public Endpoints getEndpoints(@PathVariable("namespace") String namespace, @PathVariable("serviceName") String serviceName) {
        return endpointsService.getEndpoints(namespace, serviceName);
    }

}
