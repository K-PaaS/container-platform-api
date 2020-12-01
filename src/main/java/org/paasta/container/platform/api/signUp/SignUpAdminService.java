package org.paasta.container.platform.api.signUp;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.adminToken.AdminTokenService;
import org.paasta.container.platform.api.clusters.clusters.ClustersService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.paasta.container.platform.api.common.Constants.*;

/**
 * Sign Up Admin Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class SignUpAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpAdminService.class);

    private final PropertyService propertyService;
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final AccessTokenService accessTokenService;
    private final UsersService usersService;
    private final ResourceYamlService resourceYamlService;
    private final AdminTokenService adminTokenService;
    private final ClustersService clustersService;

    /**
     * Instantiates a new SignUpAdminService service
     * @param propertyService the property service
     * @param restTemplateService the rest template service
     * @param commonService the common service
     * @param accessTokenService the access token service
     * @param usersService the users service
     * @param resourceYamlService the resource yaml service
     * @param adminTokenService the admin token service
     * @param clustersService the clusters service
     */
    @Autowired
    public SignUpAdminService(PropertyService propertyService, RestTemplateService restTemplateService, CommonService commonService, AccessTokenService accessTokenService, UsersService usersService, ResourceYamlService resourceYamlService, AdminTokenService adminTokenService, ClustersService clustersService) {
        this.propertyService = propertyService;
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.accessTokenService = accessTokenService;
        this.usersService = usersService;
        this.resourceYamlService = resourceYamlService;
        this.adminTokenService = adminTokenService;
        this.clustersService = clustersService;
    }


    /**
     * 운영자 회원가입(Sign Up Admin)
     *
     * @param users the user
     * @return the resultStatus
     */
    public ResultStatus signUpAdminUsers(Users users) {
        ResultStatus rsDb = new ResultStatus();

        String namespace = users.getCpNamespace();
        String username = users.getUserId();

        String defaultNamespace = propertyService.getDefaultNamespace();

        List<String> nsList = new ArrayList<>();
        nsList.add(namespace);
        nsList.add(defaultNamespace);

        // save admin token
        adminTokenService.saveAdminToken(users.getClusterToken());

        for (String reqNamespace : nsList) {
            if(reqNamespace.equals(namespace)) {
                // create row spec resource quota, limit range
                ResultStatus nsResult = resourceYamlService.createNamespace(namespace);

                if(Constants.RESULT_STATUS_FAIL.equals(nsResult.getResultCode())) {
                    return nsResult;
                }

                resourceYamlService.createDefaultResourceQuota(namespace,null);
                resourceYamlService.createDefaultLimitRanges(namespace, null);

                ResultStatus saResult = resourceYamlService.createServiceAccount(username, namespace);

                if(Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
                    return saResult;
                }

                ResultStatus rbResult = resourceYamlService.createRoleBinding(username, namespace, null);
                if(Constants.RESULT_STATUS_FAIL.equals(rbResult.getResultCode())) {
                    LOGGER.info("CLUSTER ROLE BINDING EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
                    restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", username), HttpMethod.DELETE, null, Object.class, true);
                    return rbResult;
                }

                String adminSaSecretName = restTemplateService.getSecretName(namespace, users.getUserId());
                users.setCpNamespace(namespace);
                users.setRoleSetCode(DEFAULT_CLUSTER_ADMIN_ROLE);
                users.setSaSecret(adminSaSecretName);
                users.setSaToken(accessTokenService.getSecrets(namespace, adminSaSecretName).getUserAccessToken());
                users.setUserType(AUTH_CLUSTER_ADMIN);
                users.setIsActive(CHECK_Y);

            } else {
                // add temporary namespace
                ResultStatus saResult = resourceYamlService.createServiceAccount(username, defaultNamespace);
                if(Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
                    return saResult;
                }

                String saSecretName = restTemplateService.getSecretName(defaultNamespace, username);
                users.setCpNamespace(defaultNamespace);
                users.setRoleSetCode(NOT_ASSIGNED_ROLE);
                users.setSaSecret(saSecretName);
                users.setSaToken(accessTokenService.getSecrets(defaultNamespace, saSecretName).getUserAccessToken());
                users.setUserType(AUTH_USER);
                users.setIsActive(CHECK_N);
            }

            users.setServiceAccountName(username);
            rsDb = usersService.createUsers(users);
        }

        clustersService.createClusters(users.getClusterApiUrl(), users.getClusterName(), users.getClusterToken());

        if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT, CLUSTER ROLE BINDING WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespacesDeleteUrl().replace("{namespace}", namespace), HttpMethod.DELETE, null, Object.class, true);
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListClusterRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", "cluster-admin-" + username), HttpMethod.DELETE, null, Object.class, true);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_INTRO_OVERVIEW);
    }

}
