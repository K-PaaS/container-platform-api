package org.paasta.container.platform.api.common;

/**
 * Message Constants 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.11.06
 */
public class MessageConstant {

    public static final String NOT_ALLOWED_POD_NAME = "부적절한 Pod 이름 입니다.";
    public static final String NOT_ALLOWED_RESOURCE_NAME = "부적절한 리소스 이름 입니다.";
    public static final String PREFIX_KUBE_NOT_ALLOW = "'kube-' 접두사는 허용되지 않습니다.";
    public static final String NOT_MATCH_NAMESPACES = "현재 네임스페이스와 요청한 네임스페이스가 일치하지 않습니다.";
    public static final String NOT_MATCH_USER_ID = "현재 User ID와 요청한 User ID가 일치하지 않습니다.";
    public static final String NOT_EXIST_RESOURCE = "해당 리소스가 존재하지 않습니다.";
    public static final String NOT_EXIST = " 리소스 kind 가 존재하지 않습니다.";
    public static final String NOT_UPDATE_YAML = "에 대한 수정 yaml 형식이 아닙니다.";
    public static final String RESOURCE_NAMED = "이름을 가진 리소스";
    public static final String INCLUDE_INACCESSIBLE_RESOURCES = "User 권한으로 생성이 불가한 리소스가 포함되어있습니다.";
    public static final String INVALID_YAML_FORMAT = "잘못된 YAML 형식입니다.";
    public static final String DO_NOT_DELETE_DEFAULT_RESOURCES = "해당 리소스는 삭제할 수 없습니다.";
    public static final String MANDATORY_NAMESPACE_AND_ROLE = "Namespace와 Role 선택은 필수입니다.";
    public static final String UNAPPROACHABLE_USERS = "지정이 불가한 사용자입니다.";
    public static final String REQUIRES_NAMESPACE_ADMINISTRATOR_ASSIGNMENT = "Namespace 관리자 지정이 필요합니다.";
    public static final String NAMESPACES_CANNOT_BE_CREATED = "리소스 생성이 불가한 Namespace 입니다.";
    public static final String NAMESPACES_CANNOT_BE_MODIFIED = "리소스 수정이 불가한 Namespace 입니다.";

    public static final String CODE_ERROR = "요청 사항에 오류가 발생하였습니다. 관리자에게 문의하세요.";
    public static final String REQUEST_VALUE_IS_MISSING = "필수 사항이 누락되었습니다.";

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
    public static final String INVALID_LOGIN_INFO = "로그인 정보가 올바르지 않습니다.";


    public static final String LOGIN_TOKEN_FAIL ="TOKEN_FAILED";
    public static final String LOGIN_TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String LOGIN_INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    public static final String LOGIN_TOKEN_FAIL_MESSAGE = "Token authentication failed";
    public static final String LOGIN_TOKEN_EXPIRED_MESSAGE = "Access Token has Expired";
    public static final String LOGIN_INVALID_CREDENTIALS_MESSAGE = "Invalid Credentials";




    // paging
    public static final String LIMIT_ILLEGALARGUMENT = "limit(한 페이지에 가져올 리소스 최대 수) 는 반드시 0 이상이여아 합니다. limit >=0 ";
    public static final String OFFSET_ILLEGALARGUMENT = "offset(목록 시작지점) 은 반드시 0 이상이여아 합니다. offset >=0 ";
    public static final String OFFSET_REQUIRES_LIMIT_ILLEGALARGUMENT = "offset(목록 시작지점) 사용 시 limit(한 페이지에 가져올 리소스 최대 수) 값이 필요합니다.";


    // searching
    public static final String USER_TYPE_ILLEGALARGUMENT = "사용자 유형 선택 목록에 없는 항목입니다.";

    // register
    public static final String DUPLICATE_USER_ID = "User ID가 중복입니다.";
    public static final String RE_CONFIRM_INPUT_VALUE = "입력 값을 다시 확인해 주세요.";
    public static final String REGISTER_FAIL = "회원가입에 실패했습니다.";

}
