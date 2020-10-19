package org.paasta.container.platform.api.users;

import lombok.Data;

import java.beans.Transient;
import java.util.List;
import java.util.Map;

/**
 * User Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/

@Data
public class Users {
    public String resultCode;
    public String resultMessage;
    public Integer httpStatusCode;
    public String detailMessage;

    public long id;
    public String userId;
    public String password;
    public String email;
    public String clusterName;
    public String clusterApiUrl;
    public String clusterServiceAccountName;
    public String clusterToken;
    public String cpNamespace;
    public String cpAccountTokenName;
    public String serviceAccountName;
    public String saSecret;
    public String saToken;
    public String isActive;
    public String roleSetCode;
    public String description;
    public String userType;
    public String created;
    public String lastModified;

    // user 생성 시 multi namespaces, roles
    private List<NamespaceRole> selectValues;

    @Data
    public static class NamespaceRole {
        private String namespace;
        private String role;
    }
}
