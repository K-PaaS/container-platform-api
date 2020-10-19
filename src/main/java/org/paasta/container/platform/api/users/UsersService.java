package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.secret.Secrets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.paasta.container.platform.api.common.CommonService.convert;
import static org.paasta.container.platform.api.common.Constants.*;

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
    private final PropertyService propertyService;
    private final CommonService commonService;
    private final ResourceYamlService resourceYamlService;
    private final AccessTokenService accessTokenService;


    @Autowired
    public UsersService(RestTemplateService restTemplateService, PropertyService propertyService, CommonService commonService, ResourceYamlService resourceYamlService, AccessTokenService accessTokenService) {
        this.restTemplateService = restTemplateService;
        this.propertyService = propertyService;
        this.commonService = commonService;
        this.resourceYamlService = resourceYamlService;
        this.accessTokenService = accessTokenService;
    }


    /**
     * Users 전체 목록 조회
     *
     * @return
     */
    public UsersListAdmin getUsersList() {
        UsersListAdmin rsDb = restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST, HttpMethod.GET, null, UsersListAdmin.class);
        return (UsersListAdmin) commonService.setResultModel(commonService.setResultObject(rsDb, UsersListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * 각 namespace별 Users 목록 조회
     *
     * @param namespace the namespace
     * @return the UsersList
     */
    public UsersList getUsersListByNamespace(String namespace) {
        return restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE.replace("{namespace:.+}", namespace), HttpMethod.GET, null, UsersList.class);
    }


    /**
     * 하나의 Cluster 내 여러 Namespaces에 속한 User에 대한 상세 조회
     *
     * @param userId
     * @return
     */
    public Object getUsers(String userId) throws Exception {
        UsersList list = restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);

        UsersAdmin usersAdmin = new UsersAdmin();
        UsersAdmin.UsersDetails usersDetails;
        List<UsersAdmin.UsersDetails> usersDetailsList = new ArrayList<>();

        for(Users users:list.getItems()) {
            usersDetails = convert(users, UsersAdmin.UsersDetails.class);
            Object obj = restTemplateService.sendAdmin(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListSecretsGetUrl().replace("{namespace}", usersDetails.getCpNamespace()).replace("{name}", usersDetails.getSaSecret()), HttpMethod.GET, null, Map.class);

            if(obj instanceof ResultStatus) {
                return obj;
            }

            // k8s에서 secret 정보 조회
            Secrets secrets = (Secrets) commonService.setResultModel(commonService.setResultObject(obj, Secrets.class), Constants.RESULT_STATUS_SUCCESS);

            usersDetails.setSecrets(UsersAdmin.Secrets.builder()
                    .serviceAccountUid(secrets.getMetadata().getUid())
                    .secretLabels(secrets.getMetadata().getLabels())
                    .secretType(secrets.getType()).build());

            usersDetailsList.add(usersDetails);
        }

        usersAdmin.setUsersDetail(usersDetailsList);

        return commonService.setResultModel(commonService.setResultObject(usersAdmin, UsersAdmin.class), Constants.RESULT_STATUS_SUCCESS);
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
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);
    }


    /**
     * namespace와 userId로 사용자 단 건 상세 조회
     *
     * @param namespace
     * @param userId
     * @return
     */
    public Users getUsers(String namespace, String userId) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS.replace("{namespace:.+}", namespace).replace("{userId:.+}", userId), HttpMethod.GET, null, Users.class);
    }


    /**
     * 사용자 DB 저장
     *
     * @param users
     * @return
     */
    public ResultStatus createUsers(Users users) {
        return restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class);
    }

    /**
     * 사용자 생성 (운영자 portal)
     *
     * @param users
     * @return
     */
    public ResultStatus registerUsers(Users users) {
        List<Users.NamespaceRole> list = users.getSelectValues();
        ResultStatus rsDb = new ResultStatus();

        for (Users.NamespaceRole nsRole:list) {
            String namespace = nsRole.getNamespace();
            String role = nsRole.getRole();
            System.out.println("namespace = "+namespace + " & role = " + role);

            String userName = users.getUserId();

            // 각 namespace 별 service account 생성
            resourceYamlService.createServiceAccount(userName, namespace);

            // select box에서 선택한 role으로 role binding
            resourceYamlService.createRoleBinding(userName, namespace, role);

            String adminSaSecretName = restTemplateService.getSecretName(namespace, users.getUserId());

            users.setUserType(Constants.AUTH_USER);
            users.setCpNamespace(namespace);
            users.setServiceAccountName(userName);
            users.setRoleSetCode(role);
            users.setSaSecret(adminSaSecretName);
            users.setSaToken(accessTokenService.getSecret(namespace, adminSaSecretName).getUserAccessToken());
            users.setIsActive("Y");

            // DB에 저장
            rsDb = createUsers(users);

            // DB 커밋에 실패했을 경우 k8s 에 만들어진 service account 삭제
            if(Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
                LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
                restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", userName), HttpMethod.DELETE, null, Object.class);
                return rsDb;
            }
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS);
    }


    /**
     * Users 수정 (Admin Portal)
     *
     * @param userId
     * @param users
     * @return
     */
    public ResultStatus modifyUsers(String userId, Users users) throws Exception {
        ResultStatus rsDb = new ResultStatus();

        List<UsersAdmin.UsersDetails> usersDetails = ((UsersAdmin) getUsers(users.getServiceAccountName())).getUsersDetail();
        List<Users.NamespaceRole> selectValues = users.getSelectValues();

        // 기존 namespace list
        List<String> defaultNsList = usersDetails.stream().map(UsersAdmin.UsersDetails::getCpNamespace).collect(Collectors.toList());

        // 넘어온 새로운 select value 중 namespace list
        List<String> newNsList = selectValues.stream().map(Users.NamespaceRole::getNamespace).collect(Collectors.toList());


        // 새로운 것 기준
        for (UsersAdmin.UsersDetails details:usersDetails) {
            // 기존 namespace와 겹치는 namespace일 경우
            if(newNsList.contains(details.getCpNamespace())) {
                // role 까지 같은 지 확인
                // 같으면 그대로, 다르면 기존 role binding 지운뒤 새롭게 role binding
                for (Users.NamespaceRole nsRole:selectValues) {
                    String namespace = nsRole.getNamespace();
                    String role = nsRole.getRole();

                    // 기존 DB update
                    Users updateUser = getUsers(namespace, users.getServiceAccountName());

                    // role이 다를 경우
                    if(details.getCpNamespace().equalsIgnoreCase(nsRole.getNamespace()) && !details.getRoleSetCode().equalsIgnoreCase(nsRole.getRole())) {
                        LOGGER.info("Same Namespace >> {}, Default Role >> {}, New Role >> {}", namespace, details.getRoleSetCode(), role);
                        restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", users.getServiceAccountName() + "-" + details.getRoleSetCode() + "-binding"), HttpMethod.DELETE, null, Object.class);
                        resourceYamlService.createRoleBinding(users.getServiceAccountName(), namespace, role);

                        updateUser.setPassword(users.getPassword());
                        updateUser.setEmail(users.getEmail());
                        updateUser.setRoleSetCode(role);
                        updateUser.setSaToken(accessTokenService.getSecret(namespace, updateUser.getSaSecret()).getUserAccessToken());

                    } else{  // namespace, role 모두 같을 경우
                        updateUser.setPassword(users.getPassword());
                        updateUser.setEmail(users.getEmail());
                    }

                    rsDb = createUsers(updateUser);
                }

            } else {
                LOGGER.info("Default Namespace's service account delete >> " + details.getCpNamespace());
                Users deleteUser = getUsers(details.getCpNamespace(), users.getServiceAccountName());
                deleteUsers(deleteUser);
            }

        }


        // 기존 것 기준
        for (Users.NamespaceRole namespaceRole:selectValues) {
            String newNamespace = namespaceRole.getNamespace();
            String newSa = users.getServiceAccountName();

            // 기존에는 없는 namespace일 경우
            if(!defaultNsList.contains(newNamespace)) {
                LOGGER.info("New Namespace create >> {}, New Role >> {}", newNamespace, namespaceRole.getRole());

                // 새로운 namespace에 sa create
                resourceYamlService.createServiceAccount(newSa, newNamespace);

                // 새로운 namespace에 role binding
                resourceYamlService.createRoleBinding(newSa, newNamespace, namespaceRole.getRole());

                String saSecretName = restTemplateService.getSecretName(newNamespace, newSa);

                // DB 새로 insert
                Users newUser = users;

                newUser.setCpNamespace(namespaceRole.getNamespace());
                newUser.setRoleSetCode(namespaceRole.getRole());
                newUser.setIsActive("Y");
                newUser.setSaSecret(saSecretName);
                newUser.setSaToken(accessTokenService.getSecret(newNamespace, saSecretName).getUserAccessToken());
                newUser.setUserType("USER");

                rsDb = createUsers(newUser);
            }
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS_DETAIL.replace("{userId:.+}", users.getServiceAccountName()));
    }


    /**
     * 사용자 삭제
     *
     * @param users
     */
    public ResultStatus deleteUsers(Users users) {
        String namespace = users.getCpNamespace();
        String saName = users.getServiceAccountName();
        String role = users.getRoleSetCode();

        // 기존 service account 삭제
        restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", saName), HttpMethod.DELETE, null, Object.class);

        // role binding 삭제
        restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", saName + "-" + role + "-binding"), HttpMethod.DELETE, null, Object.class);

        // DB delete
        ResultStatus rsDb = (ResultStatus) restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DELETE + users.getId(), HttpMethod.DELETE, null, Object.class);

        if(RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            return rsDb;
        }

        return rsDb;
    }
}
