package org.paasta.container.platform.api.users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    /**
     * Users 전체 목록 조회(Get Users all list)
     *
     * @return the Users list
     */
    @ApiOperation(value = "Users 전체 목록 조회(Get Users list)", nickname = "getUsersList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/users")
    public UsersListAdmin getUsersList(@RequestParam(name = "namespace") String namespace) {
        return usersService.getUsersAll(namespace);
    }


    /**
     * 각 Namespace 별 Users 목록 조회(Get Users namespace list)
     *
     * @param namespace the namespace
     * @return the Users list
     */
    @ApiOperation(value = "각 Namespace 별 Users 목록 조회(Get Users namespace list)", nickname = "getUsersListByNamespace")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public Object getUsersListByNamespace(@PathVariable(value = "namespace") String namespace,
                                          @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return usersService.getUsersListByNamespaceAdmin(namespace);
        }
        return usersService.getUsersListByNamespace(namespace);
    }

    /**
     * 각 Namespace 별 Users 상세 조회(Get Users namespace detail)
     *
     * @param namespace the namespace
     * @return the Users list
     */
    @ApiOperation(value = "각 Namespace 별 Users 상세 조회(Get Users namespace detail)", nickname = "getUsersByNamespace")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/{userId:.+}")
    public Users getUsersByNamespace(@PathVariable(value = "namespace") String namespace,
                                     @PathVariable(value = "userId") String userId) {
        return usersService.getUsers(namespace, userId);
    }


    /**
     * 하나의 Cluster 내 여러 Namespace 에 속한 User 에 대한 상세 조회(Get Users cluster namespace)
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "하나의 Cluster 내 여러 Namespace 에 속한 User 에 대한 상세 조회(Get Users cluster namespace)", nickname = "getUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object getUsers(@PathVariable(value = "userId") String userId) throws Exception {
        return usersService.getUsers(userId);
    }


    /**
     * 각 Namespace 별 등록 되어 있는 사용자들의 이름 목록 조회(Get Users registered list namespace)
     *
     * @return the Map
     */
    @ApiOperation(value = "각 Namespace 별 등록 되어 있는 사용자들의 이름 목록 조회(Get Users registered list namespace)", nickname = "getUsersNameList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/names")
    public Map<String, List> getUsersNameList(@PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersNameListByNamespace(namespace);
    }


    /**
     * Users 생성 (Create Users)
     * (Admin Portal)
     * 복수개의 Namespace 에 속할 수 있음
     *
     * @param users
     * @return
     */
    @ApiOperation(value = "Users 생성 (Create Users)", nickname = "registerUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "users", value = "유저", required = true, dataType = "Users", paramType = "body")
    })
    @PostMapping(value = "/clusters/{cluster:.+}/users")
    public ResultStatus registerUsers(@RequestBody Users users) {
        return usersService.registerUsers(users);
    }


    /**
     * Users 수정(Update Users)
     *
     * @param userId
     * @param users
     * @return
     */
    @ApiOperation(value = "Users 수정(Update Users)", nickname = "modifyUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "users", value = "유저", required = true, dataType = "Users", paramType = "body")
    })
    @PutMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object modifyUsers(@PathVariable(value = "userId") String userId,
                              @RequestBody Users users,
                              @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) throws Exception {

        // input parameter regex
        if(!Constants.RESULT_STATUS_SUCCESS.equals(regexMatch(users))) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage("입력 값을 다시 확인해 주세요.")
                    .httpStatusCode(400)
                    .detailMessage(regexMatch(users)).build();
        }

        // For Admin
        if(isAdmin) {
            return usersService.modifyUsersAdmin(userId, users);
        }

        return usersService.modifyUsers(userId, users);
    }


    /**
     * Users 권한 설정(Set Users authority)
     *
     * @param namespace the namespace
     * @param users the users
     * @return the resultStatus
     */
    @ApiOperation(value = "Users 권한 설정(Set Users authority)", nickname = "modifyUsersConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "users", value = "유저 목록", required = true, dataType = "List<Users>", paramType = "body")
    })
    @PutMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public ResultStatus modifyUsersConfig(@PathVariable(value = "namespace") String namespace,
                                          @RequestBody List<Users> users) {
        return usersService.modifyUsersConfig(namespace, users);
    }


    /**
     * Users 삭제(Delete Users)
     * (All Namespaces)
     *
     * @param userId   the user id
     * @return         ResultStatus
     */
    @ApiOperation(value = "Users 삭제(Delete Users)", nickname = "deleteUsers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저 Id", required = true, dataType = "string", paramType = "path")
    })
    @DeleteMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public ResultStatus deleteUsers(@PathVariable(value = "userId") String userId) {
        return usersService.deleteUsersByAllNamespaces(userId);
    }
}
