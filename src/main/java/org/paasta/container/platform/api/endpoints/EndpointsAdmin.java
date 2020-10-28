package org.paasta.container.platform.api.endpoints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.paasta.container.platform.api.common.model.*;

import java.util.List;

/**
 * Endpoints Admin Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Data
public class EndpointsAdmin {

    private String resultCode;
    private String resultMessage;
    private Integer httpStatusCode;
    private String detailMessage;


    //private CommonMetaData metadata;
    //private CommonSpec spec;
    //private CommonStatus status;
    private List<CommonSubset> subsets;
    

}
