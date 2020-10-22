package org.paasta.container.platform.api.customServices;

import io.swagger.annotations.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Custom Services Controller 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.10
 */
@Api(value = "CustomServicesController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/services")
public class CustomServicesController {

    private final CustomServicesService customServicesService;

    /**
     * Instantiates a new Custom services controller.
     *
     * @param customServicesService the custom services service
     */
    @Autowired
    public CustomServicesController(CustomServicesService customServicesService) {
        this.customServicesService = customServicesService;
    }


    /**
     * Services 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the custom services list
     */
    @ApiOperation(value="Services 목록 조회", nickname="getCustomServicesList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping
    public CustomServicesList getCustomServicesList(@PathVariable(value = "namespace") String namespace,
                                                    @RequestParam(required = false, defaultValue = "0") int limit,
                                                    @RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, name = "searchParam") String searchParam)  {

        return customServicesService.getCustomServicesList(namespace, limit, offset, searchParam);
    }


    /**
     * Services 상세 정보를 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the service name
     * @return the custom services
     */
    @ApiOperation(value="Services 상세 조회", nickname="getCustomServices")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "service 명", required = true, dataType = "string", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public CustomServices getCustomServices(@PathVariable(value = "namespace") String namespace,
                                            @PathVariable(value = "resourceName") String resourceName) {
        return customServicesService.getCustomServices(namespace, resourceName);
    }


    /**
     * Services YAML을 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the service name
     * @return the custom services yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public CustomServices getCustomServicesYaml(@PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName) {

        return customServicesService.getCustomServicesYaml(namespace, resourceName, new HashMap<>());
    }




    /**
     * Services 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return                return is succeeded
     */
    @PostMapping
    public Object createServices(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return customServicesService.createServices(namespace, yaml);
    }


    /**
     * Services 를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteServices(@PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName) {

        return customServicesService.deleteServices(namespace, resourceName, new HashMap<>());
    }


    /**
     * Services 를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return the services
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateServices(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @PathVariable(value = "resourceName") String resourceName,
                                 @RequestBody String yaml) {

        return customServicesService.updateServices(namespace, resourceName, yaml);
    }




}
