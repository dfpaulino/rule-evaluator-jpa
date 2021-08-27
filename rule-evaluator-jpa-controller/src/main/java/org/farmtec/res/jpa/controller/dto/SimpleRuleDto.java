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
    private long id;
    private String name;
    private int priority;
    private String filter;
    private List<Action> actions;
}
