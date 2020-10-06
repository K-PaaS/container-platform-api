package org.paasta.container.platform.api.signUp;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.CommonUtils.stringNullCheck;

/**
 * 회원가입 Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@RestController
@RequestMapping(value = "/users")
public class SignUpController {

    private final SignUpUserService signUpUserService;
    private final SignUpAdminService signUpAdminService;

    @Autowired
    public SignUpController(SignUpUserService signUpUserService, SignUpAdminService signUpAdminService) {
        this.signUpUserService = signUpUserService;
        this.signUpAdminService = signUpAdminService;
    }


    /**
     * 사용자 회원가입
     *
     * @param users the users
     * @return the ResultStatus
     */
    @PostMapping
    public ResultStatus registerUsers(@RequestBody Users users) {
        return signUpUserService.registerUser(users);
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
        return signUpAdminService.registerAdminUser(users);
    }

    /**
     * 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    @GetMapping(value = "/names")
    public Map<String, List> getUsersNameList() {
        return signUpUserService.getUsersNameList();
    }
}
