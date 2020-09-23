package org.paasta.container.platform.api.workloads.deployments;

import io.swagger.annotations.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Deployments Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.08
 */
@Api(value = "DeploymentsController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/deployments")
public class DeploymentsController {

    private final DeploymentsService deploymentsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentsController.class);

    /**
     * Instantiates a new deployments controller.
     * @param deploymentsService the deployments service
     */
    @Autowired
    public DeploymentsController(DeploymentsService deploymentsService) {
        this.deploymentsService = deploymentsService;
    }

    /**
     * Deployments 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the deployments list
     */
    @ApiOperation(value="Deployments 목록 조회", nickname="getDeploymentList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping
    public DeploymentsList getDeploymentsList(@PathVariable(value = "namespace") String namespace, @RequestParam(required = false, defaultValue = "0") int limit, @RequestParam(required = false, name = "continue") String continueToken) {
        return deploymentsService.getDeploymentsList(namespace, limit, continueToken);
    }

    /**
     * Deployments 상세정보를 조회한다.
     *
     * @param namespace       the namespace
     * @param resourceName the resource name
     * @return the deployments
     */
    @ApiOperation(value="Deployments 상세 조회", nickname="getDeployment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "deploymentName", value = "deployment 명", required = true, dataType = "string", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Deployments getDeployments(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return deploymentsService.getDeployments(namespace, resourceName);
    }

    /**
     * Deployments YAML을 조회한다.
     *
     * @param namespace       the namespace
     * @param resourceName the resource name
     * @return the deployments yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Deployments getDeploymentsYaml(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "resourceName") String resourceName) {
        return deploymentsService.getDeploymentsYaml(namespace, resourceName, new HashMap<>());
    }

    /**
     * Deployments를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createDeployments(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @RequestBody String yaml) throws Exception {
        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return deploymentsService.createDeployments(namespace, yaml);

    }

    /**
     * Deployments를 삭제한다.
     *
     * @param namespace        the namespace
     * @param resourceName the resource name
     * @return the ResultStatus
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteDeployments(@PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName) {
        return deploymentsService.deleteDeployments(namespace, resourceName);
    }

    /**
     * Deployments를 수정한다.
     *
     * @param namespace       the namespace
     * @param resourceName the resource name
     * @return the deployments
     */
    @PutMapping("/{resourceName:.+}")
    public ResultStatus updateDeployments(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @PathVariable(value = "resourceName") String resourceName,
                                          @RequestBody String yaml) {
        return deploymentsService.updateDeployments(namespace, resourceName, yaml);
    }


}
