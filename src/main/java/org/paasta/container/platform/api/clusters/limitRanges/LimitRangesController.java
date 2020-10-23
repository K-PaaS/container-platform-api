package org.paasta.container.platform.api.clusters.limitRanges;

import io.swagger.annotations.*;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * LimitRanges Controller 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.22
 */
@Api(value = "LimitRangesController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/limitRanges")
public class LimitRangesController {

    private final LimitRangesService limitRangesService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LimitRangesController.class);

    /**
     * Instantiates a new LimitRanges controller
     *
     * @param limitRangesService the limitRanges service
     */
    @Autowired
    public LimitRangesController(LimitRangesService limitRangesService) {
        this.limitRangesService = limitRangesService;
    }

    /**
     * LimitRanges 목록 조회(Get LimitRanges list)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the limitRanges list
     */
    @GetMapping
    public Object getLimitRangesList(@PathVariable(value = "cluster") String cluster,
                                     @PathVariable(value = "namespace") String namespace,
                                     @RequestParam(required = false, defaultValue = "0") int limit,
                                     @RequestParam(required = false, name = "continue") String continueToken,
                                     @RequestParam(required = false) String searchParam,
                                     @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return limitRangesService.getLimitRangesListAdmin(namespace, limit, continueToken, searchParam);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * LimitRanges 상세 조회(Get LimitRanges detail)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the limitRanges
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getLimitRanges(@PathVariable(value = "cluster") String cluster,
                                 @PathVariable(value = "namespace") String namespace,
                                 @PathVariable(value = "resourceName") String resourceName,
                                 @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return limitRangesService.getLimitRangesAdmin(namespace, resourceName);
        }
        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * LimitRanges YAML 조회(Get LimitRanges yaml)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the limitRanges yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Object getLimitRangesYaml(@PathVariable(value = "cluster") String cluster,
                                     @PathVariable(value = "namespace") String namespace,
                                     @PathVariable(value = "resourceName") String resourceName,
                                     @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return limitRangesService.getLimitRangesYaml(namespace, resourceName, new HashMap<>());
        }
        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * LimitRanges 생성(Create LimitRanges)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createLimitRanges(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @RequestParam(required = false, name = "isAdmin") boolean isAdmin,
                                    @RequestBody String yaml) throws Exception {

        if (isAdmin) {

            if (yaml.contains("---")) {
                Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
                return object;
            }

            return limitRangesService.createLimitRanges(namespace, yaml);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * LimitRanges 삭제(Delete LimitRanges)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteLimitRanges(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return limitRangesService.deleteLimitRanges(namespace, resourceName);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * LimitRanges 수정(Update LimitRanges)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public ResultStatus updateLimitRanges(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @RequestParam(required = false, name = "isAdmin") boolean isAdmin,
                                          @RequestBody String yaml) {

        if (isAdmin) {
            return limitRangesService.updateLimitRanges(namespace, resourceName, yaml);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


}
