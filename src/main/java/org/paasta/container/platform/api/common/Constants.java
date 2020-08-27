package org.paasta.container.platform.api.common;

import org.springframework.http.MediaType;

/**
 * Constants 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.26
 */
public class Constants {

    public static final String RESULT_STATUS_SUCCESS = "SUCCESS";
    public static final String RESULT_STATUS_FAIL = "FAIL";

    public static final String TARGET_CP_MASTER_API = "cpMasterApi";
    public static final String ACCEPT_TYPE_YAML = String.valueOf(MediaType.valueOf("application/yaml"));

    public static final String TOKEN_KEY = "cp_admin";

    public static final String TARGET_COMMON_API = "commonApi";

    public static final String URI_COMMON_API_ADMIN_TOKEN_DETAIL = "/adminToken/{tokenName:.+}";

    static final String STRING_DATE_TYPE = "yyyy-MM-dd HH:mm:ss";
    static final String STRING_ORIGINAL_DATE_TYPE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    static final String STRING_TIME_ZONE_ID = "Asia/Seoul";

    static final String ACCEPT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;


    public Constants() {
        throw new IllegalStateException();
    }

}
