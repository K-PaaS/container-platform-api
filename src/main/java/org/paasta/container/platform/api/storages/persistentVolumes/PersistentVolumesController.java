package org.paasta.container.platform.api.storages.persistentVolumes;


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
 * PersistentVolumes Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@Api(value = "PersistentVolumesController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/persistentVolumes")
public class PersistentVolumesController {
    private final PersistentVolumesService persistentVolumesService;

    /**
     * Instantiates a new PersistentVolumes controller
     *
     * @param persistentVolumesService the persistentVolumes service
     */
    @Autowired
    public PersistentVolumesController(PersistentVolumesService persistentVolumesService){
        this.persistentVolumesService = persistentVolumesService;
    }

    /**
     * PersistentVolumes 목록 조회(Get PersistentVolumes list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the persistentVolumes list
     */
    @ApiOperation(value = "PersistentVolumes 목록 조회(Get PersistentVolumes list)", nickname = "getPersistentVolumesList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "continue", value = "컨티뉴 토큰", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchParam", value = "검색 매개 변수", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getPersistentVolumesList(@PathVariable(value = "cluster") String cluster,
                                           @PathVariable(value = "namespace") String namespace,
                                           @RequestParam(required = false, defaultValue = "0") int limit,
                                           @RequestParam(required = false, name = "continue") String continueToken,
                                           @RequestParam(required = false) String searchParam,
                                           @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return persistentVolumesService.getPersistentVolumesListAdmin(namespace, limit, continueToken, searchParam);
        }
        return persistentVolumesService.getPersistentVolumesList(namespace, limit, continueToken);

    }

    /**
     * PersistentVolumes 상세 조회(Get PersistentVolumes detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the persistentVolumes detail
     */
    @ApiOperation(value = "PersistentVolumes 상세 조회(Get PersistentVolumes detail)", nickname = "getPersistentVolumes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getPersistentVolumes(@PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName,
                                       @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return persistentVolumesService.getPersistentVolumesAdmin(namespace, resourceName);
        }
        return persistentVolumesService.getPersistentVolumes(namespace, resourceName);
    }

    /**
     * PersistentVolumes YAML 조회(Get PersistentVolumes yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the persistentVolumes yaml
     */
    @ApiOperation(value = "PersistentVolumes YAML 조회(Get PersistentVolumes yaml)", nickname = "getPersistentVolumesYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "{resourceName:.+}/yaml")
    public Object getPersistentVolumesYaml(@PathVariable(value = "cluster") String cluster,
                                           @PathVariable(value = "namespace") String namespace,
                                           @PathVariable(value = "resourceName") String resourceName,
                                           @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return persistentVolumesService.getPersistentVolumesYaml(namespace, resourceName, new HashMap<>());
        }
        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }

    /**
     * PersistentVolumes 생성(Create PersistentVolumes)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "PersistentVolumes 생성(Create PersistentVolumes)", nickname = "createPersistentVolumes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 생성 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PostMapping
    public Object createPersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin,
                                          @RequestBody String yaml) throws Exception{
        if (isAdmin) {

            if (yaml.contains("---")) {
                Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
                return object;
            }

            return persistentVolumesService.createPersistentVolumes(namespace, yaml);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }

    /**
     * PersistentVolumes 삭제(Delete PersistentVolumes)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @ApiOperation(value = "PersistentVolumes 삭제(Delete PersistentVolumes)", nickname = "deletePersistentVolumes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deletePersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName,
                                                @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return persistentVolumesService.deletePersistentVolumes(namespace, resourceName, new HashMap<>());
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }

    /**
     * PersistentVolumes 수정(Update PersistentVolumes)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "PersistentVolumes 수정(Update PersistentVolumes)", nickname = "updatePersistentVolumes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 수정 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PutMapping("/{resourceName:.+}")
    public Object updatePersistentVolumes(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin,
                                          @RequestBody String yaml) {

        if (isAdmin) {
            return persistentVolumesService.updatePersistentVolumes(namespace, resourceName,yaml);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }
}
