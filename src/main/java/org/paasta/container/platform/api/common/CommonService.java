package org.paasta.container.platform.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Common Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.26
 */
@Service
public class CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
    private final Gson gson;

    /**
     * Instantiates a new Common service
     *
     * @param gson the gson
     */
    @Autowired
    public CommonService(Gson gson) {
        this.gson = gson;
    }


    /**
     * result model 설정(Sets result model)
     *
     * @param reqObject  the req object
     * @param resultCode the result code
     * @return the result model
     */
    public Object setResultModel(Object reqObject, String resultCode) {
        try {
            Class<?> aClass = reqObject.getClass();
            ObjectMapper oMapper = new ObjectMapper();
            Map map = oMapper.convertValue(reqObject, Map.class);

            Method methodSetResultCode = aClass.getMethod("setResultCode", String.class);
            Method methodSetResultMessage = aClass.getMethod("setResultMessage", String.class);
            Method methodSetHttpStatusCode = aClass.getMethod("setHttpStatusCode", Integer.class);
            Method methodSetDetailMessage = aClass.getMethod("setDetailMessage", String.class);

            if (Constants.RESULT_STATUS_FAIL.equals(map.get("resultCode"))) {
                methodSetResultCode.invoke(reqObject, map.get("resultCode"));
            } else {
                methodSetResultCode.invoke(reqObject, resultCode);
                methodSetResultMessage.invoke(reqObject, CommonStatusCode.OK.getMsg());
                methodSetHttpStatusCode.invoke(reqObject, CommonStatusCode.OK.getCode());
                methodSetDetailMessage.invoke(reqObject, CommonStatusCode.OK.getMsg());
            }

        } catch (NoSuchMethodException e) {
            LOGGER.error("NoSuchMethodException :: {}", e);
        } catch (IllegalAccessException e1) {
            LOGGER.error("IllegalAccessException :: {}", e1);
        } catch (InvocationTargetException e2) {
            LOGGER.error("InvocationTargetException :: {}", e2);
        }

        return reqObject;
    }

    /**
     * 생성/수정/삭제 후 페이지 이동을 위한 result model 설정(Set result model for moving the page after a create/update/delete)
     *
     * @param reqObject
     * @param resultCode
     * @param nextActionUrl
     * @return
     */
    public Object setResultModelWithNextUrl(Object reqObject, String resultCode, String nextActionUrl) {
        try {
            Class<?> aClass = reqObject.getClass();
            ObjectMapper oMapper = new ObjectMapper();
            Map map = oMapper.convertValue(reqObject, Map.class);

            Method methodSetResultCode = aClass.getMethod("setResultCode", String.class);
            Method methodSetNextActionUrl = aClass.getMethod("setNextActionUrl", String.class);

            if (Constants.RESULT_STATUS_FAIL.equals(map.get("resultCode"))) {
                methodSetResultCode.invoke(reqObject, map.get("resultCode"));
            } else {
                methodSetResultCode.invoke(reqObject, resultCode);
            }

            if (nextActionUrl != null) {
                methodSetNextActionUrl.invoke(reqObject, nextActionUrl);
            }

        } catch (NoSuchMethodException e) {
            LOGGER.error("NoSuchMethodException :: {}", e);
        } catch (IllegalAccessException e1) {
            LOGGER.error("IllegalAccessException :: {}", e1);
        } catch (InvocationTargetException e2) {
            LOGGER.error("InvocationTargetException :: {}", e2);
        }

        return reqObject;
    }

    /**
     * result object 설정(Set result object)
     *
     * @param <T>           the type parameter
     * @param requestObject the request object
     * @param requestClass  the request class
     * @return the result object
     */
    public <T> T setResultObject(Object requestObject, Class<T> requestClass) {
        return this.fromJson(this.toJson(requestObject), requestClass);
    }


    /**
     * json string 으로 변환(To json string)
     *
     * @param requestObject the request object
     * @return the string
     */
    private String toJson(Object requestObject) {
        return gson.toJson(requestObject);
    }


    /**
     * json 에서 t로 변환(From json t)
     *
     * @param <T>           the type parameter
     * @param requestString the request string
     * @param requestClass  the request class
     * @return the t
     */
    private <T> T fromJson(String requestString, Class<T> requestClass) {
        return gson.fromJson(requestString, requestClass);
    }

    /**
     * 서로 다른 객체를 매핑 (mapping each other objects)
     *
     * @param instance
     * @param targetClass
     * @param <A>
     * @param <B>
     * @return the b
     * @throws Exception
     */
    public static <A, B> B convert(A instance, Class<B> targetClass) throws Exception {
        B target = targetClass.newInstance();

        for (Field targetField : targetClass.getDeclaredFields()) {
            Field[] instanceFields = instance.getClass().getDeclaredFields();

            for (Field instanceField : instanceFields) {
                if (targetField.getName().equals(instanceField.getName())) {
                    targetField.set(target, instance.getClass().getDeclaredField(targetField.getName()).get(instance));
                }
            }
        }
        return target;
    }


    /**
     * 필드를 조회하고, 그 값을 반환 처리(check the field and return the result)
     *
     * @param fieldName
     * @param obj
     * @return the t
     */
    @SneakyThrows
    public <T> T getField(String fieldName, Object obj) {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object result = field.get(obj);
        field.setAccessible(false);
        return (T) result;
    }

    /**
     * 필드를 조회하고, 그 값을 저장 처리(check the field and save the result)
     *
     * @param fieldName
     * @param obj
     * @param value
     * @return the object
     */
    @SneakyThrows
    public Object setField(String fieldName, Object obj, Object value) {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
        field.setAccessible(false);
        return obj;
    }


    /**
     * 리소스 명 기준, 키워드가 포함된 리스트 반환 처리(return the list including keywords)
     *
     * @param commonList
     * @param keyword
     * @return the list
     */
    public <T> List<T> searchKeywordForResourceName(List<T> commonList, String keyword) {
        List filterList = commonList.stream()
                .filter(x -> this.<String>getField(Constants.RESOURCE_NAME,
                        getField(Constants.RESOURCE_METADATA, x)).matches("(?i).*" + keyword + ".*"))
                .collect(Collectors.toList());

        return filterList;
    }


    /**
     * 리소스 생성날짜 또는 이름으로 리스트 정렬 처리(order by creation time or name)
     *
     * @param commonList
     * @param orderBy
     * @param order
     * @return the list
     */
    public <T> List<T> sortingListByCondition(List<T> commonList, String orderBy, String order) {

        List sortList = null;

        orderBy = orderBy.toLowerCase();
        order = order.toLowerCase();

        if (orderBy.equals(Constants.RESOURCE_NAME)) {
            //리소스명 기준
            order = (order.equals("")) ? "asc" : order;
            if (order.equals("asc")) {
                sortList = commonList.stream().sorted(Comparator.comparing(x -> this.<String>getField(Constants.RESOURCE_NAME,
                        getField(Constants.RESOURCE_METADATA, x)))).collect(Collectors.toList());
            } else {
                sortList = commonList.stream().sorted(Comparator.comparing(x -> this.<String>getField(Constants.RESOURCE_NAME,
                        getField(Constants.RESOURCE_METADATA, x))).reversed()).collect(Collectors.toList());
            }
        } else {
            // 생성날짜 기준
            order = (order.equals("")) ? "desc" : order;

            if (order.equals("asc")) {

                sortList = commonList.stream().sorted(Comparator.comparing(x -> this.<String>getField(Constants.RESOURCE_CREATIONTIMESTAMP,
                        getField(Constants.RESOURCE_METADATA, x)))).collect(Collectors.toList());
            } else {
                sortList = commonList.stream().sorted(Comparator.comparing(x -> this.<String>getField(Constants.RESOURCE_CREATIONTIMESTAMP,
                        getField(Constants.RESOURCE_METADATA, x))).reversed()).collect(Collectors.toList());
            }
        }

        return sortList;
    }


    /**
     * offset & limit을 통한 리스트 가공 처리(sublist using offset and limit)
     *
     * @param itemList
     * @param offset
     * @param limit
     * @return the list
     */
    public <T> List<T> subListforLimit(List<T> itemList, int offset, int limit) {
        List returnList = itemList;

        if (limit > 0) {
            returnList = itemList.stream().skip(offset * limit).limit(limit).collect(Collectors.toList());
        }
        return returnList;
    }

    /**
     * commonItemMetaData 객체 생성(create a commonItemMetaData object)
     *
     * @param itemList
     * @param offset
     * @param limit
     * @return the CommonItemMetaData
     */
    public CommonItemMetaData setCommonItemMetaData(List itemList, int offset, int limit) {
        CommonItemMetaData commonItemMetaData = new CommonItemMetaData(0, 0);


        if (limit < 0) {
            throw new IllegalArgumentException(Constants.LIMIT_ILLEGALARGUMENT);
        }
        if (offset < 0) {
            throw new IllegalArgumentException(Constants.OFFSET_ILLEGALARGUMENT);
        }

        if (offset > 0 && limit == 0) {
            throw new IllegalArgumentException(Constants.OFFSET_REQUIRES_LIMIT_ILLEGALARGUMENT);
        }


        int allItemCount = itemList.size();
        int remainingItemCount = allItemCount - ((offset + 1) * limit);

        if (limit == 0 || remainingItemCount < 0) {
            remainingItemCount = 0;
        }

        commonItemMetaData.setAllItemCount(allItemCount);
        commonItemMetaData.setRemainingItemCount(remainingItemCount);

        return commonItemMetaData;
    }

    public <T> T resourceListProcessing(Object resourceList, int offset, int limit, String orderBy, String order, String searchName, Class<T> requestClass) {

        Object resourceReturnList = null;

        List resourceItemList = getField("items", resourceList);

        if (searchName != null) {
            searchName = searchName.trim();
        }

        // 1. 키워드 match에 따른 리스트 필터
        if (searchName != null || searchName.length() > 0) {
            resourceItemList = searchKeywordForResourceName(resourceItemList, searchName);
        }

        // 2. 조건에 따른 리스트 정렬
        resourceItemList = sortingListByCondition(resourceItemList, orderBy, order);

        // 3. commonItemMetaData 추가
        CommonItemMetaData commonItemMetaData = setCommonItemMetaData(resourceItemList, offset, limit);
        resourceReturnList = setField("itemMetaData", resourceList, commonItemMetaData);


        // 4. offset, limit에 따른 리스트 subLIst
        resourceItemList = subListforLimit(resourceItemList, offset, limit);
        resourceReturnList = setField("items", resourceReturnList, resourceItemList);

        return (T) resourceReturnList;
    }


    /**
     * selector에 의한 리스트 조회 commonItemMetaData 설정(config commonItemMetaData)
     *
     * @param resourceList
     * @return the t
     */
    public <T> T setCommonItemMetaDataBySelector(Object resourceList, Class<T> requestClass) {

        Object resourceReturnList = null;
        List resourceItemList = getField("items", resourceList);
        CommonItemMetaData commonItemMetaData = new CommonItemMetaData(resourceItemList.size(), 0);
        resourceReturnList = setField("itemMetaData", resourceList, commonItemMetaData);

        return (T) resourceReturnList;
    }

}