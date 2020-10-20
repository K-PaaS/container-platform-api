package org.paasta.container.platform.api.signUp;

import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.config.NoAuth;
import org.paasta.container.platform.api.users.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.CommonUtils.regexMatch;
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
     * @param requestUsers the requestUsers
     * @return the ResultStatus
     */
    @NoAuth
    @ApiOperation(value = "회원가입", httpMethod = "POST", hidden = true)
    @PostMapping(value = "/signUp")
    public ResultStatus signUpUsers(@RequestBody Object requestUsers) {
        Object obj = stringNullCheck(requestUsers);
        if(obj instanceof ResultStatus) {
            return (ResultStatus) obj;
        }

        Users users = (Users) obj;

        // input parameter regex
        if(!Constants.RESULT_STATUS_SUCCESS.equals(regexMatch(users))) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage("입력 값을 다시 확인해 주세요.")
                    .httpStatusCode(400)
                    .detailMessage(regexMatch(users)).build();
        }


        // id 중복 체크
        if(duplicatedUserIdCheck(users)) {
            return ResultStatus.builder().resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage("The User ID is already exist.")
                    .httpStatusCode(409)
                    .detailMessage("User ID가 중복입니다.").build();
        }

        // Admin을 판별할 수 있는 cluster token이 있는 경우
        if(!users.getClusterToken().isEmpty()) {
            return signUpAdminService.signUpAdminUsers(users);
        }

        return signUpUserService.signUpUsers(users);
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
