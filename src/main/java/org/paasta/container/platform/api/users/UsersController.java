package org.paasta.container.platform.api.users;

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
@RequestMapping(value = "/clusters/{cluster:.+}/namespaces/{namespace:.+}/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    /**
     * 각 namespace별 사용자 목록 조회
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    @GetMapping
    public UsersList getUsersList(@PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersList(namespace);
    }


    /**
     * 각 namespace별 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    @GetMapping(value = "/names")
    public Map<String, List> getUsersNameList(@PathVariable(value = "namespace") String namespace) {
        return usersService.getUsersNameListByNamespace(namespace);
    }
}
