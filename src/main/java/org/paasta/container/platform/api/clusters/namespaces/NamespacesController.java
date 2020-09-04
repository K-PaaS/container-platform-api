package org.paasta.container.platform.api.clusters.namespaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Namespaces Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.02
 */
@RestController
@RequestMapping("/namespaces")
public class NamespacesController {

    private final NamespacesService namespacesService;

    /**
     * Instantiates a Namespaces controller
     *
     * @param namespacesService the namespaces service
     */
    @Autowired
    public NamespacesController(NamespacesService namespacesService) {
        this.namespacesService = namespacesService;
    }


    /**
     * Namespaces 상세정보를 조회한다.
     *
     * @param namespace the namespaces
     * @return the namespaces
     */
    @GetMapping("/{namespace:.+}")
    public Namespaces getNamespaces(@PathVariable("namespace") String namespace) {
        return namespacesService.getNamespaces(namespace);
    }


    /**
     * NameSpace를 생성한다.
     *
     * @param namespace the namespaces
     * @return Map
     */
    @PostMapping
    public Map<?,?> createNamespaces(@RequestBody Object namespace) {
        return namespacesService.createNamespaces(namespace);
    }

}