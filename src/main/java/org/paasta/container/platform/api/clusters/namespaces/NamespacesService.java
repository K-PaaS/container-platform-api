package org.paasta.container.platform.api.clusters.namespaces;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.workloads.replicaSets.ReplicaSets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.Map;

/**
 * Namespaces Service 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.14
 */
@Service
public class NamespacesService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Namespace service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public NamespacesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * Namespaces 상세정보를 조회한다.
     *
     * @param namespace the namespaces
     * @return Namespaces the namespaces
     */
    Namespaces getNamespaces(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespaceGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (Namespaces) commonService.setResultModel(commonService.setResultObject(resultMap, Namespaces.class), Constants.RESULT_STATUS_SUCCESS);
    }




    /**
     * NameSpace를 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the namespaces list
     */
    public NamespacesList getNamespacesList(String namespace, int limit, String continueToken) {
        String param = "";

        if(continueToken != null){
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListNamespaceGetUrl()
                        .replace("{namespace}", namespace) + "?limit" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (NamespacesList) commonService.setResultModel(commonService.setResultObject(responseMap, NamespacesList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * NameSpace YAML을 조회한다.
     *
     * @param namespace   the namespace
     * @return the NameSpace yaml
     */
    public Namespaces getNamespacesYaml(String namespace) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespaceGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, String.class , Constants.ACCEPT_TYPE_YAML);

        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("sourceTypeYaml", resultString);

        return (Namespaces) commonService.setResultModel(commonService.setResultObject(resultMap, Namespaces.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * NameSpace를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return return is succeeded
     */
    public Object createNamespaces(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespaceCreateUrl()
                        .replace("{namespace}",namespace), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
    }

    /**
     * NameSpace를 삭제한다.
     *
     * @param namespace        the namespace
     * @return the ResultStatus
     */
    public ResultStatus deleteNamepspaces(String namespace) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespaceDeleteUrl()
                        .replace("{namespace}", namespace), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus,ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
    }

    /**
     * NameSpace를 수정한다.
     *
     * @param namespace the namespace
     * @param yaml          the yaml
     * @return the services
     */
    public ResultStatus updateNamespaces(String namespace, String yaml) {
        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespaceUpdateUrl()
                        .replace("{namespace}", namespace), HttpMethod.PUT, yaml, ResultStatus.class);
        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);

    }
}
