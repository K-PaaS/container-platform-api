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

    //service
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

    @Value("${cpMaster.api.list.services.listAllNamespaces}")
    private String cpMasterApiListServicesListAllNamespacesUrl;

    //endpoint
    @Value("${cpMaster.api.list.endpoints.list}")
    private String cpMasterApiListEndpointsListUrl;

    @Value("${cpMaster.api.list.endpoints.get}")
    private String cpMasterApiListEndpointsGetUrl;

    @Value("${cpMaster.api.list.endpoints.listAllNamespaces}")
    private String cpMasterApiListEndpointsListAllNamespacesUrl;

    //pod
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

    @Value("${cpMaster.api.list.pods.listAllNamespaces}")
    private String cpMasterApiListPodsListAllNamespacesUrl;

    //node
    @Value("${cpMaster.api.list.nodes.list}")
    private String cpMasterApiListNodesListUrl;

    @Value("${cpMaster.api.list.nodes.get}")
    private String cpMasterApiListNodesGetUrl;

    //replicaSets
    @Value("${cpMaster.api.list.replicaSets.list}")
    private String cpMasterApiListReplicaSetsListUrl;

    @Value("${cpMaster.api.list.replicaSets.get}")
    private String cpMasterApiListReplicaSetsGetUrl;

    @Value("${cpMaster.api.list.replicaSets.delete}")
    private String cpMasterApiListReplicaSetsDeleteUrl;

    @Value("${cpMaster.api.list.replicaSets.create}")
    private String cpMasterApiListReplicaSetsCreateUrl;

    @Value("${cpMaster.api.list.replicaSets.update}")
    private String cpMasterApiListReplicaSetsUpdateUrl;

    @Value("${cpMaster.api.list.replicaSets.listAllNamespaces}")
    private String cpMasterApiListReplicaSetsListAllNamespacesUrl;

    //persistentVolumes
    @Value("${cpMaster.api.list.persistentVolumes.list}")
    private String cpMasterApiListPersistentVolumesListUrl;

    @Value("${cpMaster.api.list.persistentVolumes.get}")
    private String cpMasterApiListPersistentVolumesGetUrl;

    @Value("${cpMaster.api.list.persistentVolumes.create}")
    private String cpMasterApiListPersistentVolumesCreateUrl;

    @Value("${cpMaster.api.list.persistentVolumes.delete}")
    private String cpMasterApiListPersistentVolumesDeleteUrl;

    @Value("${cpMaster.api.list.persistentVolumes.update}")
    private String cpMasterApiListPersistentVolumesUpdateUrl;

    @Value("${cpMaster.api.list.persistentVolumes.listAllNamespaces}")
    private String cpMasterApiListPersistentVolumesListAllNamespacesUrl;

    //persistentVolumeClaims
    @Value("${cpMaster.api.list.persistentVolumeClaims.list}")
    private String cpMasterApiListPersistentVolumeClaimsListUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.get}")
    private String cpMasterApiListPersistentVolumeClaimsGetUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.create}")
    private String cpMasterApiListPersistentVolumeClaimsCreateUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.delete}")
    private String cpMasterApiListPersistentVolumeClaimsDeleteUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.update}")
    private String cpMasterApiListPersistentVolumeClaimsUpdateUrl;

    @Value("${cpMaster.api.list.persistentVolumeClaims.listAllNamespaces}")
    private String cpMasterApiListPersistentVolumeClaimsListAllNamespacesUrl;

    //storageClasses
    @Value("${cpMaster.api.list.storageClasses.list}")
    private String cpMasterApiListStorageClassesListUrl;

    @Value("${cpMaster.api.list.storageClasses.get}")
    private String cpMasterApiListStorageClassesGetUrl;

    @Value("${cpMaster.api.list.storageClasses.create}")
    private String cpMasterApiListStorageClassesCreateUrl;

    @Value("${cpMaster.api.list.storageClasses.delete}")
    private String cpMasterApiListStorageClassesDeleteUrl;

    @Value("${cpMaster.api.list.storageClasses.update}")
    private String cpMasterApiListStorageClassesUpdateUrl;

    @Value("${cpMaster.api.list.storageClasses.listAllNamespaces}")
    private String cpMasterApiListStorageClassesListAllNamespacesUrl;
    //event
    @Value("${cpMaster.api.list.events.list}")
    private String cpMasterApiListEventsListUrl;

    @Value("${cpMaster.api.list.events.get}")
    private String cpMasterApiListEventsGetUrl;

    @Value("${cpMaster.api.list.events.listAllNamespaces}")
    private String cpMasterApiListEventsListAllNamespacesUrl;

    //role
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

    @Value("${cpMaster.api.list.roles.listAllNamespaces}")
    private String cpMasterApiListRolesListAllNamespacesUrl;

    //deployments
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

    @Value("${cpMaster.api.list.deployments.listAllNamespaces}")
    private String cpMasterApiListDeploymentsListAllNamespacesUrl;

    //rolebinding
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

    @Value("${cpMaster.api.list.roleBindings.listAllNamespaces}")
    private String cpMasterApiListRoleBindingsListAllNamespacesUrl;

    //user
    @Value("${cpMaster.api.list.users.get}")
    private String cpMasterApiListUsersGetUrl;

    @Value("${cpMaster.api.list.users.create}")
    private String cpMasterApiListUsersCreateUrl;

    @Value("${cpMaster.api.list.users.delete}")
    private String cpMasterApiListUsersDeleteUrl;

    //namespace
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

    //resourceQuotas
    @Value("${cpMaster.api.list.resourceQuotas.list}")
    private String cpMasterApiListResourceQuotasListUrl;

    @Value("${cpMaster.api.list.resourceQuotas.get}")
    private String cpMasterApiListResourceQuotasGetUrl;

    @Value("${cpMaster.api.list.resourceQuotas.create}")
    private String cpMasterApiListResourceQuotasCreateUrl;

    @Value("${cpMaster.api.list.resourceQuotas.delete}")
    private String cpMasterApiListResourceQuotasDeleteUrl;

    @Value("${cpMaster.api.list.resourceQuotas.update}")
    private String cpMasterApiListResourceQuotasUpdateUrl;

    @Value("${cpMaster.api.list.resourceQuotas.listAllNamespaces}")
    private String cpMasterApiListResourceQuotasListAllNamespacesUrl;

    //secret
    @Value("${cpMaster.api.list.secrets.get}")
    private String cpMasterApiListSecretsGetUrl;

    @Value("${cpMaster.api.list.secrets.create}")
    private String cpMasterApiListSecretsCreateUrl;

    //clusterRoleBindings
    @Value("${cpMaster.api.list.clusterRoleBindings.create}")
    private String cpMasterApiListClusterRoleBindingsCreateUrl;

    @Value("${cpMaster.api.list.clusterRoleBindings.delete}")
    private String cpMasterApiListClusterRoleBindingsDeleteUrl;

    //limitRanges
    @Value("${cpMaster.api.list.limitRanges.create}")
    private String cpMasterApiListLimitRangesCreateUrl;
    
    @Value("${cpMaster.api.list.limitRanges.list}")
    private String cpMasterApiListLimitRangesListUrl;

    @Value("${cpMaster.api.list.limitRanges.get}")
    private String cpMasterApiListLimitRangesGetUrl;

    @Value("${cpMaster.api.list.limitRanges.delete}")
    private String cpMasterApiListLimitRangesDeleteUrl;

    @Value("${cpMaster.api.list.limitRanges.update}")
    private String cpMasterApiListLimitRangesUpdateUrl;

    @Value("${cpMaster.api.list.limitRanges.listAllNamespaces}")
    private String cpMasterApiListLimitRangesListAllNamespacesUrl;

}
