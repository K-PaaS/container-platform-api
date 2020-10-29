package org.paasta.container.platform.api.common;

import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * Constants 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.26
 */
public class Constants {

    public static final String RESULT_STATUS_SUCCESS = "SUCCESS";
    public static final String RESULT_STATUS_FAIL = "FAIL";

    public static final String TARGET_CP_MASTER_API = "cpMasterApi";
    public static final String TARGET_COMMON_API = "commonApi";

    public static final String ACCEPT_TYPE_YAML = "application/yaml";

    public static final String TOKEN_KEY = "cp_admin";

    public static final String AUTH_CLUSTER_ADMIN = "CLUSTER_ADMIN";
    public static final String AUTH_NAMESPACE_ADMIN = "NAMESPACE_ADMIN";
    public static final String AUTH_USER = "USER";

    public static final String DEFAULT_NAMESPACE_NAME = "temp-namespace";
    public static final String NOT_ASSIGNED_ROLE = "NOT_ASSIGNED_ROLE";
    public static final String DEFAULT_INIT_ROLE = "init-role";
    public static final String DEFAULT_CLUSTER_ADMIN_ROLE = "cluster-admin"; // k8s default cluster role's name
    public static final String DEFAULT_RESOURCE_QUOTA_NAME = "cp-low-rq";
    public static final String DEFAULT_LIMIT_RANGE_NAME = "cp-memory-limit-range";

    static final String STRING_DATE_TYPE = "yyyy-MM-dd HH:mm:ss";
    static final String STRING_ORIGINAL_DATE_TYPE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    static final String STRING_TIME_ZONE_ID = "Asia/Seoul";

    static final String ACCEPT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;

    // COMMON API CALL URI
    public static final String URI_COMMON_API_ADMIN_TOKEN_DETAIL = "/adminToken/{tokenName:.+}";
    public static final String URI_COMMON_API_USERS = "/clusters/cp-cluster/namespaces/{namespace:.+}/users/{userId:.+}";
    public static final String URI_COMMON_API_USERS_DETAIL =  "/users/{userId:.+}";
    public static final String URI_COMMON_API_USERS_LIST =  "/users";
    public static final String URI_COMMON_API_USER_DETAIL_LOGIN =  "/users/login/{userId:.+}";
    public static final String URI_COMMON_API_USERS_LIST_BY_NAMESPACE = "/clusters/cp-cluster/namespaces/{namespace:.+}/users";
    public static final String URI_COMMON_API_USERS_NAMES_LIST = "/clusters/cp-cluster/namespaces/{namespace:.+}/users/names";
    public static final String URI_COMMON_API_USER_DELETE = "/users/";

    // NEXT ACTION MOVEMENT DASHBOARD URI
    public static final String URI_CLUSTER_NODES = "/container-platform/clusters/nodes";
    public static final String URI_CLUSTER_NAMESPACES = "/container-platform/clusters/namespaces";
    public static final String URI_INTRO_OVERVIEW = "/container-platform/intro/overview";
    public static final String URI_INTRO_ACCESS_INFO = "/container-platform/intro/accessInfo";
    public static final String URI_INTRO_PRIVATE_REGISTRY_INFO = "/container-platform/intro/privateRegistryInfo";

    public static final String URI_WORKLOAD_OVERVIEW = "/container-platform/workloads/overview";
    public static final String URI_WORKLOAD_DEPLOYMENTS = "/container-platform/workloads/deployments";
    public static final String URI_WORKLOAD_DEPLOYMENTS_DETAIL = "/container-platform/workloads/deployments/{deploymentName:.+}";
    public static final String URI_WORKLOAD_PODS = "/container-platform/workloads/pods";
    public static final String URI_WORKLOAD_PODS_DETAIL = "/container-platform/workloads/pods/{podName:.+}";
    public static final String URI_WORKLOAD_REPLICA_SETS = "/container-platform/workloads/replicaSets";
    public static final String URI_WORKLOAD_REPLICA_SETS_DETAIL = "/container-platform/workloads/replicaSets/{replicaSetName:.+}";

    public static final String URI_SERVICES = "/container-platform/services";
    public static final String URI_SERVICES_DETAIL = "/container-platform/services/{serviceName:.+}";

    public static final String URI_STORAGES = "/container-platform/storages";
    public static final String URI_STORAGES_PERSISTENT_VOLUME_CLAIM = "/container-platform/storages/persistentVolumeClaims";
    public static final String URI_STORAGES_PERSISTENT_VOLUME_CLAIM_DETAIL = "/container-platform/storages/persistentVolumeClaims/{persistentVolumeClaimName:.+}";
    public static final String URI_STORAGES_DETAIL = "/container-platform/storages/{persistentVolumeClaimName:.+}";

    public static final String URI_USERS = "/container-platform/users";
    public static final String URI_USERS_DETAIL = "/container-platform/users/{userId:.+}";
    public static final String URI_USERS_CONFIG = "/container-platform/users/config";

    public static final String URI_ROLES = "/container-platform/roles";

    public static final String URI_LIMITRANGES = "/container-platform";
    public static final String URI_LIMITRANGES_DETAIL = "/container-platform/{limitRangeName:.+}";


    //login
    public static final String LOGIN_SUCCESS = "Login Successful.";
    public static final String LOGIN_FAIL = "Login Failed.";
    public static final String NON_EXISTENT_ID = "존재하지 않는 사용자 아이디입니다.";
    public static final String UNAVAILABLE_ID = "해당 사용자 아이디는 사용할 수 없습니다.";
    public static final String INVALID_PASSWORD = "비밀번호가 올바르지 않습니다.";
    public static final String ID_REQUIRED = "사용자 아이디를 입력해주세요.";
    public static final String PASSWORD_REQUIRED = "비밀번호를 입력해주세요.";
    public static final String ID_PASSWORD_REQUIRED = "사용자 아이디와 비밀번호를 입력해주세요.";


    /** 서비스 요청시 처리 메소드 kind 매핑 정보 */
    public static final String RESOURCE_SERVICEACCOUNT = "ServiceAccount";
    public static final String RESOURCE_ROLEBINDING = "RoleBinding";
    public static final String RESOURCE_SECRET = "Secret";
    public static final String RESOURCE_ENDPOINTS = "Endpoints";
    public static final String RESOURCE_EVENTS = "Events";

    //cluster
    public static final String RESOURCE_NAMESPACE = "Namespace";
    public static final String RESOURCE_NODE = "Node";

    //workload
    public static final String RESOURCE_DEPLOYMENT = "Deployment";
    public static final String RESOURCE_POD = "Pod";
    public static final String RESOURCE_REPLICASET = "ReplicaSet";

    //service
    public static final String RESOURCE_SERVICE = "Service";

    //storage
    public static final String RESOURCE_PERSISTENTVOLUME = "PersistentVolume";
    public static final String RESOURCE_PERSISTENTVOLUMECLAIM = "PersistentVolumeClaim";
    public static final String RESOURCE_STORAGECLASS = "StorageClass";

    //management
    public static final String RESOURCE_RESOURCEQUOTA = "ResourceQuota";
    public static final String RESOURCE_LIMITRANGE = "LimitRange";
    public static final String RESOURCE_ROLE = "Role";


//    public static final List<String> RESOURCE_MAP = Collections.unmodifiableList(new ArrayList<String>(){
//        {
//            add(RESOURCE_POD);
//            add(RESOURCE_DEPLOYMENT);
//            add(RESOURCE_SERVICE);
//        }
//    });

    /** 서비스 클래스의 Package */
    public static final String SERVICE_PACKAGE = "org.paasta.container.platform.api.";

    public static final Map<String, String> RESOURCE_SERVICE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put(RESOURCE_ENDPOINTS, SERVICE_PACKAGE + "endpoints:EndpointsService");     // Endpoints 서비스
            put(RESOURCE_EVENTS, SERVICE_PACKAGE + "events:EventsService");     // Endpoints 서비스
            put(RESOURCE_NAMESPACE, SERVICE_PACKAGE + "clusters.namespaces:NamespacesService");     // Namespace 서비스
            put(RESOURCE_NODE, SERVICE_PACKAGE + "clusters.nodes:NodesService");     // Node 서비스
            put(RESOURCE_DEPLOYMENT, SERVICE_PACKAGE + "workloads.deployments:DeploymentsService");     // Deployment 서비스
            put(RESOURCE_POD, SERVICE_PACKAGE + "workloads.pods:PodsService");     // Pod 서비스
            put(RESOURCE_REPLICASET, SERVICE_PACKAGE + "workloads.pods:ReplicaSetsService");     // ReplicaSet 서비스
            put(RESOURCE_SERVICE, SERVICE_PACKAGE + "customServices:CustomServicesService");     // Service 서비스
            put(RESOURCE_PERSISTENTVOLUMECLAIM, SERVICE_PACKAGE + "storages.persistentVolumeClaims:PersistentVolumeClaimsService");     // PersistentVolumeClaim 서비스
            put(RESOURCE_STORAGECLASS, SERVICE_PACKAGE + "storages.storageClasses:StorageClassesService");     // StorageClass 서비스
            put(RESOURCE_RESOURCEQUOTA, SERVICE_PACKAGE + "managements.resourceQuotas:ResourceQuotasService");     // ResourceQuota 서비스
            put(RESOURCE_ROLE, SERVICE_PACKAGE + "roles:RolesService"); // Role 서비스
        }

    });

    public static final ResultStatus FORBIDDEN_ACCESS_RESULT_STATUS = new ResultStatus(Constants.RESULT_STATUS_FAIL, CommonStatusCode.FORBIDDEN.getMsg(),
            CommonStatusCode.FORBIDDEN.getCode(),CommonStatusCode.FORBIDDEN.getMsg(), null );

    public Constants() {
        throw new IllegalStateException();
    }

}
