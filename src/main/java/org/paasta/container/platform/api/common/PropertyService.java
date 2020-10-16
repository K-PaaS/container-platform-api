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

    @Value("${limitRange.cpu}")
    private String limitRangeCpu;

    @Value("${limitRange.memory}")
    private String limitRangeMemory;

    @Value("${resourceQuota.limits.cpu}")
    private String resourceQuotaLimitsCpu;

    @Value("${resourceQuota.limits.memory}")
    private String resourceQuotaLimitsMemory;

    @Value("${resourceQuota.requests.storage}")
    private String resourceQuotaRequestsStorage;

    @Value("${cpMaster.api.list.services.list}")
    private String cpMasterApiListServicesListUrl;

    @Value("${cpMaster.api.list.services.get}")
    private String cpMasterApiListServicesGetUrl;

    @Value("${cpMaster.api.list.services.create}")
    private String cpMasterApiListServicesCreateUrl;

    @Value("${cpMaster.api.list.services.delete}")
    private String cpMasterApiListServicesDeleteUrl;

    @Value("${cpMaster.api.list.services.update}")
    private String cpMasterApiListServicesUpdateUrl;

    @Value("${cpMaster.api.list.endpoints.list}")
    private String cpMasterApiListEndpointsListUrl;

    @Value("${cpMaster.api.list.endpoints.get}")
    private String cpMasterApiListEndpointsGetUrl;

    @Value("${cpMaster.api.list.pods.list}")
    private String cpMasterApiListPodsListUrl;

    @Value("${cpMaster.api.list.pods.get}")
    private String cpMasterApiListPodsGetUrl;

    @Value("${cpMaster.api.list.pods.create}")
    private String cpMasterApiListPodsCreateUrl;

    @Value("${cpMaster.api.list.pods.delete}")
    private String cpMasterApiListPodsDeleteUrl;

    @Value("${cpMaster.api.list.pods.update}")
    private String cpMasterApiListPodsUpdateUrl;

    @Value("${cpMaster.api.list.nodes.list}")
    private String cpMasterApiListNodesListUrl;

    @Value("${cpMaster.api.list.nodes.get}")
    private String cpMasterApiListNodesGetUrl;

    @Value("${cpMaster.api.list.replicasets.list}")
    private String cpMasterApiListReplicasetsListUrl;

    @Value("${cpMaster.api.list.replicasets.get}")
    private String cpMasterApiListReplicasetsGetUrl;

    @Value("${cpMaster.api.list.replicasets.delete}")
    private String cpMasterApiListReplicasetsDeleteUrl;

    @Value("${cpMaster.api.list.replicasets.create}")
    private String cpMasterApiListReplicasetsCreateUrl;

    @Value("${cpMaster.api.list.replicasets.update}")
    private String cpMasterApiListReplicasetsUpdateUrl;

    @Value("${cpMaster.api.list.persistentvolumes.list}")
    private String cpMasterApiListPersistentvolumesListUrl;

    @Value("${cpMaster.api.list.persistentvolumes.get}")
    private String cpMasterApiListPersistentvolumesGetUrl;

    @Value("${cpMaster.api.list.persistentvolumeclaims.list}")
    private String cpMasterApiListPersistentvolumeclaimsListUrl;

    @Value("${cpMaster.api.list.persistentvolumeclaims.get}")
    private String cpMasterApiListPersistentvolumeclaimsGetUrl;

    @Value("${cpMaster.api.list.persistentvolumeclaims.create}")
    private String cpMasterApiListPersistentvolumeclaimsCreateUrl;

    @Value("${cpMaster.api.list.persistentvolumeclaims.delete}")
    private String cpMasterApiListPersistentvolumeclaimsDeleteUrl;

    @Value("${cpMaster.api.list.persistentvolumeclaims.update}")
    private String cpMasterApiListPersistentvolumeclaimsUpdateUrl;

    @Value("${cpMaster.api.list.storageclasses.list}")
    private String cpMasterApiListStorageclassesListUrl;

    @Value("${cpMaster.api.list.storageclasses.get}")
    private String cpMasterApiListStorageclassesGetUrl;

    @Value("${cpMaster.api.list.storageclasses.create}")
    private String cpMasterApiListStorageclassesCreateUrl;

    @Value("${cpMaster.api.list.storageclasses.delete}")
    private String cpMasterApiListStorageclassesDeleteUrl;

    @Value("${cpMaster.api.list.storageclasses.update}")
    private String cpMasterApiListStorageclassesUpdateUrl;

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
    private String cpMasterApiListDeploymentsListUrl;

    @Value("${cpMaster.api.list.deployments.get}")
    private String cpMasterApiListDeploymentsGetUrl;

    @Value("${cpMaster.api.list.deployments.create}")
    private String cpMasterApiListDeploymentsCreateUrl;

    @Value("${cpMaster.api.list.deployments.delete}")
    private String cpMasterApiListDeploymentsDeleteUrl;

    @Value("${cpMaster.api.list.deployments.update}")
    private String cpMasterApiListDeploymentsUpdateUrl;

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

    @Value("${cpMaster.api.list.namespaces.list}")
    private String cpMasterApiListNamespacesListUrl;

    @Value("${cpMaster.api.list.namespaces.get}")
    private String cpMasterApiListNamespacesGetUrl;

    @Value("${cpMaster.api.list.namespaces.create}")
    private String cpMasterApiListNamespacesCreateUrl;

    @Value("${cpMaster.api.list.namespaces.delete}")
    private String cpMasterApiListNamespacesDeleteUrl;

    @Value("${cpMaster.api.list.namespaces.update}")
    private String cpMasterApiListNamespacesUpdateUrl;

    @Value("${cpMaster.api.list.resourceQuotas.list}")
    private String cpMasterApiListResourceQuotasListUrl;

    @Value("${cpMaster.api.list.resourceQuotas.create}")
    private String cpMasterApiListResourceQuotasCreateUrl;

    @Value("${cpMaster.api.list.secrets.get}")
    private String cpMasterApiListSecretsGetUrl;

    @Value("${cpMaster.api.list.secrets.create}")
    private String cpMasterApiListSecretsCreateUrl;

    @Value("${cpMaster.api.list.clusterRoleBindings.create}")
    private String cpMasterApiListClusterRoleBindingsCreateUrl;

    @Value("${cpMaster.api.list.clusterRoleBindings.delete}")
    private String cpMasterApiListClusterRoleBindingsDeleteUrl;

    @Value("${cpMaster.api.list.limitRanges.create}")
    private String cpMasterApiListLimitRangesCreateUrl;

}
