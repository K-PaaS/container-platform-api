package org.paasta.container.platform.api.common.model;

import lombok.Data;

/**
 * Common CommonOwnerReferences Model 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.08.25
 */
@Data
class CommonOwnerReferences {
    private String name;
    private boolean controller;
}