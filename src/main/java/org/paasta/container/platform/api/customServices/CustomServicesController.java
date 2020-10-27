package org.paasta.container.platform.api.customServices;

import io.swagger.annotations.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * CustomServices Controller 클래스
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
     * Instantiates a new CustomServices controller
     *
     * @param customServicesService the customServices service
     */
    @Autowired
    public CustomServicesController(CustomServicesService customServicesService) {
        this.customServicesService = customServicesService;
    }


    /**
     * Services 목록 조회(Get Services list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @return the services list
     */
    @GetMapping
    public Object getCustomServicesList(@PathVariable(value = "cluster") String cluster,
                                        @PathVariable(value = "namespace") String namespace,
                                        @RequestParam(required = false, defaultValue = "0") int limit,
                                        @RequestParam(required = false, name = "continue") String continueToken,
                                        @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return customServicesService.getCustomServicesListAdmin(namespace, limit, continueToken);
        }

        return customServicesService.getCustomServicesList(namespace, limit, continueToken);
    }


    /**
     * Services 상세 조회(Get Services detail)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the services detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getCustomServices(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName,
                                    @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return customServicesService.getCustomServicesAdmin(namespace, resourceName);
        }

        return customServicesService.getCustomServices(namespace, resourceName);
    }


    /**
     * Services YAML 조회(Get Services yaml)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the services yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public CustomServices getCustomServicesYaml(@PathVariable(value = "cluster") String cluster,
                                                @PathVariable(value = "namespace") String namespace,
                                                @PathVariable(value = "resourceName") String resourceName) {

        return customServicesService.getCustomServicesYaml(namespace, resourceName, new HashMap<>());
    }


    /**
     * Services 생성(Create Services)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createServices(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @RequestBody String yaml) throws Exception {
        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return customServicesService.createServices(namespace, yaml);
    }


    /**
     * Services 삭제(Delete Services)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteServices(@PathVariable(value = "cluster") String cluster,
                                       @PathVariable(value = "namespace") String namespace,
                                       @PathVariable(value = "resourceName") String resourceName) {

        return customServicesService.deleteServices(namespace, resourceName, new HashMap<>());
    }


    /**
     * Services 수정(Update Services)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateServices(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @PathVariable(value = "resourceName") String resourceName,
                                 @RequestBody String yaml) {

        return customServicesService.updateServices(namespace, resourceName, yaml);
    }


//    /**
//     * Services 목록 조회 페이징 테스트 (Get Services list paging test)
//     *
//     * @param cluster the cluster
//     * @param namespace the namespace
//     * @param limit the limit
//     * @param offset the offset
//     * @param searchParam the searchParam
//     * @return the services list
//     */
//    @GetMapping("/test")
//    public CustomServicesList getCustomServicesListTest(@PathVariable(value = "cluster") String cluster,
//                                                        @PathVariable(value = "namespace") String namespace,
//                                                        @RequestParam(required = false, defaultValue = "0") int limit,
//                                                        @RequestParam(required = false, defaultValue = "0") int offset,
//                                                        @RequestParam(required = false, name = "searchParam") String searchParam) {
//
//        return customServicesService.getCustomServicesListTest(namespace, limit, offset, searchParam);
//    }
}
