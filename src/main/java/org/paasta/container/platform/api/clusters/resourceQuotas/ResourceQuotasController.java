package org.paasta.container.platform.api.clusters.resourceQuotas;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * ResourceQuotas Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 **/
@Api(value = "ResourceQuotasController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/resourceQuotas")
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
     * ResourceQuotas 목록 조회(Get ResourceQuotas list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the resourceQuotas list
     */
    @ApiOperation(value = "ResourceQuotas 목록 조회(Get ResourceQuotas list)", nickname = "getResourceQuotasList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "continue", value = "컨티뉴 토큰", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchParam", value = "검색 매개 변수", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getResourceQuotasList(@PathVariable(value = "cluster") String cluster,
                                        @PathVariable("namespace") String namespace,
                                        @RequestParam(required = false, defaultValue = "0") int limit,
                                        @RequestParam(required = false, name = "continue") String continueToken,
                                        @RequestParam(required = false) String searchParam,
                                        @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return resourceQuotasService.getResourceQuotasListAdmin(namespace, limit, continueToken, searchParam);
        }

        return resourceQuotasService.getResourceQuotasList(namespace, limit, continueToken);
    }

    /**
     * ResourceQuotas 상세 조회(Get ResourceQuotas detail)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the resourceQuotas detail
     */
    @ApiOperation(value = "ResourceQuotas 상세 조회(Get ResourceQuotas detail)", nickname = "getResourceQuotas")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName,
                                    @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return resourceQuotasService.getResourceQuotasAdmin(namespace, resourceName);
        }

        return resourceQuotasService.getResourceQuotas(namespace, resourceName);
    }


    /**
     * ResourceQuotas YAML 조회(Get ResourceQuotas yaml)
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the resourceQuotas yaml
     */
    @ApiOperation(value = "ResourceQuotas YAML 조회(Get ResourceQuotas yaml)", nickname = "getResourceQuotasYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "{resourceName:.+}/yaml")
    public Object getResourceQuotasYaml(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName,
                                                @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return resourceQuotasService.getResourceQuotasYaml(namespace, resourceName, new HashMap<>());
        }
        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }

    /**
     * ResourceQuotas 생성(Create ResourceQuotas)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @param isAdmin the isAdmin
     * @return return is succeeded
     */
    @ApiOperation(value = "ResourceQuotas 생성(Create ResourceQuotas)", nickname = "createResourceQuotas")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 생성 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PostMapping
    public Object createResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin,
                                       @RequestBody String yaml) throws Exception{
        if (isAdmin) {

            if (yaml.contains("---")) {
                Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
                return object;
            }

            return resourceQuotasService.createResourceQuotas(namespace, yaml);
        }
        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }

    /**
     * ResourceQuotas 삭제(Delete ResourceQuotas)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return return is succeeded
     */
    @ApiOperation(value = "ResourceQuotas 삭제(Delete ResourceQuotas)", nickname = "deleteResourceQuotas")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping(value = "/{resourceName:.+}")
    public ResultStatus deleteResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                             @PathVariable(value = "namespace") String namespace,
                                             @PathVariable(value = "resourceName") String resourceName,
                                             @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return resourceQuotasService.deleteResourceQuotas(namespace, resourceName, new HashMap<>());
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }

    /**
     * ResourceQuotas 수정(Update ResourceQuotas)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @param yaml the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "ResourceQuotas 수정(Update ResourceQuotas)", nickname = "updateResourceQuotas")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 수정 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PutMapping(value = "/{resourceName:.+}")
    public Object updateResourceQuotas(@PathVariable(value = "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName,
                                       @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin,
                                       @RequestBody String yaml) {
        if (isAdmin) {
            return resourceQuotasService.updateResourceQuotas(namespace, resourceName,yaml);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * ResourceQuota Default Template 목록 조회 (Get ResourceQouta Default Template list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param isAdmin the isAdmin
     * @return the object
     * @throws JsonProcessingException
     */
    @ApiOperation(value = "ResourceQuota Default Template 목록 조회 (Get ResourceQouta Default Template list)", nickname = "getResourceQuotasDefaultList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/template")
    public Object getResourceQuotasDefaultList(@PathVariable(value = "cluster") String cluster,
                                               @PathVariable(value = "namespace") String namespace,
                                               @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) throws JsonProcessingException {

        if (isAdmin) {
            return resourceQuotasService.getRqDefaultList(namespace);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


}
