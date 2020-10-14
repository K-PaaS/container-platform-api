package org.paasta.container.platform.api.roles;

import io.swagger.annotations.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


/**
 * Roles Controller 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.13
 */
@Api(value = "RoleController v1")
@RestController
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/roles")
public class RolesController {

    private final RolesService rolesService;

    /**
     * Instantiates a new role services controller.
     *
     * @param rolesService the roles services service
     */
    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }


    /**
     * Roles 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the roles list
     */
    @ApiOperation(value="Roles 목록 조회", nickname="getRolesList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping
    public RolesList getRolesList(@PathVariable(value = "namespace") String namespace,
                                  @RequestParam(required = false, defaultValue = "0") int limit,
                                  @RequestParam(required = false, name = "continue") String continueToken)   {

        return rolesService.getRolesList(namespace, limit, continueToken);
    }



    /**
     * Roles 상세 정보를 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the resource name
     * @return the roles
     */
    @ApiOperation(value="Roles 상세 조회", nickname="getRoles")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "role 명", required = true, dataType = "string", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "SUCCESS")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Roles getRoles(@PathVariable(value = "namespace") String namespace,
                          @PathVariable(value = "resourceName") String resourceName) {

       return rolesService.getRoles(namespace, resourceName);
    }


    /**
     * Roles YAML을 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the resource name
     * @return the roles yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Roles getRolesYaml(@PathVariable(value = "namespace") String namespace,
                              @PathVariable(value = "resourceName") String resourceName) {

        return rolesService.getRolesYaml(namespace, resourceName, new HashMap<>());
    }




    /**
     * Roles 를 생성한다.
     *
     * @param namespace  the namespace
     * @param yaml  the yaml
     * @return  return is succeeded
     */
    @PostMapping
    public Object createRoles(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "namespace") String namespace,
                              @RequestBody String yaml) throws Exception {

        if(yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return rolesService.createRoles(namespace, yaml);
    }


    /**
     * Roles 를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteRoles(@PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName) {

        return rolesService.deleteRoles(namespace, resourceName, new HashMap<>());
    }


    /**
     * Role 를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return the roles
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateRoles(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "namespace") String namespace,
                              @PathVariable(value = "resourceName") String resourceName,
                              @RequestBody String yaml) {

        return rolesService.updateRoles(namespace, resourceName, yaml);
    }



}
