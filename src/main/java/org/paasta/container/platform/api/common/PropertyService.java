package org.paasta.container.platform.api.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Property Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.26
 */
@Service
@Data
public class PropertyService {

    @Value("${cpMaster.api.url}")
    private String cpMasterApiUrl;

    @Value("${commonApi.url}")
    private String commonApiUrl;

    @Value("${cpMaster.api.list.services.list}")
    private String cpMasterApiListServicesListUrl;

    @Value("${cpMaster.api.list.services.get}")
    private String cpMasterApiListServicesGetUrl;

    @Value("${cpMaster.api.list.services.create}")
    private String cpMasterApiListServicesCreate;

    @Value("${cpMaster.api.list.services.delete}")
    private String cpMasterApiListServicesDelete;

    @Value("${cpMaster.api.list.services.update}")
    private String cpMasterApiListServicesUpdate;

    @Value("${cpMaster.api.list.endpoints.list}")
    private String cpMasterApiListEndpointsListUrl;

    @Value("${cpMaster.api.list.endpoints.get}")
    private String cpMasterApiListEndpointsGetUrl;

    @Value("${cpMaster.api.list.pods.list}")
    private String cpMasterApiListPodsListUrl;

    @Value("${cpMaster.api.list.pods.get}")
    private String cpMasterApiListPodsGetUrl;

    @Value("${cpMaster.api.list.pods.create}")
    private String cpMasterApiListPodsCreate;

    @Value("${cpMaster.api.list.pods.delete}")
    private String cpMasterApiListPodsDelete;

    @Value("${cpMaster.api.list.pods.update}")
    private String cpMasterApiListPodsUpdate;

    @Value("${cpMaster.api.list.nodes.list}")
    private String cpMasterApiListNodesListUrl;

    @Value("${cpMaster.api.list.nodes.get}")
    private String cpMasterApiListNodesGetUrl;

    @Value("${cpMaster.api.list.replicasets.list}")
    private String cpMasterApiListReplicasetsListUrl;

    @Value("${cpMaster.api.list.replicasets.get}")
    private String cpMasterApiListReplicasetsGetUrl;

    @Value("${cpMaster.api.list.replicasets.delete}")
    private String cpMasterApiListReplicasetsDelete;

    @Value("${cpMaster.api.list.replicasets.create}")
    private String cpMasterApiListReplicasetsCreate;

    @Value("${cpMaster.api.list.replicasets.update}")
    private String cpMasterApiListReplicasetsUpdate;

    @Value("${cpMaster.api.list.persistentvolumes.list}")
    private String cpMasterApiListPersistentvolumesListUrl;

    @Value("${cpMaster.api.list.persistentvolumes.get}")
    private String cpMasterApiListPersistentvolumesGetUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.list}")
    private String cpMasterApiListPersistentVolumeClaimsListUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.get}")
    private String cpMasterApiListPersistentVolumeClaimsGetUrl;

    @Value("${cpMaster.api.list.events.list}")
    private String cpMasterApiListEventsListUrl;

    @Value("${cpMaster.api.list.roles.list}")
    private String cpMasterApiListRolesListUrl;

    @Value("${cpMaster.api.list.roles.get}")
    private String cpMasterApiListRolesGetUrl;

    @Value("${cpMaster.api.list.roles.create}")
    private String cpMasterApiListRolesCreateUrl;

    @Value("${cpMaster.api.list.roles.delete}")
    private String cpMasterApiListRolesDeleteUrl;

    @Value("${cpMaster.api.list.roles.update}")
    private String cpMasterApiListRolesUpdateUrl;

    @Value("${cpMaster.api.list.deployments.list}")
    private String cpMasterApiListDeploymentsList;

    @Value("${cpMaster.api.list.deployments.get}")
    private String cpMasterApiListDeploymentsGet;

    @Value("${cpMaster.api.list.deployments.create}")
    private String cpMasterApiListDeploymentsCreate;

    @Value("${cpMaster.api.list.deployments.delete}")
    private String cpMasterApiListDeploymentsDelete;

    @Value("${cpMaster.api.list.deployments.update}")
    private String cpMasterApiListDeploymentsUpdate;

    @Value("${cpMaster.api.list.roleBindings.list}")
    private String cpMasterApiListRoleBindingsListUrl;

    @Value("${cpMaster.api.list.roleBindings.get}")
    private String cpMasterApiListRoleBindingsGetUrl;

    @Value("${cpMaster.api.list.roleBindings.create}")
    private String cpMasterApiListRoleBindingsCreateUrl;

    @Value("${cpMaster.api.list.roleBindings.delete}")
    private String cpMasterApiListRoleBindingsDeleteUrl;

    @Value("${cpMaster.api.list.roleBindings.update}")
    private String cpMasterApiListRoleBindingsUpdateUrl;

    @Value("${cpMaster.api.list.users.get}")
    private String cpMasterApiListUsersGetUrl;

    @Value("${cpMaster.api.list.users.create}")
    private String cpMasterApiListUsersCreateUrl;

    @Value("${cpMaster.api.list.users.delete}")
    private String cpMasterApiListUsersDeleteUrl;

    @Value("${cpMaster.api.list.namespaces.get}")
    private String cpMasterApiListNamespaceGetUrl;

    @Value("${cpMaster.api.list.namespaces.create}")
    private String cpMasterApiListNamespaceCreateUrl;

    @Value("${cpMaster.api.list.resourceQuotas.list}")
    private String cpMasterApiListResourceQuotasListUrl;

    @Value("${cpMaster.api.list.secrets.get}")
    private String cpMasterApiListSecretsGetUrl;

    @Value("${cpMaster.api.list.secrets.create}")
    private String cpMasterApiListSecretsCreateUrl;

    @Value("${cpMaster.api.list.clusterRoleBindings.create}")
    private String cpMasterApiListClusterRoleBindingsCreateUrl;

    @Value("${cpMaster.api.list.clusterRoleBindings.delete}")
    private String cpMasterApiListClusterRoleBindingsDeleteUrl;

}
