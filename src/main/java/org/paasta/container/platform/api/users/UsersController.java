package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * Users 전체 목록 조회
     *
     * @return
     */
    @GetMapping(value = "/clusters/{cluster:.+}/users")
    public UsersListAdmin getUsersList() {
        return usersService.getUsersList();
    }


    /**
     * 각 namespace별 Users 목록 조회
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
    public UsersList getUsersListByNamespace(@PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersListByNamespace(namespace);
    }

    /**
     * 각 namespace별 Users 상세 조회
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/{userId:.+}")
    public Users getUsersByNamespace(@PathVariable(value = "namespace") String namespace, @PathVariable(value = "userId") String userId) {
        return usersService.getUsers(namespace, userId);
    }


    /**
     * 하나의 Cluster 내 여러 Namespaces에 속한 User에 대한 상세 조회
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public Object getUsersList(@PathVariable(value = "userId") String userId) throws Exception {
        return usersService.getUsers(userId);
    }


    /**
     * 각 namespace별 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    @GetMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users/names")
    public Map<String, List> getUsersNameList(@PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersNameListByNamespace(namespace);
    }


    /**
     * Users 생성 (Admin Portal)
     * 복수 개의 namespace에 속할 수 있다.
     *
     * @param users
     * @return
     */
    @PostMapping(value = "/clusters/{cluster:.+}/users")
    public ResultStatus registerUsers(@RequestBody Users users) {
        return usersService.registerUsers(users);
    }


    /**
     * Users 수정 (Admin Portal)
     * 복수 개의 namespace에 속할 수 있다.
     *
     * @param userId
     * @param users
     * @return
     */
    @PutMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public ResultStatus modifyUsers(@PathVariable(value = "userId") String userId, @RequestBody Users users) throws Exception {
        return usersService.modifyUsers(userId, users);
    }


    /**
     * 사용자 삭제 (All Namespaces)
     *
     * @param userId   the user id
     * @return         ResultStatus
     */
    @DeleteMapping(value = "/clusters/{cluster:.+}/users/{userId:.+}")
    public ResultStatus deleteUsers(@PathVariable(value = "userId") String userId) {
        return usersService.deleteUsersByAllNamespaces(userId);
    }
}
