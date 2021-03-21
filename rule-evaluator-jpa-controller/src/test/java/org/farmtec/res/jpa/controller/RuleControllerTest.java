package org.farmtec.res.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.jpa.repositories.RulesRepository;
import org.farmtec.res.jpa.service.utils.RulesValidator;
import org.farmtec.res.jpa.service.utils.RulesValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dp on 01/03/2021
 */
@ExtendWith(SpringExtension.class)
class RuleControllerTest {

    private MockMvc mockMvc;
    private Rule rule1, rule2;
    private GroupComposite g1, g2, gGroup;
    private PredicateLeaf p1, p2, p3, p4;
    @Mock
    private RulesRepository rulesRepository;
    @Mock
    private RulesValidator rulesValidator;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RuleController(rulesRepository, new RulesValidatorImpl()))
                .setControllerAdvice(ControllerAdvice.class)
                .build();

        setPredicates();
        g1 = new GroupComposite();
        g1.setId(1L);
        g1.setCreateTime(new Date());
        g1.setUpdateTime(new Date());
        g1.setLogicalOperation("AND");
        g1.setPredicateLeaves(new HashSet<>(Set.of(p1, p2)));

        g2 = new GroupComposite();
        g2.setId(2L);
        g2.setCreateTime(new Date());
        g2.setUpdateTime(new Date());
        g2.setLogicalOperation("AND");
        g2.setPredicateLeaves(new HashSet<>(Set.of(p3, p4)));

        gGroup = new GroupComposite();
        gGroup.setId(3L);
        gGroup.setCreateTime(new Date());
        gGroup.setUpdateTime(new Date());
        gGroup.setLogicalOperation("OR");
        gGroup.setGroupComposites(new HashSet<>(Set.of(g1, g2)));

        rule1 = new Rule();
        rule1.setId(1L);
        rule1.setName("Rule_1");
        rule1.setPriority(1);
        rule1.setGroupComposite(gGroup);

        rule2 = new Rule();
        rule2.setId(2L);
        rule2.setName("Rule_2");
        rule2.setPriority(2);
        rule2.setGroupComposite(g2);

        when(rulesRepository.findAll()).thenReturn(new ArrayList<>(List.of(rule1,rule2)));
        when(rulesRepository.findById(1L)).thenReturn(Optional.of(rule1));

    }

    private void setPredicates() {
        p1 = new PredicateLeaf();
        p1.setId(1L);
        p1.setCreateTime(new Date());
        p1.setUpdateTime(new Date());
        p1.setOperation("EQ");
        p1.setTag("tag");
        p1.setType("integer");
        p1.setValue("123");

        p2 = new PredicateLeaf();
        p2.setId(2L);
        p2.setCreateTime(new Date());
        p2.setUpdateTime(new Date());
        p2.setOperation("CONTAINS");
        p2.setTag("tagStr");
        p2.setType("string");
        p2.setValue("spring");

        p3 = new PredicateLeaf();
        p3.setId(3L);
        p3.setCreateTime(new Date());
        p3.setUpdateTime(new Date());
        p3.setOperation("EQ");
        p3.setTag("tagStr2");
        p3.setType("string");
        p3.setValue("water");

        p4 = new PredicateLeaf();
        p4.setId(4L);
        p4.setCreateTime(new Date());
        p4.setUpdateTime(new Date());
        p4.setOperation("GTE");
        p4.setTag("tag2");
        p4.setType("long");
        p4.setValue("999999999999999");
    }

    @Test
    void getAllRules() throws Exception{
        mockMvc.perform(get("/rules"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").isNumber())
                .andExpect(jsonPath("$.content[0].name").isString())
                .andExpect(jsonPath("$.content[0].name",containsString("Rule")))
                .andExpect(jsonPath("$.content[0].priority").isNumber())
                .andExpect(jsonPath("$.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.content[0].links[0].href",
                        containsString("http://localhost/rules")))
                .andExpect(jsonPath("$.content[0].id").isNumber())
                .andExpect(jsonPath("$.content[1].name").isString())
                .andExpect(jsonPath("$.content[1].name",containsString("Rule")))
                .andExpect(jsonPath("$.content[1].priority").isNumber())
                .andExpect(jsonPath("$.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.content[1].links[0].href",
                        containsString("http://localhost/rules")));
    }

    @Test
    void getRuleById() throws Exception{
        mockMvc.perform(get("/rules/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name",containsString("Rule")))
                .andExpect(jsonPath("$.priority").isNumber())
                .andExpect(jsonPath("$.group.logicalOperation",is("OR")))
                .andExpect(jsonPath("$.group.groups.content",hasSize(2)))
                .andExpect(jsonPath("$.group.groups.content[0].logicalOperation",is("AND")))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content",hasSize(2)))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].id").isNumber())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].type").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].operation").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].tag").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].value").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].links[0].href",containsString("http://localhost/predicates/")))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].links[1].href",containsString("http://localhost/groups/")))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].id").isNumber())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].type").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].operation").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].tag").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].value").isString());

    }

    @Test
    void deleteRuleById()  throws Exception{
        mockMvc.perform(delete("/rules/1"))
                .andExpect(status().isOk())
                .andDo(print());
        verify(rulesRepository,times(1)).deleteById(1L);
    }
    @Test
    void addRule() throws Exception {
        //given
        when(rulesRepository.save(ArgumentMatchers.any(Rule.class))).thenReturn(rule1);
        ObjectMapper mapper = new ObjectMapper();
        String rule = mapper.writeValueAsString(rule1);
        System.out.println("["+rule+"]");
        String content = "{\n" +
                "   \"name\":\"Rule_1\",\n" +
                "   \"priority\":1,\n" +
                "   \"groupComposite\":{\n" +
                "      \"logicalOperation\":\"OR\",\n" +
                "      \"groupComposites\":[\n" +
                "         {\n" +
                "            \"logicalOperation\":\"AND\",\n" +
                "            \"predicateLeaves\":[\n" +
                "               {\n" +
                "                  \"type\":\"string\",\n" +
                "                  \"operation\":\"EQ\",\n" +
                "                  \"tag\":\"tagStr2\",\n" +
                "                  \"value\":\"water\"\n" +
                "               },\n" +
                "               {\n" +
                "                  \"type\":\"long\",\n" +
                "                  \"operation\":\"GTE\",\n" +
                "                  \"tag\":\"tag2\",\n" +
                "                  \"value\":\"999999999999999\"\n" +
                "               }\n" +
                "            ]\n" +
                "         },\n" +
                "         {\n" +
                "            \"logicalOperation\":\"AND\",\n" +
                "            \"predicateLeaves\":[\n" +
                "               {\n" +
                "                  \"type\":\"string\",\n" +
                "                  \"operation\":\"CONTAINS\",\n" +
                "                  \"tag\":\"tagStr\",\n" +
                "                  \"value\":\"spring\"\n" +
                "               },\n" +
                "               {\n" +
                "                  \"type\":\"integer\",\n" +
                "                  \"operation\":\"EQ\",\n" +
                "                  \"tag\":\"tag\",\n" +
                "                  \"value\":\"123\"\n" +
                "               }\n" +
                "            ]\n" +
                "         }\n" +
                "      ]\n" +
                "    }\n" +
                "}";

        mockMvc.perform(post("/rules").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name",containsString("Rule")))
                .andExpect(jsonPath("$.priority").isNumber())
                .andExpect(jsonPath("$.group.logicalOperation",is("OR")))
                .andExpect(jsonPath("$.group.groups.content",hasSize(2)))
                .andExpect(jsonPath("$.group.groups.content[0].logicalOperation",is("AND")))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content",hasSize(2)))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].id").isNumber())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].type").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].operation").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].tag").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].value").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].links[0].href",containsString("http://localhost/predicates/")))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[0].links[1].href",containsString("http://localhost/groups/")))
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].id").isNumber())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].type").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].operation").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].tag").isString())
                .andExpect(jsonPath("$.group.groups.content[0].predicates.content[1].value").isString());

        verify(rulesRepository,times(1)).save(ArgumentMatchers.any(Rule.class));
    }

    @Test
    void addRule_invalidType() throws Exception {
        //given
        when(rulesRepository.save(ArgumentMatchers.any(Rule.class))).thenReturn(rule1);
        ObjectMapper mapper = new ObjectMapper();
        String rule = mapper.writeValueAsString(rule1);
        System.out.println("["+rule+"]");
        String content = "{\n" +
                "   \"name\":\"Rule_1\",\n" +
                "   \"priority\":1,\n" +
                "   \"groupComposite\":{\n" +
                "      \"logicalOperation\":\"OR\",\n" +
                "      \"groupComposites\":[\n" +
                "         {\n" +
                "            \"logicalOperation\":\"AND\",\n" +
                "            \"predicateLeaves\":[\n" +
                "               {\n" +
                "                  \"type\":\"strong\",\n" +
                "                  \"operation\":\"EQ\",\n" +
                "                  \"tag\":\"tagStr2\",\n" +
                "                  \"value\":\"water\"\n" +
                "               },\n" +
                "               {\n" +
                "                  \"type\":\"long\",\n" +
                "                  \"operation\":\"GTE\",\n" +
                "                  \"tag\":\"tag2\",\n" +
                "                  \"value\":\"999999999999999\"\n" +
                "               }\n" +
                "            ]\n" +
                "         },\n" +
                "         {\n" +
                "            \"logicalOperation\":\"AND\",\n" +
                "            \"predicateLeaves\":[\n" +
                "               {\n" +
                "                  \"type\":\"string\",\n" +
                "                  \"operation\":\"CONTAINS\",\n" +
                "                  \"tag\":\"tagStr\",\n" +
                "                  \"value\":\"spring\"\n" +
                "               },\n" +
                "               {\n" +
                "                  \"type\":\"integer\",\n" +
                "                  \"operation\":\"EQ\",\n" +
                "                  \"tag\":\"tag\",\n" +
                "                  \"value\":\"123\"\n" +
                "               }\n" +
                "            ]\n" +
                "         }\n" +
                "      ]\n" +
                "    }\n" +
                "}";

        mockMvc.perform(post("/rules").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(status().isBadRequest());
        verify(rulesRepository,times(0)).save(ArgumentMatchers.any(Rule.class));
    }


}