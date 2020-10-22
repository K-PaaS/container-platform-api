package org.paasta.container.platform.api.workloads.pods;


import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Pods Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.09
 */
@RestController
@RequestMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/pods")
public class PodsController {
    private final PodsService podsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PodsController.class);

    /**
     * Instantiates a new Pods controller
     *
     * @param podsService the pods service
     */
    @Autowired
    public PodsController(PodsService podsService) {
        this.podsService = podsService;
    }

    /**
     * Pods 목록 조회(Get Pods list)
     *
     * @param namespace the namespace
     * @return the pods list
     */

    @GetMapping
    @ResponseBody
    public PodsList getPodsList(@PathVariable(value = "namespace") String namespace, @RequestParam(required = false, defaultValue = "0") int limit, @RequestParam(required = false, name = "continue") String continueToken) {
        return podsService.getPodsList(namespace, limit, continueToken);
    }

    /**
     * Pods 목록 조회(Get Pods selector)
     *
     * @param namespace the namespace
     * @param selector  the selector
     * @return the pods list
     */
    @GetMapping(value = "/resources/{selector:.+}")
    @ResponseBody
    public PodsList getPodListBySelector(@PathVariable(value = "namespace") String namespace,
                                         @PathVariable(value = "selector") String selector) {
        return podsService.getPodListWithLabelSelector(namespace, selector);
    }

    /**
     * Pods 목록 조회(Get Pods node)
     *
     * @param namespace the namespace
     * @param nodeName  the node name
     * @return the pods list
     */
    @GetMapping(value = "/nodes/{nodeName:.+}")
    public PodsList getPodListByNode(@PathVariable(value = "namespace") String namespace,
                                     @PathVariable(value = "nodeName") String nodeName) {
        return podsService.getPodListByNode(namespace, nodeName);
    }

    /**
     * Pods 상세 조회(Get Pods detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the pods
     */

    @GetMapping(value = "/{resourceName:.+}")
    public Pods getPods(@PathVariable(value = "namespace") String namespace,
                        @PathVariable(value = "resourceName") String resourceName) {
        return podsService.getPods(namespace, resourceName);
    }

    /**
     * Pods YAML 조회(Get Pods yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the pods yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Pods getPodsYaml(@PathVariable(value = "namespace") String namespace,
                            @PathVariable(value = "resourceName") String resourceName) {
        return podsService.getPodsYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * Pods 생성(Create Pods)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createPods(@PathVariable(value = "cluster") String cluster,
                             @PathVariable(value = "namespace") String namespace,
                             @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return podsService.createPods(namespace, yaml);

    }

    /**
     * Pods 삭제(Delete Pods)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the resultStatus
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deletePods(@PathVariable(value = "namespace") String namespace,
                                   @PathVariable(value = "resourceName") String resourceName){
        return podsService.deletePods(namespace, resourceName, new HashMap<>());
    }

    /**
     * Pods 수정(Update Pods)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updatePods(@PathVariable(value = "cluster") String cluster,
                             @PathVariable(value = "namespace") String namespace,
                             @PathVariable(value ="resourceName") String resourceName,
                             @RequestBody String yaml) {
        return podsService.updatePods(namespace, resourceName, yaml);
    }


}
