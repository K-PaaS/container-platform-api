package org.paasta.container.platform.api.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonItemMetaData;
import org.paasta.container.platform.api.common.model.CommonMetaData;

import java.util.List;
import java.util.Map;

/**
 * Roles List AllNamespaces Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.11.04
 **/
@Data
public class RolesListAllNamespaces {
    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private Map metadata;
    private CommonItemMetaData itemMetaData;
    private List<RolesListAllNamespacesItem> items;


    public class RolesListAllNamespacesItem {
        private String name;
        private String namespace;
        private String checkYn;
        private String userType;

        @JsonIgnore
        private CommonMetaData metadata;

        public String getName() {
            return metadata.getName();
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNamespace() {
            return metadata.getNamespace();
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public CommonMetaData getMetadata() {
            return metadata;
        }

        public void setMetadata(CommonMetaData metadata) {
            this.metadata = metadata;
        }

        public String getCheckYn() {
            return checkYn;
        }

        public void setCheckYn(String checkYn) {
            this.checkYn = checkYn;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}
