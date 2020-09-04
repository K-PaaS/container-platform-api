package org.paasta.container.platform.api.managements.resourceQuotas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ResourceQuotas Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 **/

@RestController
@RequestMapping("/namespaces/{namespace:.+}/resourceQuotas")
public class ResourceQuotasController {
    private final ResourceQuotasService resourceQuotasService;

    /**
     * Instantiates a ResourceQuotas Controller
     *
     * @param resourceQuotasService the resourceQuotas Service
     */
    @Autowired
    public ResourceQuotasController(ResourceQuotasService resourceQuotasService) {
        this.resourceQuotasService = resourceQuotasService;
    }

    /**
     * ResourceQuota 목록을 조회한다.
     *
     * @param namespace the namespaces
     * @return the ResourceQuotaList
     */
    @GetMapping
    public ResourceQuotasList getResourceQuotasList(@PathVariable("namespace") String namespace) {
        return resourceQuotasService.getResourceQuotasList(namespace);
    }
}
