package org.paasta.container.platform.api.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.paasta.container.platform.api.common.Constants;

import java.util.List;

/**
 * User List Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.10.15
 **/
@Data
public class UsersListInNamespaceAdmin {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;

    private List<UserDetail> items;

    @Data
    public static class UserDetail {
        private String isAdmin;
        private String userId;
        private String serviceAccountName;
        private String created;

        private String userType;

        public String getIsAdmin() {

            if (userType.equals(Constants.AUTH_NAMESPACE_ADMIN)) {
                isAdmin = "Y";
            } else {
                isAdmin = "N";
            }

            return isAdmin;
        }

        public void setIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
        }

    }


}

