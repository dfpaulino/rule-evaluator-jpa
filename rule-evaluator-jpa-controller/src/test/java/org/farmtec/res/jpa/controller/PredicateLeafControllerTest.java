package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.JsonPath;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;


/**
 * Created by dp on 18/02/2021
 */
@ExtendWith(SpringExtension.class)
class PredicateLeafControllerTest {

    private  MockMvc mockMvc ;
    @Mock
    PredicateLeafRepository predicateLeafRepository;

    PredicateLeaf p1,p2,p3,p4;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PredicateLeafController(predicateLeafRepository))
                .build();
        setPredicates();
        when(predicateLeafRepository.findAll()).thenReturn(Arrays.asList(p1,p2,p3,p4));
        when(predicateLeafRepository.findById(1L)).thenReturn(Optional.of(p1));
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getAll() throws Exception{
        mockMvc.perform(get("/predicates").accept("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("links[0].rel",is("self")))
                .andExpect(jsonPath("links[0].href  ",is("http://localhost/predicates")))
                .andExpect(jsonPath("content",hasSize(4)))
                .andExpect(jsonPath("content[0].id",is(1)))
                .andExpect(jsonPath("content[0].links[0].rel",is("self")))
                .andExpect(jsonPath("content[0].links[0].href  ",is("http://localhost/predicates/1")))
                .andExpect(jsonPath("content[1].id",is(2)))
                .andExpect(jsonPath("content[2].id",is(3)))
                .andExpect(jsonPath("content[3].id",is(4)))
                .andDo(print());
    }

    @Test
    void getAll_whenEmpty() throws Exception{
        when(predicateLeafRepository.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/predicates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content",is(new ArrayList())))
                .andDo(print());
    }

    @Test
    void getPredicateLeafById() throws Exception {
        mockMvc.perform(get("/predicates/1").accept("application/hal+json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("id",is(1)))
                .andExpect(jsonPath(("type"),is("integer")))
                .andExpect(jsonPath(("operation"),is("EQ")))
                .andExpect(jsonPath(("tag"),is("tag")))
                .andExpect(jsonPath(("value"),is("123")))
                .andExpect(jsonPath("links[0].rel",is("self")))
                .andExpect(jsonPath("links[0].href  ",is("http://localhost/predicates/1")));
    }

    @Test
    @Disabled
    void updatePredicate() {
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
}