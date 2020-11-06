package org.paasta.container.platform.api.roles;


import lombok.Data;
import org.paasta.container.platform.api.common.model.CommonMetaData;

import java.util.ArrayList;


/**
 * Roles Model 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.13
 */
@Data
public class RolesYaml {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;
    private String sourceTypeYaml;

}
