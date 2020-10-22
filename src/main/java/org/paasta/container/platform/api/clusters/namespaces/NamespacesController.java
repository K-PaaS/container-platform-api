package org.paasta.container.platform.api.clusters.namespaces;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
     * @return the namespaces list
     */
    @GetMapping
    public NamespacesList getNamespacesList(@RequestParam(required = false, defaultValue = "0") int limit, @RequestParam(required = false, name = "continue") String continueToken){
        return namespacesService.getNamespacesList(limit, continueToken);
    }

    /**
     * Namespaces 상세 조회(Get Namespaces detail)
     *
     * @param namespace the namespaces
     * @return the namespaces
     */
    @GetMapping("/{namespace:.+}")
    public Namespaces getNamespaces(@PathVariable("namespace") String namespace) {
        return namespacesService.getNamespaces(namespace);
    }

    /**
     * Namespaces YAML 조회(Get Namespaces yaml)
     *
     * @param namespace the namespace
     * @return the namespaces yaml
     */
    @GetMapping(value = "/{namespace:.+}/yaml")
    public Namespaces getNamespacesYaml(@PathVariable(value = "namespace") String namespace){
        return namespacesService.getNamespacesYaml(namespace);
    }

    /**
     * Namespaces 생성(Create Namespaces)
     *
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createNamespaces(@PathVariable(value = "cluster") String cluster,
                                   @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")){
            Object object = ResourceExecuteManager.commonControllerExecute(null, yaml);
            return object;
        }

        return namespacesService.createNamespaces(yaml);
    }

    /**
     * Namespaces 삭제(Delete Namespaces)
     *
     * @param namespace the namespace
     * @return return is succeeded
     */
    @DeleteMapping(value = "/{namespace:.+}")
    public ResultStatus deleteNamespaces(@PathVariable("namespace") String namespace){
        return namespacesService.deleteNamespaces(namespace);
    }

    /**
     * Namespaces 수정(Update Namespaces)
     *
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