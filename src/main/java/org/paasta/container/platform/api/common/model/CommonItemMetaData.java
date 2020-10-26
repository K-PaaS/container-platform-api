package org.paasta.container.platform.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonItemMetaData {

    private Integer allItemCount;
    private Integer remainingItemCount;

}
