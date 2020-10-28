package org.paasta.container.platform.api.storages.storageClasses;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * StorageClasses Controller 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.13
 */
@Api(value = "StorageClassesController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/storageClasses")
public class StorageClassesController {
    private StorageClassesService storageClassesService;

    /**
     * Instantiates a new StorageClasses controller
     *
     * @param storageClassesService the storageClasses service
     */
    @Autowired
    public StorageClassesController(StorageClassesService storageClassesService){
        this.storageClassesService = storageClassesService;
    }

    /**
     * StorageClasses 목록 조회(Get StorageClasses list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the storageClasses list
     */
    @ApiOperation(value = "StorageClasses 목록 조회(Get StorageClasses list)", nickname = "getStorageClassesList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "continue", value = "컨티뉴 토큰", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchParam", value = "검색 매개 변수", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getStorageClassesList(@PathVariable(value = "cluster") String cluster,
                                        @PathVariable(value = "namespace") String namespace,
                                        @RequestParam(required = false, defaultValue = "0") int limit,
                                        @RequestParam(required = false, name = "continue") String continueToken,
                                        @RequestParam(required = false) String searchParam,
                                        @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return storageClassesService.getStorageClassesListAdmin(namespace, limit, continueToken, searchParam);
        }
        return storageClassesService.getStorageClassesList(namespace);
    }

    /**
     * StorageClasses 상세 조회(Get StorageClasses detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the storageClasses detail
     */
    @ApiOperation(value = "StorageClasses 상세 조회(Get StorageClasses detail)", nickname = "getStorageClasses")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getStorageClasses(@PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName,
                                    @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return storageClassesService.getStorageClassesAdmin(namespace, resourceName);
        }

        return storageClassesService.getStorageClasses(namespace, resourceName);
    }

    /**
     * StorageClasses YAML 조회(Get StorageClasses yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the storageClasses yaml
     */
    @ApiOperation(value = "StorageClasses YAML 조회(Get StorageClasses yaml)", nickname = "getStorageClassesYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public StorageClasses getStorageClassesYaml(@PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName){
        return storageClassesService.getStorageClassesYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * StorageClasses 생성(Create StorageClasses)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "StorageClasses 생성(Create StorageClasses)", nickname = "createStorageClasses")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 생성 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PostMapping
    public Object createStorageClasses(@PathVariable(value = "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @RequestBody String yaml) throws Exception {
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return storageClassesService.createStorageClasses(namespace, yaml);
    }

    /**
     * StorageClasses 삭제(Delete StorageClasses)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @ApiOperation(value = "StorageClasses 삭제(Delete StorageClasses)", nickname = "deleteStorageClasses")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteStorageClasses(@PathVariable(value = "namespace") String namespace,
                                             @PathVariable(value = "resourceName") String resourceName){

        return storageClassesService.deleteStorageClasses(namespace, resourceName, new HashMap<>());
    }

    /**
     * StorageClasses 수정(Update StorageClasses)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @ApiOperation(value = "StorageClasses 수정(Update StorageClasses)", nickname = "updateStorageClasses")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "yaml", value = "리소스 수정 yaml", required = true, dataType = "string", paramType = "body")
    })
    @PutMapping("/{resourceName:.+}")
    public Object updateStorageClasses(@PathVariable(value =  "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName,
                                       @RequestBody String yaml){

        return storageClassesService.updateStorageClasses(namespace, resourceName, yaml);
    }
}
