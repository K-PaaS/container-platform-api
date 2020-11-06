package org.paasta.container.platform.api.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * User Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.15
 **/

@Data
public class UsersAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private List<UsersDetails> UsersDetail;

    // Details
    @Data
    public static class UsersDetails {
        public String userId;
        public String serviceAccountName;
        public String created;

        @JsonIgnore
        public String saSecret;
        private String serviceAccountUid;
        public String cpNamespace;
        public String roleSetCode;

        public Secrets secrets;

        // Cluster Info
        public String clusterApiUrl;
        public String clusterServiceAccountName;
        public String clusterToken;

    }

    // Secrets
    @Data
    @Builder
    public static class Secrets {
        private String saSecret;
        private Object secretLabels;
        private String secretType;
    }


}
