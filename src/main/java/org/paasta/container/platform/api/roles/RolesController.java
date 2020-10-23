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
     * Instantiates a new Roles controller
     *
     * @param rolesService the roles service
     */
    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }


    /**
     * Roles 목록 조회(Get Roles list)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the roles list
     */
    @GetMapping
    public Object getRolesList(@PathVariable(value = "cluster") String cluster,
                               @PathVariable(value = "namespace") String namespace,
                               @RequestParam(required = false, defaultValue = "0") int limit,
                               @RequestParam(required = false, name = "continue") String continueToken,
                               @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return rolesService.getRolesListAdmin(namespace, limit, continueToken);
        }
        return rolesService.getRolesList(namespace, limit, continueToken);
    }


    /**
     * Roles 상세 조회(Get Roles detail)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the roles detail
     */
    @GetMapping(value = "/{resourceName:.+}")
    public Object getRoles(@PathVariable(value = "cluster") String cluster,
                           @PathVariable(value = "namespace") String namespace,
                           @PathVariable(value = "resourceName") String resourceName,
                           @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return rolesService.getRolesAdmin(namespace, resourceName);
        }
        return rolesService.getRoles(namespace, resourceName);
    }


    /**
     * Roles YAML 조회(Get Roles yaml)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the roles yaml
     */
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Roles getRolesYaml(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "namespace") String namespace,
                              @PathVariable(value = "resourceName") String resourceName) {

        return rolesService.getRolesYaml(namespace, resourceName, new HashMap<>());
    }


    /**
     * Roles 생성(Create Roles)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    @PostMapping
    public Object createRoles(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "namespace") String namespace,
                              @RequestBody String yaml) throws Exception {

        if (yaml.contains("---")) {
            Object object = ResourceExecuteManager.commonControllerExecute(namespace, yaml);
            return object;
        }

        return rolesService.createRoles(namespace, yaml);
    }


    /**
     * Roles 삭제(Delete Roles)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    @DeleteMapping("/{resourceName:.+}")
    public ResultStatus deleteRoles(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "namespace") String namespace,
                                    @PathVariable(value = "resourceName") String resourceName) {

        return rolesService.deleteRoles(namespace, resourceName, new HashMap<>());
    }


    /**
     * Roles 수정(Update Roles)
     *
     * @param cluster      the cluster
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    @PutMapping("/{resourceName:.+}")
    public Object updateRoles(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "namespace") String namespace,
                              @PathVariable(value = "resourceName") String resourceName,
                              @RequestBody String yaml) {

        return rolesService.updateRoles(namespace, resourceName, yaml);
    }

}
