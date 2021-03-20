package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.controller.service.PredicateControllerService;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


/**
 * Created by dp on 18/02/2021
 */
@ExtendWith(SpringExtension.class)
class PredicateLeafControllerTest {

    private  MockMvc mockMvc ;
    @Mock
    PredicateControllerService predicateControllerService;

    PredicateLeaf p1,p2,p3,p4;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PredicateLeafController(predicateControllerService))
                .setControllerAdvice(ControllerAdvice.class)
                .build();
        setPredicates();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getAll() throws Exception{
        when(predicateControllerService.getAllPredicates()).thenReturn(Arrays.asList(p1,p2,p3,p4));

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
        when(predicateControllerService.getAllPredicates()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/predicates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content",is(new ArrayList())))
                .andDo(print());
    }

    @Test
    void getPredicateLeafById() throws Exception {
        when(predicateControllerService.getPredicateById(anyLong())).thenReturn(p1);

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
    void getPredicateLeafById_whenNotFound() throws Exception {
        when(predicateControllerService.getPredicateById(anyLong())).thenThrow(new ResourceNotFound("Predicate not found"));

        mockMvc.perform(get("/predicates/1").accept("application/hal+json"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFound))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void updatePredicate() throws Exception {

        String content ="{" +
                "\"type\":\"integer\"," +
                "\"operation\":\"GTE\"," +
                "\"tag\":\"age\"," +
                "\"value\":\"65\"" +
                "}";
        when(predicateControllerService.updatePredicate(anyLong(),ArgumentMatchers.any(PredicateLeaf.class))).thenReturn(p1);
        mockMvc.perform(put("/predicates/1/").content(content).contentType(MediaType.APPLICATION_JSON))
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
    public void updatePredicate_whenInvalid_shouldReturn400() throws Exception {

        String content ="{" +
                "\"type\":\"integer\"," +
                "\"operation\":\"GTE\"," +
                "\"tag\":\"age\"," +
                "\"value\":\"65\"" +
                "}";
        when(predicateControllerService.updatePredicate(anyLong(),ArgumentMatchers.any(PredicateLeaf.class))).thenThrow(new InvalidOperation("error"));
        mockMvc.perform(put("/predicates/1/").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
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