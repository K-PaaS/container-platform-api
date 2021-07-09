package org.paasta.container.platform.api.signUp;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.common.*;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersList;

import org.paasta.container.platform.api.users.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.paasta.container.platform.api.common.Constants.*;

/**
 * Sign Up User Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class SignUpUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpUserService.class);

    private final CommonService commonService;
    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;
    private final AccessTokenService accessTokenService;
    private final UsersService usersService;
    private final ResourceYamlService resourceYamlService;

    /**
     * Instantiates a new SignUpUserService service
     *
     * @param commonService the common service
     * @param propertyService the property service
     * @param restTemplateService the rest template service
     * @param accessTokenService the access token service
     * @param usersService the users service
     * @param resourceYamlService the resource yaml service
     */
    @Autowired
    public SignUpUserService(CommonService commonService, PropertyService propertyService, RestTemplateService restTemplateService, AccessTokenService accessTokenService, UsersService usersService, ResourceYamlService resourceYamlService) {
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
        this.accessTokenService = accessTokenService;
        this.usersService = usersService;
        this.resourceYamlService = resourceYamlService;
    }


    /**
     * 사용자 회원가입(User Sign Up)
     *
     * @param users the users
     * @return the resultStatus
     */
    public ResultStatus signUpUsers(Users users) {

        // 1. 해당 계정이 KEYCLOAK, CP USER 에 등록된 계정인지 확인
        UsersList registerUser = checkRegisterUser(users);

        // 2. KEYCLOAK 에 미등록 사용자인 경우, 메세지 리턴 처리
        if(registerUser.getResultMessage().equals(MessageConstant.USER_NOT_REGISTERED_IN_KEYCLOAK_MESSAGE)) {
            return USER_NOT_REGISTERED_IN_KEYCLOAK;
        }


        // 3. CP USER에 등록된 사용자인 경우, 메세지 리턴 처리
        if(registerUser.getItems().size() > 0) {
            return USER_ALREADY_REGISTERED;
        }


        //4. KEYCLOAK에서는 삭제된 계정이지만, CP에 남아있는 동일한 USER ID의 DB 컬럼, K8S SA, ROLEBINDING 삭제 진행
        UsersList usersList = getUsersListByUserId(users.getUserId());

        List<Users> deleteUsers = usersList.getItems().stream().filter(x-> !x.getUserAuthId().matches(users.getUserAuthId())).collect(Collectors.toList());

        for(Users du: deleteUsers) {
            usersService.deleteUsers(du);
        }


        // 5. CP-USER에 미등록인 사용자 CP-USER 계정 생성
        users.setCpNamespace(propertyService.getDefaultNamespace());
        users.setServiceAccountName(users.getUserId());
        users.setRoleSetCode(NOT_ASSIGNED_ROLE);
        users.setSaSecret(NULL_REPLACE_TEXT);
        users.setSaToken(NULL_REPLACE_TEXT);
        users.setUserType(AUTH_USER);

        // 6. 계정생성 COMMON-API REST SEND
        ResultStatus rsDb = sendSignUpUser(users);

        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED....");
            return CREATE_USERS_FAIL;
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, "/");
    }

    /**
     * Users 이름 목록 조회(Get Users names list)
     *
     * @return the Map
     */
    public Map<String, List<String>> getUsersNameList() {
        return restTemplateService.send(TARGET_COMMON_API, "/users/names", HttpMethod.GET, null, Map.class);
    }


    /**
     * 사용자 등록 여부 확인(Check for registered User)
     *
     * @param users the users
     * @return the usersList
     */
    public UsersList checkRegisterUser(Users users) {
        return restTemplateService.sendAdmin(TARGET_COMMON_API, URI_COMMON_API_CHECK_USER_REGISTER.replace("{userId:.+}", users.getUserId())
                .replace("{userAuthId:.+}", users.getUserAuthId()), HttpMethod.GET, null, UsersList.class);
    }


    /**
     * 사용자 회원가입 (Send user registration)
     *
     * @param users the users
     * @return return is succeeded
     */
    public ResultStatus sendSignUpUser(Users users) {
        return restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_SIGNUP, HttpMethod.POST, users, ResultStatus.class);
    }


    /**
     * 아이디로 존재하는 USER 계정 조회(Get users by user id)
     *
     * @param userId the userId
     * @return the users detail
     */
    public UsersList getUsersListByUserId(String userId) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);
    }

}
