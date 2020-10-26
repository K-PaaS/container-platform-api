package org.paasta.container.platform.api.clusters.namespaces;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Namespaces Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.14
 */
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces")
public class NamespacesController {

    private final NamespacesService namespacesService;

    /**
     * Instantiates a new Namespaces controller
     *
     * @param namespacesService the namespaces service
     */
    @Autowired
    public NamespacesController(NamespacesService namespacesService) {
        this.namespacesService = namespacesService;
    }


    /**
     * Namespaces 목록 조회(Get Namespaces list)
     *
     * @param cluster the cluster
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the namespaces list
     */
    @GetMapping
    public Object getNamespacesList(@PathVariable(value = "cluster") String cluster,
                                            @RequestParam(required = false, defaultValue = "0") int limit,
                                            @RequestParam(required = false, name = "continue") String continueToken,
                                            @RequestParam(required = false) String searchParam,
                                            @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return namespacesService.getNamespacesListAdmin(limit, continueToken, searchParam);
        }
        return namespacesService.getNamespacesList(limit, continueToken);
    }

    /**
     * Namespaces 상세 조회(Get Namespaces detail)
     *
     * @param cluster the cluster
     * @param namespace the namespace name
     * @param isAdmin the isAdmin
     * @return the namespaces detail
     */
    @GetMapping("/{namespace:.+}")
    public Object getNamespaces(@PathVariable(value = "cluster") String cluster,
                                @PathVariable(value = "namespace") String namespace,
                                @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return namespacesService.getNamespacesAdmin(namespace);
        }
        return namespacesService.getNamespaces(namespace);
    }

    /**
     * Namespaces YAML 조회(Get Namespaces yaml)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @return the namespaces yaml
     */
    @GetMapping(value = "/{namespace:.+}/yaml")
    public Namespaces getNamespacesYaml(@PathVariable(value = "cluster") String cluster,
                                        @PathVariable(value = "namespace") String namespace){
        return namespacesService.getNamespacesYaml(namespace);
    }

    /**
     * Namespaces 생성(Create Namespaces)
     *
     * @param cluster the cluster
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createNamespaces(@PathVariable(value = "cluster") String cluster,
                                   @RequestBody String yaml) throws Exception {
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(null, yaml);
            return object;
        }

        return namespacesService.createNamespaces(yaml);
    }

    /**
     * Namespaces 삭제(Delete Namespaces)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @return return is succeeded
     */
    @DeleteMapping(value = "/{namespace:.+}")
    public ResultStatus deleteNamespaces(@PathVariable(value = "cluster") String cluster,
                                         @PathVariable("namespace") String namespace){
        return namespacesService.deleteNamespaces(namespace);
    }

    /**
     * Namespaces 수정(Update Namespaces)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping(value = "/{namespace:.+}")
    public Object updateNamespaces(@PathVariable(value = "cluster") String cluster,
                                   @PathVariable(value = "namespace") String namespace,
                                   @RequestBody String yaml){
        return namespacesService.updateNamespaces(namespace, yaml);
    }
}