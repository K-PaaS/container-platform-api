package org.paasta.container.platform.api.login.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class loginMetaDataItem {
    private String namespace;
    private String userType;
}
