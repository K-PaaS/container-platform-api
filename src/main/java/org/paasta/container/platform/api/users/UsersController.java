package org.paasta.container.platform.api.users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.MessageConstant;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.CommonUtils.regexMatch;

/**
 * User Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Api(value = "UsersController v1")
@RestController
public class UsersController {
    @Value("${cpNamespace.defaultNamespace}")
    private String defaultNamespace;

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Users 전체 목록 조회(Get Users all list)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param userType  the user type
     * @param searchName the searchName
     * @param limit      the limit
     * @param offset     the offset
     * @param orderBy    the orderBy
     * @param order      the order
     * @param isAdmin    the isAdmin
     * @return the users list
     */
    @ApiOperation(value = "Users 전체 목록 조회(Get Users list)", nickname = "getUsersList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userType", value = "유저 타입", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchName", value = "리소스 명 검색", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "목록 시작지점, 기본값 0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "정렬 기준, 기본값 creationTime(생성날짜)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "정렬 순서, 기본값 desc(내림차순)", required = false, dataType = "string", paramType = "query"),

    })
    @GetMapping(value = "/clusters/{cluster:.+}/users")
    public Object getUsersList(@PathVariable(value = "cluster") String cluster,
                               @RequestParam(name = "namespace", required = false) String namespace,
                               @RequestParam(defaultValue = "administrator") String userType,
                               @RequestParam(required = false, defaultValue = "") String searchName,
                               @RequestParam(required = false, defaultValue = "0") int limit,
                               @RequestParam(required = false, defaultValue = "0") int offset,
                               @RequestParam(required = false, defaultValue = "created") String orderBy,
                               @RequestParam(required = false, defaultValue = "desc") String order,
                               @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return usersService.getUsersAllByCluster(cluster, userType, searchName, limit, offset, orderBy, order);
        }

        return usersService.getUsersAll(namespace);
    }

    /**
     * 각 Namespace 별 Users 목록 조회(Get Users namespace list)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param isAdmin   the isAdmin
     * @return the users list
     */
    @ApiOperation(value = "각 Namespace 별 Users 목록 조회(Get Users namespace list)", nickname = "getUsersListByNamespace")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public Object getUsersListByNamespace(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return usersService.getUsersListByNamespaceAdmin(cluster, namespace);
        }
        return usersService.getUsersListByNamespace(cluster, namespace);
    }



    /**
     * Namespace 상세 Users 목록 조회(Get Users in namespace list)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the users list
     */
    @ApiOperation(value = "각 Namespace 별 Users 목록 조회(Get Users namespace list)", nickname = "getUsersListInNamespace")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "offset", value = "목록 시작지점, 기본값 0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "정렬 기준, 기본값 created(생성날짜)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "정렬 순서, 기본값 desc(내림차순)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchName", value = "userId 검색", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/namespace")
    public Object getUsersListInNamespace(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @RequestParam(required = false, defaultValue = "0") int offset,
                                          @RequestParam(required = false, defaultValue = "0") int limit,
                                          @RequestParam(required = false, defaultValue = "created") String orderBy,
                                          @RequestParam(required = false, defaultValue = "desc") String order,
                                          @RequestParam(required = false, defaultValue = "") String searchName,
                                          @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return usersService.getUsersListInNamespaceAdmin(cluster, namespace, offset, limit, orderBy, order, searchName);
        }
        return usersService.getUsersListByNamespace(cluster, namespace);
    }



    /**
     * 각 Namespace 별 Users 상세 조회(Get Users namespace detail)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param userId    the userId
     * @return the users list
     */
    @ApiOperation(value = "각 Namespace 별 Users 상세 조회(Get Users namespace detail)", nickname = "getUsersByNamespace")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/{userId:.+}")
    public Object getUsersByNamespace(@PathVariable(value = "cluster") String cluster,
                                      @PathVariable(value = "namespace") String namespace,
                                      @PathVariable(value = "userId") String userId) {
        if(Constants.ALL_NAMESPACES.equals(namespace)) {
            namespace = defaultNamespace;
        }

        return usersService.getUsers(cluster, namespace, userId);
    }



    /**
     * 하나의 Cluster 내 여러 Namespace 에 속한 User 에 대한 상세 조회(Get Users cluster namespace)
     *
     * @param cluster the cluster
     * @param userId  the userId
     * @param limit   the limit
     * @param offset  the offset
     * @return the users detail
     */
    @ApiOperation(value = "하나의 Cluster 내 여러 Namespace 에 속한 User 에 대한 상세 조회(Get Users cluster namespace)", nickname = "getUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "목록 시작지점, 기본값 0", required = false, dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object getUsers(@PathVariable(value = "cluster") String cluster,
                           @PathVariable(value = "userId") String userId,
                           @RequestParam(required = false, defaultValue = "0") int limit,
                           @RequestParam(required = false, defaultValue = "0") int offset) throws Exception {
        return usersService.getUsersInMultiNamespace(cluster, userId, limit, offset);
    }



    /**
     * 각 Namespace 별 등록 되어 있는 사용자들의 이름 목록 조회(Get Users registered list namespace)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the users list
     */
    @ApiOperation(value = "각 Namespace 별 등록 되어 있는 사용자들의 이름 목록 조회(Get Users registered list namespace)", nickname = "getUsersNameList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/names")
    public Map<String, List> getUsersNameList(@PathVariable(value = "cluster") String cluster,
                                              @PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersNameListByNamespace(cluster, namespace);
    }


    /**
     * Users 생성 (Create Users)
     * (Admin Portal)
     * 복수개의 Namespace 에 속할 수 있음
     *
     * @param cluster the cluster
     * @param users   the users
     * @return return is succeeded
     */
    @ApiOperation(value = "Users 생성 (Create Users)", nickname = "registerUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "users", value = "유저", required = true, dataType = "Users", paramType = "body")
    })
    @PostMapping(value = "/clusters/{cluster:.+}/users")
    public ResultStatus registerUsers(@PathVariable(value = "cluster") String cluster,
                                      @RequestBody Users users) {
        // input parameter regex
        if (!Constants.RESULT_STATUS_SUCCESS.equals(regexMatch(users))) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage(MessageConstant.RE_CONFIRM_INPUT_VALUE)
                    .httpStatusCode(400)
                    .detailMessage(regexMatch(users)).build();
        }

        if(users.getSelectValues().size() == 0) {
            return Constants.MANDATORY_NAMESPACE_AND_ROLE;
        }

        return usersService.registerUsers(users);
    }

    /**
     * Users 수정(Update Users)
     *
     * @param cluster the cluster
     * @param userId  the userId
     * @param users   the users
     * @param isAdmin the isAdmin
     * @return return is succeeded
     */
    @ApiOperation(value = "Users 수정(Update Users)", nickname = "modifyUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "users", value = "유저", required = true, dataType = "Users", paramType = "body")
    })
    @PutMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object modifyUsers(@PathVariable(value = "cluster") String cluster,
                              @PathVariable(value = "userId") String userId,
                              @RequestBody Users users,
                              @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) throws Exception {

        // input parameter regex
        if (!Constants.RESULT_STATUS_SUCCESS.equals(regexMatch(users))) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage(MessageConstant.RE_CONFIRM_INPUT_VALUE)
                    .httpStatusCode(400)
                    .detailMessage(regexMatch(users)).build();
        }

        // For Admin
        if (isAdmin) {
            return usersService.modifyUsersAdmin(cluster, userId, users);
        }

        return usersService.modifyUsers(userId, users);
    }


    /**
     * 운영자 정보 수정 (Update admin info)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param userId    the user id
     * @param users     the users
     * @param isAdmin   the isAdmin
     * @return return is succeeded
     */
    @ApiOperation(value = "Users 수정(Update Users)", nickname = "modifyUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "users", value = "유저", required = true, dataType = "Users", paramType = "body")
    })
    @PutMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/{userId:.+}")
    public Object modifyUsersInfo(@PathVariable(value = "cluster") String cluster,
                                  @PathVariable(value = "namespace") String namespace,
                                  @PathVariable(value = "userId") String userId,
                                  @RequestBody Users users,
                                  @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // input parameter regex
        if (!Constants.RESULT_STATUS_SUCCESS.equals(regexMatch(users))) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage(MessageConstant.RE_CONFIRM_INPUT_VALUE)
                    .httpStatusCode(400)
                    .detailMessage(regexMatch(users)).build();
        }

        if (isAdmin) {
            return usersService.modifyUsers(userId, users);
        }

        return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
    }


    /**
     * Users 권한 설정(Set Users authority)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param users     the users
     * @return return is succeeded
     */
    @ApiOperation(value = "Users 권한 설정(Set Users authority)", nickname = "modifyUsersConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "users", value = "유저 목록", required = true, dataType = "List<Users>", paramType = "body")
    })
    @PutMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public ResultStatus modifyUsersConfig(@PathVariable(value = "cluster") String cluster,
                                          @PathVariable(value = "namespace") String namespace,
                                          @RequestBody List<Users> users) {
        return usersService.modifyUsersConfig(cluster, namespace, users);
    }


    /**
     * Users 삭제(Delete Users)
     * (All Namespaces)
     *
     * @param cluster the cluster
     * @param userId  the user id
     * @return return is succeeded
     */
    @ApiOperation(value = "Users 삭제(Delete Users)", nickname = "deleteUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public ResultStatus deleteUsers(@PathVariable(value = "cluster") String cluster,
                                    @PathVariable(value = "userId") String userId) {
        return usersService.deleteUsersByAllNamespaces(userId);
    }


    /**
     * 특정 Namespace 관리자 판별이 포함된 Users Name 목록 조회(Get Users Name List containing Namespace Admin)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the UsersInNamespace
     */
    @ApiOperation(value = "특정 Namespace 관리자 판별이 포함된 Users Name 목록 조회(Get Users Name List containing Namespace Admin)", nickname = "getUsersNameListByNamespaceAdmin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/adminCheck")
    public UsersInNamespace getUsersNameListByNamespaceAdmin(@PathVariable(value = "cluster") String cluster,
                                                             @PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersNameListByNamespaceAdmin(cluster, namespace);
    }

}
