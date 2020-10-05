package org.paasta.container.platform.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Common utils 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.26
 */
public class CommonUtils {

    /**
     * Timestamp Timezone 을 변경하여 재설정한다.
     *
     * @param requestTimestamp the request timestamp
     * @return the string
     */
    public static String procSetTimestamp(String requestTimestamp) {
        String resultString = "";

        if (null == requestTimestamp || "".equals(requestTimestamp)) {
            return resultString;
        }

        SimpleDateFormat simpleDateFormatForOrigin = new SimpleDateFormat(Constants.STRING_ORIGINAL_DATE_TYPE);
        SimpleDateFormat simpleDateFormatForSet = new SimpleDateFormat(Constants.STRING_DATE_TYPE);

        try {
            Date parseDate = simpleDateFormatForOrigin.parse(requestTimestamp);
            long parseDateTime = parseDate.getTime();
            int offset = TimeZone.getTimeZone(Constants.STRING_TIME_ZONE_ID).getOffset(parseDateTime);

            resultString = simpleDateFormatForSet.format(parseDateTime + offset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultString;
    }

    public static Object stringNullCheck(Object obj) {
        List<String> checkParamList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.convertValue(obj, Map.class);

        for(String key : map.keySet()) {
            if(!StringUtils.hasText(map.get(key))) {
                checkParamList.add(key);
            }
        }

        if(checkParamList.size() > 0) {
            return ResultStatus.builder()
                    .resultCode(Constants.RESULT_STATUS_FAIL)
                    .resultMessage("Failed Sign Up.")
                    .httpStatusCode(400)
                    .detailMessage("Failed Sign Up. Re Confirm " + checkParamList.toString()).build();
        }

        return objectMapper.convertValue(map, Users.class);
    }


    public static Map yamlMatch(String username, String namespace) {
        Map<String, Object> model = new HashMap<>();
        model.put("userName", username);
        model.put("spaceName", namespace);

        return model;
    }

}
