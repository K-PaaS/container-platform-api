package org.paasta.container.platform.api.users;

import lombok.Data;

/**
 * User Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/

@Data
public class Users {
    private String resultCode;
    private String resultMessage;

    private long id;
    private String userId;
    private String password;
    private String email;
    private String clusterName;
    private String clusterApiUrl;
    private String clusterServiceAccountName;
    private String clusterToken;
    private String cpNamespace;
    private String cpAccountTokenName;
    private String serviceAccountName;
    private String saSecret;
    private String saToken;
    private String isActive;
    private String roleSetCode;
    private String description;
    private String userType;
    private String created;
    private String lastModified;

}
