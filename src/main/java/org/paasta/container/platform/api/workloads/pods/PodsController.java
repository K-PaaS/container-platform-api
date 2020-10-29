package org.paasta.container.platform.api.workloads.pods;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * Pods Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.09
 */
@Api(value = "PodsController v1")
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
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the pods list
     */
    @ApiOperation(value = "Pods 목록 조회(Get Pods list)", nickname = "getPodsList")
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
    @ResponseBody
    public Object getPodsList(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "namespace") String namespace,
                              @RequestParam(required = false, defaultValue = "0") int offset,
                              @RequestParam(required = false, defaultValue = "0") int limit,
                              @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                              @RequestParam(required = false, defaultValue = "desc") String order,
                              @RequestParam(required = false, defaultValue = "") String searchName,
                              @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return podsService.getPodsListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }
        return podsService.getPodsList(namespace, offset, limit, orderBy, order, searchName);
    }

    /**
     * Pods 목록 조회(Get Pods selector)
     *
     * @param namespace the namespace
     * @param selector  the selector
     * @return the pods list
     */
    @ApiOperation(value = "Pods 목록 조회(Get Pods selector)", nickname = "getPodListBySelector")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "selector", value = "셀렉터", required = true, dataType = "string", paramType = "query")
    })
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
    @ApiOperation(value = "Pods 목록 조회(Get Pods node)", nickname = "getPodListByNode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "nodeName", value = "노드 명", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/nodes/{nodeName:.+}")
    public PodsList getPodListByNode(@PathVariable(value = "namespace") String namespace,
                                     @PathVariable(value = "nodeName") String nodeName) {
        return podsService.getPodListByNode(namespace, nodeName);
    }

    /**
     * Pods 상세 조회(Get Pods detail)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param isAdmin      the isAdmin
     * @return the pods detail
     */
    @ApiOperation(value = "Pods 상세 조회(Get Pods detail)", nickname = "getPods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getPods(@PathVariable(value = "namespace") String namespace,
                          @PathVariable(value = "resourceName") String resourceName,
                          @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return podsService.getPodsAdmin(namespace, resourceName);
        }
        return podsService.getPods(namespace, resourceName);
    }

    /**
     * Pods YAML 조회(Get Pods yaml)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the pods yaml
     */
    @ApiOperation(value = "Pods YAML 조회(Get Pods yaml)", nickname = "getPodsYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Pods getPodsYaml(@PathVariable(value = "namespace") String namespace,
                            @PathVariable(value = "resourceName") String resourceName) {
        return podsService.getPodsYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * Pods 생성(Create Pods)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "Pods 생성(Create Pods)", nickname = "createPods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 생성 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PostMapping
    public Object createPods(@PathVariable(value = "cluster") String cluster,
                             @PathVariable(value = "namespace") String namespace,
                             @RequestBody String yaml) throws Exception {
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return podsService.createPods(namespace, yaml);

    }

    /**
     * Pods 삭제(Delete Pods)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @ApiOperation(value = "Pods 삭제(Delete Pods)", nickname = "deletePods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deletePods(@PathVariable(value = "namespace") String namespace,
                                   @PathVariable(value = "resourceName") String resourceName) {
        return podsService.deletePods(namespace, resourceName, new HashMap<>());
    }

    /**
     * Pods 수정(Update Pods)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "Pods 수정(Update Pods)", nickname = "updatePods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 수정 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PutMapping("/{resourceName:.+}")
    public Object updatePods(@PathVariable(value = "cluster") String cluster,
                             @PathVariable(value = "namespace") String namespace,
                             @PathVariable(value = "resourceName") String resourceName,
                             @RequestBody String yaml) {
        return podsService.updatePods(namespace, resourceName, yaml);
    }
}
