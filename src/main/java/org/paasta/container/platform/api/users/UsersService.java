package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.paasta.container.platform.api.common.Constants.TARGET_COMMON_API;
import static org.paasta.container.platform.api.common.Constants.TARGET_CP_MASTER_API;

/**
 * User Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class UsersService {

    private final CommonService commonService;
    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;

    @Autowired
    public UsersService(CommonService commonService, PropertyService propertyService, RestTemplateService restTemplateService) {
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
    }


    public ResultStatus createUsers(Users users) {
        // Todo (1) ::: service account 생성. 타겟은 temp-namespace
        String saYaml = "apiVersion: v1\n" +
                "kind: ServiceAccount\n" +
                "metadata:\n" +
                " name: " + users.getUserId() + "\n" +
                " namespace: " + Constants.DEFAULT_NAMESPACE_NAME;
        ResultStatus saResult = restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersCreateUrl().replace("{namespace}", Constants.DEFAULT_NAMESPACE_NAME), HttpMethod.POST, saYaml, ResultStatus.class);

        // Todo (2) ::: service account 생성 완료 시 아래 Common API 호출 고고!!!
        if(Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
            return saResult;
        }

        users.setCpNamespace(Constants.DEFAULT_NAMESPACE_NAME);
        users.setServiceAccountName(users.getUserId());

        ResultStatus resultStatus = restTemplateService.send(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class);
        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
    }

    public UsersList getUsersList() {
        return restTemplateService.send(TARGET_COMMON_API, "/users", HttpMethod.GET, null, UsersList.class);
    }

    public List<String> getUsersNameList() {
        return restTemplateService.send(TARGET_COMMON_API, "/users", HttpMethod.GET, null, List.class);
    }
}
