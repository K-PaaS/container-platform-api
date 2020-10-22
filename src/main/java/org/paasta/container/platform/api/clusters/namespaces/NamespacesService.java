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
     * Namespaces 상세 조회(Get Namespaces detail)
     *
     * @param namespace the namespaces
     * @return the namespaces
     */
    Namespaces getNamespaces(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (Namespaces) commonService.setResultModel(commonService.setResultObject(resultMap, Namespaces.class), Constants.RESULT_STATUS_SUCCESS);
    }




    /**
     * NameSpaces 목록 조회(Get NameSpaces list)
     *
     * @return the namespaces list
     */
    public NamespacesList getNamespacesList(int limit, String continueToken) {
        String param = "";

        if(continueToken != null){
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListNamespacesListUrl()
                , HttpMethod.GET, null, Map.class);

        return (NamespacesList) commonService.setResultModel(commonService.setResultObject(responseMap, NamespacesList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * NameSpaces YAML 조회(Get NameSpaces yaml)
     *
     * @param namespace the namespace
     * @return the namespaces yaml
     */
    public Namespaces getNamespacesYaml(String namespace) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, String.class , Constants.ACCEPT_TYPE_YAML);

        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("sourceTypeYaml", resultString);

        return (Namespaces) commonService.setResultModel(commonService.setResultObject(resultMap, Namespaces.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * NameSpace 생성(Create NameSpaces)
     *
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object createNamespaces(String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesCreateUrl(), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
    }

    /**
     * NameSpaces 삭제(Delete NameSpaces)
     *
     * @param namespace the namespace
     * @return the resultStatus
     */
    public ResultStatus deleteNamespaces(String namespace) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesDeleteUrl()
                        .replace("{name}", namespace), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus,ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
    }

    /**
     * NameSpaces 수정(Update NameSpaces)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    public ResultStatus updateNamespaces(String namespace, String yaml) {
        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesUpdateUrl()
                        .replace("{name}", namespace), HttpMethod.PUT, yaml, ResultStatus.class);
        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);

    }
}
