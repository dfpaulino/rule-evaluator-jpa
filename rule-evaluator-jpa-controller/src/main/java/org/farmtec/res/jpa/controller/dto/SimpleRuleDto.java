package org.farmtec.res.jpa.controller.dto;

import lombok.Builder;
import lombok.Getter;
import org.farmtec.res.service.model.Action;

import java.util.List;

/**
 * Created by dp on 01/03/2021
 */
@Getter
@Builder
public class SimpleRuleDto {
    long id;
    String name;
    int priority;
    List<Action> actions;
}
