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
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the roles list
     */
    @GetMapping
    public Object getRolesList(@PathVariable(value = "cluster") String cluster,
                               @PathVariable(value = "namespace") String namespace,
                               @RequestParam(required = false, defaultValue = "0") int offset,
                               @RequestParam(required = false, defaultValue = "0") int limit,
                               @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                               @RequestParam(required = false, defaultValue = "desc") String order,
                               @RequestParam(required = false, defaultValue = "") String searchName,
                               @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return rolesService.getRolesListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }
        return rolesService.getRolesList(namespace, offset, limit, orderBy, order, searchName);
    }


    /**
     * Roles 상세 조회(Get Roles detail)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
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
     * @param cluster the cluster
     * @param namespace the namespace
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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param yaml the yaml
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
     * @param cluster the cluster
     * @param namespace the namespace
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
     * @param cluster the cluster
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
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
