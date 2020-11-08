package org.paasta.container.platform.api.common;

/**
 * Message Constants 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.11.06
 */
public class MessageConstant {

    public static final String PREFIX_KUBE_NOT_ALLOW = "'kube-' 접두사는 허용되지 않습니다.";
    public static final String NOT_MATCH_NAMESPACES = "현재 네임스페이스와 요청한 네임스페이스가 일치하지 않습니다.";

    //login
    public static final String LOGIN_SUCCESS = "Login Successful.";
    public static final String LOGIN_FAIL = "Login Failed.";
    public static final String NON_EXISTENT_ID = "존재하지 않는 사용자 아이디입니다.";
    public static final String UNAVAILABLE_ID = "해당 사용자 아이디는 사용할 수 없습니다.";
    public static final String INVALID_PASSWORD = "비밀번호가 올바르지 않습니다.";
    public static final String ID_REQUIRED = "사용자 아이디를 입력해주세요.";
    public static final String PASSWORD_REQUIRED = "비밀번호를 입력해주세요.";
    public static final String ID_PASSWORD_REQUIRED = "사용자 아이디와 비밀번호를 입력해주세요.";
    public static final String INACTIVE_USER_ACCESS = "승인되지 않은 사용자입니다. 관리자에게 문의하시기 바랍니다.";


    // paging
    public static final String LIMIT_ILLEGALARGUMENT = "limit(한 페이지에 가져올 리소스 최대 수) 는 반드시 0 이상이여아 합니다. limit >=0 ";
    public static final String OFFSET_ILLEGALARGUMENT = "offset(목록 시작지점) 은 반드시 0 이상이여아 합니다. offset >=0 ";
    public static final String OFFSET_REQUIRES_LIMIT_ILLEGALARGUMENT = "offset(목록 시작지점) 사용 시 limit(한 페이지에 가져올 리소스 최대 수) 값이 필요합니다.";


}