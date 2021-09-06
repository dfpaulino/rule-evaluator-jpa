package org.farmtec.res.jpa.parser.util;

import java.util.stream.Collectors;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.service.model.ImmutableAction;
import org.farmtec.res.service.rule.loader.dto.RuleDto;

import java.util.ArrayList;

import static org.farmtec.res.jpa.parser.RulesParserJpaImpl.GROUP_PREFIX;

/**
 * Created by dp on 07/02/2021
 */
public class RuleMapRuleDto {
    public static RuleDto RuleToRuleDto(Rule rule) {
        RuleDto ruleDto = new RuleDto();

        if(rule!=null) {
            ruleDto.setPriority(rule.getPriority());
            ruleDto.setActions(rule.getActions()!=null?rule.getActions().stream()
                .map(action -> ImmutableAction.builder()
                    .data(action.getData())
                    .type(action.getType())
                    .priority(action.getPriority())
                    .build())
                .collect(Collectors.toList()):new ArrayList<>());
            if(null!=rule.getFilter()) {
                ruleDto.setFilter(rule.getFilter());
            }
            ruleDto.setPredicateName(GROUP_PREFIX+rule.getGroupComposite().getId());
        }
        return ruleDto;
    }
}
