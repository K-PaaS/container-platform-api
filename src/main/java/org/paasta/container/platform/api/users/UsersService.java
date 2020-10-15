package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.Constants.TARGET_COMMON_API;

/**
 * User Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

    private final RestTemplateService restTemplateService;


    @Autowired
    public UsersService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }


    /**
     * 각 namespace별 사용자 목록 조회
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    public UsersList getUsersList(String namespace) {
        return restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST.replace("{namespace:.+}", namespace), HttpMethod.GET, null, UsersList.class);
    }


    /**
     * 각 namespace별 등록돼있는 사용자들의 이름 목록 조회
     *
     * @return the Map
     */
    public Map<String, List> getUsersNameListByNamespace(String namespace) {
        return restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_NAMES_LIST.replace("{namespace:.+}", namespace), HttpMethod.GET, null, Map.class);
    }


    /**
     * 사용자 로그인을 위한 상세 조회
     *
     * @return the Users
     */
    public Users getUsersDetailsForLogin (String userId) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DETAIL_LOGIN.replace("{userId:.+}", userId), HttpMethod.GET, null, Users.class);
    }

    /**
     * 사용자 상세 조회
     *
     * @return the Users
     */
    public UsersList getUsersDetails (String userId) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DETAIL.replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);
    }

}
