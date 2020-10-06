package org.paasta.container.platform.api.signUp;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping(value = "/signUp")
    public ResultStatus signUpUsers(@RequestBody Users users) {
        if(duplicatedUserIdCheck(users)) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage("The User ID is already exist.")
                    .httpStatusCode(409)
                    .detailMessage("User ID가 중복입니다.").build();
        }

        return signUpUserService.signUpUsers(users);
    }


    /**
     * 운영자 회원가입
     *
     * @param adminUsers the object
     * @return the ResultStatus
     */
    @PostMapping(value = "/signUp/admin")
    public ResultStatus signUpAdminUsers(@RequestBody Object adminUsers) {
        Object obj = stringNullCheck(adminUsers);
        if(obj instanceof ResultStatus) {
            return (ResultStatus) obj;
        }

        Users users = (Users) obj;
        if(duplicatedUserIdCheck(users)) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                        .resultMessage("The User ID is already exist.")
                        .httpStatusCode(409)
                        .detailMessage("User ID가 중복입니다.").build();
        }

        return signUpAdminService.signUpAdminUsers(users);
    }

    /**
     * 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    @GetMapping(value = "/users/names")
    public Map<String, List<String>> getUsersNameList() {
        return signUpUserService.getUsersNameList();
    }


    /**
     * User ID 중복 체크
     *
     * @param users
     * @return
     */
    public Boolean duplicatedUserIdCheck(Users users) {
        Boolean isDuplicated = false;
        List<String> list = getUsersNameList().get("users");
        for (String name:list) {
            if (name.equals(users.getUserId())) {
                isDuplicated = true;
            }
        }
        return isDuplicated;
    }
}
