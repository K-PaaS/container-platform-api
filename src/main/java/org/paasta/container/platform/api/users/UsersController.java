package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.CommonUtils.stringNullCheck;

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
    private final AdminUserService adminUserService;

    @Autowired
    public UsersController(UsersService usersService, AdminUserService adminUserService) {
        this.usersService = usersService;
        this.adminUserService = adminUserService;
    }


    /**
     * 사용자 회원가입
     *
     * @param users the users
     * @return the ResultStatus
     */
    @PostMapping
    public ResultStatus registerUsers(@RequestBody Users users) {
        return usersService.registerUser(users);
    }


    /**
     * 운영자 회원가입
     *
     * @param adminUsers the object
     * @return the ResultStatus
     */
    @PostMapping(value = "/admin")
    @ResponseBody
    public ResultStatus registerAdminUser(@RequestBody Object adminUsers) {
        Object obj = stringNullCheck(adminUsers);
        if(obj instanceof ResultStatus) {
            return (ResultStatus) obj;
        }

        Users users = (Users) obj;
        return adminUserService.registerAdminUser(users);
    }

//    @GetMapping
//    public UsersList getUsersList() {
//        return usersService.getUsersList();
//    }


    /**
     * 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    @GetMapping
    public Map<String, List> getUsersNameList() {
        return usersService.getUsersNameList();
    }
}
