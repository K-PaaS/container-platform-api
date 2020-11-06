package org.paasta.container.platform.api.clusters.limitRanges;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.paasta.container.platform.api.clusters.limitRanges.support.LimitRangesItem;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.paasta.container.platform.api.common.Constants.CHECK_N;
import static org.paasta.container.platform.api.common.Constants.CHECK_Y;

/**
 * LimitRanges Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.22
 */
@Service
public class LimitRangesService {
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new LimitRanges service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public LimitRangesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * LimitRanges Admin 목록 조회(Get LimitRanges Admin list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the limitRanges admin list
     */
    public Object getLimitRangesListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesListUrl().replace("{namespace}", namespace),
                HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        LimitRangesListAdmin limitRangesListAdmin = commonService.setResultObject(responseMap, LimitRangesListAdmin.class);
        limitRangesListAdmin = commonService.resourceListProcessing(limitRangesListAdmin, offset, limit, orderBy, order, searchName, LimitRangesListAdmin.class);

        return commonService.setResultModel(limitRangesListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * 전체 Namespaces 의 LimitRanges Admin 목록 조회(Get LimitRanges Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the limitRanges admin list
     */
    public Object getLimitRangesListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        LimitRangesListAdmin limitRangesListAdmin = commonService.setResultObject(responseMap, LimitRangesListAdmin.class);
        limitRangesListAdmin = commonService.resourceListProcessing(limitRangesListAdmin, offset, limit, orderBy, order, searchName, LimitRangesListAdmin.class);

        return commonService.setResultModel(limitRangesListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * LimitRanges Admin 상세 조회(Get LimitRanges Admin detail)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the limitRanges admin detail
     */
    public Object getLimitRangesAdmin(String namespace, String resourceName) {

        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName),
                HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, LimitRangesAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * LimitRanges 생성(Create LimitRanges)
     *
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    public Object createLimitRanges(String namespace, String yaml) {

        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesCreateUrl().replace("{namespace}", namespace),
                HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_LIMITRANGES);
    }


    /**
     * LimitRanges 삭제(Delete LimitRanges)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return return is succeeded
     */
    public ResultStatus deleteLimitRanges(String namespace, String resourceName) {
        ResultStatus resultStatus = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesDeleteUrl().replace("{namespace}", namespace).replace("{name}", resourceName),
                HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_LIMITRANGES);
    }


    /**
     * LimitRanges 수정(Update LimitRanges)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    public ResultStatus updateLimitRanges(String namespace, String resourceName, String yaml) {
        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesUpdateUrl().replace("{namespace}", namespace).replace("{name}", resourceName),
                HttpMethod.PUT, yaml, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_LIMITRANGES_DETAIL.replace("{limitRangeName:.+}", resourceName));
    }


    /**
     * LimitRanges Template 목록 조회(Get LimitRanges Template list)
     *
     * @param namespace the namespace
     * @return the limitRanges template list
     */
    public Object getLimitRangesTemplateList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        LimitRangesListAdmin limitRangesList = (LimitRangesListAdmin) getLimitRangesListAdmin(namespace, 0, 0, "creationTime", "desc", "");
        LimitRangesDefaultList defaultList = restTemplateService.send(Constants.TARGET_COMMON_API, "/limitRanges", HttpMethod.GET, null, LimitRangesDefaultList.class);

        List<LimitRangesListAdminItem> adminItems = limitRangesList.getItems();
        List<LimitRangesTemplateItem> serversItemList = new ArrayList();

        LimitRangesTemplateList serverList = new LimitRangesTemplateList();

        List<String> k8sLrNameList = limitRangesList.getItems().stream().map(LimitRangesListAdminItem::getName).collect(Collectors.toList());
        List<String> dbLrNameList = defaultList.getItems().stream().map(LimitRangesDefault::getName).collect(Collectors.toList());

        for (LimitRangesDefault limitRangesDefault : defaultList.getItems()) {
            String yn = CHECK_N;

            if(k8sLrNameList.contains(limitRangesDefault.getName())) {
                yn = CHECK_Y;
            }
            serversItemList.add(getLimitRangesDb(limitRangesDefault, yn));
        }

        if (adminItems.size() > 0) {
            for (LimitRangesListAdminItem i : adminItems) {
                List<LimitRangesItem> list = new ArrayList<>();

                if(!dbLrNameList.contains(i.getName())) {
                    LimitRangesTemplateItem serversItem = new LimitRangesTemplateItem();

                    for (LimitRangesItem item : i.getSpec().getLimits()) {

                        List<String> duplicateKeys = new ArrayList<>();
                        List<String> exclusiveKeys = new ArrayList<>();
                        duplicateKeys.addAll(getKeyResource(objectToMap(item.getDefaultLimit())));
                        duplicateKeys.addAll(getKeyResource(objectToMap(item.getDefaultRequest())));
                        duplicateKeys.addAll(getKeyResource(objectToMap(item.getMin())));
                        duplicateKeys.addAll(getKeyResource(objectToMap(item.getMax())));

                        for(String name:duplicateKeys) {
                            if(!exclusiveKeys.contains(name)) {
                                exclusiveKeys.add(name);
                            }
                        }

                        String result = String.join(", ", exclusiveKeys);

                        item.setResource(result);
                        list.add(item);
                    }
                    serversItem.setName(i.getName());
                    serversItem.setLimits(list);
                    serversItem.setCheckYn(CHECK_Y);
                    serversItem.setMetadata(i.getMetadata());
                    serversItem.setSpec(i.getSpec());

                    serversItemList.add(serversItem);
                }
            }

            serverList.setItems(serversItemList);
            serverList = commonService.setResultObject(serverList, LimitRangesTemplateList.class);
            serverList = commonService.resourceListProcessing(serverList, offset, limit, orderBy, order, searchName, LimitRangesTemplateList.class);

            return commonService.setResultModel(serverList, Constants.RESULT_STATUS_SUCCESS);
        }

        serverList.setItems(serversItemList);
        serverList = commonService.setResultObject(serverList, LimitRangesTemplateList.class);
        serverList = commonService.resourceListProcessing(serverList, offset, limit, orderBy, order, searchName, LimitRangesTemplateList.class);

        return commonService.setResultModel(serverList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * LimitRanges DB Template 형식 맞춤(Set LimitRanges DB template)
     *
     * @param limitRangesDefault the limitRangesDefault
     * @return the limitRanges template item
     */
    public LimitRangesTemplateItem getLimitRangesDb(LimitRangesDefault limitRangesDefault, String yn) {
        LimitRangesItem map = new LimitRangesItem();
        List<LimitRangesItem> list = new ArrayList<>();
        LimitRangesTemplateItem item = new LimitRangesTemplateItem();
        CommonMetaData metadata = new CommonMetaData();

        map.setDefaultRequest(limitRangesDefault.getDefaultRequest());
        map.setMin(limitRangesDefault.getMin());
        map.setMax(limitRangesDefault.getMax());
        map.setType(limitRangesDefault.getType());
        map.setResource(limitRangesDefault.getResource());
        map.setDefaultLimit(limitRangesDefault.getDefaultLimit());

        list.add(map);

        metadata.setName(limitRangesDefault.getName());
        metadata.setCreationTimestamp(limitRangesDefault.getCreationTimestamp());

        item.setName(limitRangesDefault.getName());
        item.setLimits(list);
        item.setCheckYn(yn);
        item.setMetadata(metadata);

        return item;
    }


    /**
     * LimitRanges 각 요소 map으로 변환(Object convert to map)
     *
     * @param obj the object
     * @return the map
     */
    private Map<String, Object> objectToMap(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(obj, Map.class);
    }


    /**
     * LimitRanges Resource keys 추출(Extract LimitRanges's resource key)
     *
     * @param map the map
     * @return the list
     */
    private List<String> getKeyResource(Map<String, Object> map) {
        List<String> keyList = new ArrayList<>();
        if(map != null) {
            Iterator<String> keys = map.keySet().iterator();
            while(keys.hasNext()) {
                keyList.add(keys.next());
            }
        }

        return keyList;
    }
}
