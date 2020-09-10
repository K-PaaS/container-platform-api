package org.paasta.container.platform.api.workloads.pods;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Pods List Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.09
 */
@Data
public class PodsList {
    private String resultCode;

    private List<Pods> items = new ArrayList<>();
}
