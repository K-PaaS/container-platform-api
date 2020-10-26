package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RestController
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    /**
     * Users 전체 목록 조회(Get Users list)
     *
     * @return the UsersListAdmin
     */
    @GetMapping(value = "/clusters/{cluster:.+}/users")
    public UsersListAdmin getUsersList(@RequestParam(name = "namespace") String namespace) {
        return usersService.getUsersAll(namespace);
    }


    /**
     * 각 Namespace 별 Users 목록 조회(Get Users namespace list)
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public UsersList getUsersListByNamespace(@PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersListByNamespace(namespace);
    }

    /**
     * 각 Namespace 별 Users 상세 조회(Get Users namespace detail)
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/{userId:.+}")
    public Users getUsersByNamespace(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "userId") String userId) {
        return usersService.getUsers(namespace, userId);
    }


    /**
     * 하나의 Cluster 내 여러 Namespace 에 속한 User 에 대한 상세 조회(Get Users cluster namespace)
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object getUsers(@PathVariable(value = "userId") String userId) throws Exception {
        return usersService.getUsers(userId);
    }


    /**
     * 각 Namespace 별 등록 되어 있는 사용자들의 이름 목록 조회(Get Users registered list namespace)
     *
     * @return the Map
     */
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
    @PutMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object modifyUsers(@PathVariable(value = "userId") String userId
            , @RequestBody Users users
            , @RequestParam(required = false, name = "isAdmin") boolean isAdmin) throws Exception {

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
    @PutMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public ResultStatus modifyUsersConfig(@PathVariable(value = "namespace") String namespace, @RequestBody List<Users> users) {
        return usersService.modifyUsersConfig(namespace, users);
    }


    /**
     * Users 삭제(Delete Users)
     * (All Namespaces)
     *
     * @param userId   the user id
     * @return         ResultStatus
     */
    @DeleteMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public ResultStatus deleteUsers(@PathVariable(value = "userId") String userId) {
        return usersService.deleteUsersByAllNamespaces(userId);
    }
}
