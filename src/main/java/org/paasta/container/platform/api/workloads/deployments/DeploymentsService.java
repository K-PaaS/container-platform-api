package org.paasta.container.platform.api.workloads.deployments;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Deployments Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.08
 */
@Service
public class DeploymentsService {
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new deployments service.
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public DeploymentsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * Deployments 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the deployments list
     */
    public DeploymentsList getDeploymentsList(String namespace, int limit, String continueToken) {
        String param = "";

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListDeploymentsList()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (DeploymentsList) commonService.setResultModel(commonService.setResultObject(responseMap, DeploymentsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Deployments 상세 정보를 조회한다.
     *
     * @param namespace       the namespace
     * @param deploymentName the deployments name
     * @return the deployments
     */
    public Deployments getDeployments(String namespace, String deploymentName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListDeploymentsGet()
                        .replace("{namespace}", namespace)
                        .replace("{name}", deploymentName)
                , HttpMethod.GET, null, Map.class);


        return (Deployments) commonService.setResultModel(commonService.setResultObject(responseMap, Deployments.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Deployments YAML을 조회한다.
     *
     * @param namespace       the namespace
     * @param deploymentName the deployments name
     * @param resultMap       the result map
     * @return the deployments yaml
     */
    public Deployments getDeploymentsYaml(String namespace, String deploymentName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListDeploymentsGet()
                        .replace("{namespace}", namespace)
                        .replace("{name}", deploymentName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (Deployments) commonService.setResultModel(commonService.setResultObject(resultMap, Deployments.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Deployments 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return return is succeeded
     */
    public Object createDeployments(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListDeploymentsCreate()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_DEPLOYMENTS);
    }

    /**
     * Deployments 를 삭제한다.
     *
     * @param namespace        the namespace
     * @param name             the deployments name
     * @return the ResultStatus
     */
    public ResultStatus deleteDeployments(String namespace, String name) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListDeploymentsDelete()
                        .replace("{namespace}", namespace).replace("{name}", name), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_DEPLOYMENTS);
    }


    /**
     * Deployments 를 수정한다.
     *
     * @param namespace     the namespace
     * @param name          the deployments name
     * @param yaml          the yaml
     * @return the deployments
     */
    public ResultStatus updateDeployments(String namespace, String name, String yaml) {
        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListDeploymentsUpdate()
                        .replace("{namespace}", namespace).replace("{name}", name), HttpMethod.PUT, yaml, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_DEPLOYMENTS_DETAIL.replace("{deploymentName:.+}", name));
    }

}
