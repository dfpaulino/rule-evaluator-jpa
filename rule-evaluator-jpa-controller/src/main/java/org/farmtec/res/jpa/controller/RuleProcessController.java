package org.farmtec.res.jpa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.jpa.controller.dto.SimpleRuleDto;
import org.farmtec.res.service.RuleService;
import org.farmtec.res.service.model.Rule;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dp on 09/03/2021
 */
@RestController
@RequestMapping("/rule/process")
public class RuleProcessController {

    private final RuleService ruleService;

    public RuleProcessController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public List<SimpleRuleDto> process(@RequestBody JsonNode jsonNode)  throws Exception{
        List<Rule> rules = ruleService.test(jsonNode);
        return rules.stream().map(rule ->
                SimpleRuleDto.builder()
                        .priority(rule.getPriority())
                        .name(rule.getName())
                        .actions(rule.getActions())
                        .build()
        ).collect(Collectors.toList());
    }
}
