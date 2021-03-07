package org.farmtec.res.jpa.controller.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by dp on 01/03/2021
 */
@Getter
@Builder
public class SimpleRuleDto {
    long id;
    String name;
    int priority;
}
