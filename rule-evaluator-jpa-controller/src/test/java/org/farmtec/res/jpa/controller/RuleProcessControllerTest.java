package org.farmtec.res.jpa.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.farmtec.res.enums.LogicalOperation;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.rules.impl.ImmutableRuleGroupComposite;
import org.farmtec.res.rules.impl.RuleGroupComposite;
import org.farmtec.res.service.RuleService;
import org.farmtec.res.service.model.Action;
import org.farmtec.res.service.model.ImmutableRule;
import org.farmtec.res.service.model.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;

/**
 * Created by dp on 11/03/2021
 */
@ExtendWith(MockitoExtension.class)
class RuleProcessControllerTest {
    private MockMvc mockMvc;
    @Mock
    private RuleService ruleService;
    private Rule rule1,rule2,rule3;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RuleProcessController(ruleService))
                .setControllerAdvice(DefaultHandlerExceptionResolver.class)
                .build();

        setRules();
    }

    @Test
    void process_noRules() throws Exception{
        //given
        when(ruleService.test(any(JsonNode.class))).thenReturn(Collections.emptyList());
        String content = "{\n" +
                "\"liquid\":\"water\",\n" +
                "\"amount\":2000000\n" +
                "}";
        mockMvc.perform(post("/rule/process").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void process_match1Rule() throws Exception{
        //given
        when(ruleService.test(any(JsonNode.class))).thenReturn(Arrays.asList(rule1));
        String content = "{\n" +
                "\"liquid\":\"water\",\n" +
                "\"amount\":2000000\n" +
                "}";
        mockMvc.perform(post("/rule/process").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name",is("Rule1")))
                .andExpect(jsonPath("$.[0].actions").isArray())
                .andExpect(jsonPath("$.[0].actions").isEmpty());
    }

    @Test
    void process_match3Rule() throws Exception{
        //given
        when(ruleService.test(any(JsonNode.class))).thenReturn(Arrays.asList(rule1,rule2,rule3));
        String content = "{\n" +
                "\"liquid\":\"water\",\n" +
                "\"amount\":2000000\n" +
                "}";
        mockMvc.perform(post("/rule/process").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$.[0].name",containsString("Rule")))
                .andExpect(jsonPath("$.[0].actions").isArray())
                .andExpect(jsonPath("$.[0].actions").isEmpty())
                .andExpect(jsonPath("$.[1].name",containsString("Rule")))
                .andExpect(jsonPath("$.[1].actions").isArray())
                .andExpect(jsonPath("$.[1].actions").isEmpty())
                .andExpect(jsonPath("$.[2].name",containsString("Rule")))
                .andExpect(jsonPath("$.[2].actions").isArray())
                .andExpect(jsonPath("$.[2].actions").isEmpty());
    }

    private void setRules() {

        RuleGroupComposite ruleGroupComposite = ImmutableRuleGroupComposite.builder()
                .logicalOperation(LogicalOperation.AND).build();
        rule1 = ImmutableRule.builder().name("Rule1")
                .actions(Collections.<Action>emptyList())
                .priority(1)
                .ruleGroupComposite(ruleGroupComposite)
                .build();
        rule2 = ImmutableRule.builder().name("Rule2")
                .actions(Collections.<Action>emptyList())
                .ruleGroupComposite(ruleGroupComposite)
                .priority(2).build();
        rule3 = ImmutableRule.builder().name("Rule3")
                .actions(Collections.<Action>emptyList())
                .ruleGroupComposite(ruleGroupComposite)
                .priority(3).build();

    }

}