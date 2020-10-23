package org.paasta.container.platform.api.customServices;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CustomServices Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.10
 */
@Service
public class CustomServicesService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new CustomServices service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public CustomServicesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * Services 목록 조회(Get Services list)
     *
     * @param namespace the namespace
     * @return the services list
     */
    public CustomServicesList getCustomServicesList(String namespace, int limit, String continueToken) {
        String param = "";

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (CustomServicesList) commonService.setResultModel(commonService.setResultObject(responseMap, CustomServicesList.class), Constants.RESULT_STATUS_SUCCESS);
    }



    /**
     * Services 상세 조회(Get Services detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the services detail
     */
    public CustomServices getCustomServices(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (CustomServices) commonService.setResultModel(commonService.setResultObject(responseMap, CustomServices.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Services YAML 조회(Get Services yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return the services yaml
     */
    public CustomServices getCustomServicesYaml(String namespace, String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (CustomServices) commonService.setResultModel(commonService.setResultObject(resultMap, CustomServices.class), Constants.RESULT_STATUS_SUCCESS);
    }



    /**
     * Services 생성(Create Services)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object createServices(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return  commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES);
    }




    /**
     * Services 삭제(Delete Services)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return return is succeeded
     */
    public ResultStatus deleteServices(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES);
    }



    /**
     * Services 수정(Update Services)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object updateServices(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, CustomServices.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES_DETAIL.replace("{serviceName:.+}", resourceName));
    }


    /**
     * Services 목록 조회 페이징 테스트 (Get Services list paging test)
     *
     * @param namespace the namespace
     * @return the services list
     */
    public CustomServicesList getCustomServicesListTest(String namespace, int limit, int offset, String searchParam) {

        List<CustomServices> itemList = new ArrayList<>();
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        //  JsonPath filter processing if there is a search keyword
        if (searchParam != null) {
            responseMap = commonService.searchKeywordForResourceName(responseMap, searchParam);
        }

        CustomServicesList customServicesList = commonService.setResultObject(responseMap, CustomServicesList.class);

        // Sort by resource name
        itemList = customServicesList.getItems().stream().sorted(Comparator.comparing(x -> x.getMetadata().getName())).collect(Collectors.toList());
        //Truncate the list if there is a limit value
        if(limit > 0) {
            customServicesList.setItems(commonService.listProcessingforLimit(itemList, offset, limit));
        }
        return (CustomServicesList) commonService.setResultModel(customServicesList, Constants.RESULT_STATUS_SUCCESS);
    }


    //methods for administrators

    /**
     * Services Admin 목록 조회(Get Services Admin list)
     *
     * @param namespace the namespace
     * @return the services admin list
     */
    public Object getCustomServicesListAdmin(String namespace, int limit, String continueToken) {
        String param = "";
        HashMap responseMap = null;

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

         Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        try{
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, CustomServicesListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Services Admin 상세 조회(Get Services Admin detail)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the services admin
     */
    public Object getCustomServicesAdmin(String namespace, String resourceName) {

        HashMap responseMap = null;

        Object response  = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        try{
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, CustomServicesAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


}
