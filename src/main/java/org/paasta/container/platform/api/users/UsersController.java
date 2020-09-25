package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@RestController
@RequestMapping(value = "/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    // 회원가입
    // web user에서 api -> common-api 로 가야함...
    // service account 만들고 성공하면 그 다음 DB 생성
    @PostMapping
    public ResultStatus registerUsers(@RequestBody Users users) {
        return usersService.createUsers(users);
    }

//    @GetMapping
//    public UsersList getUsersList() {
//        return usersService.getUsersList();
//    }

    @GetMapping
    public List<String> getUsersNameList() {
        return usersService.getUsersNameList();
    }
}
