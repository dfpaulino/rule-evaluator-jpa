package org.farmtec.res.jpa.controller;

import org.farmtec.res.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;

/**
 * Created by dp on 09/03/2021
 */
@ExtendWith(MockitoExtension.class)
class RuleReloadControllerTest {

    private MockMvc mockMvc;
    @Mock
    private RuleService ruleService;

    private final Date lastUpdate = new Date();

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new RuleReloadController(ruleService))
                .setControllerAdvice(DefaultHandlerExceptionResolver.class)
                .build();
    }

    @Test
    void reloadAndWait() throws Exception{
        //given
        when(ruleService.updateRules()).thenReturn(true);
        when(ruleService.getLastUpdate()).thenReturn(new Date());
        //when
        mockMvc.perform(post("/reload/rules"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.success",is(true)))
                .andExpect(jsonPath("$.lastUpdateTime").exists());
    }

    @Test
    void reloadAndWait_fails() throws Exception{
        //given
        when(ruleService.updateRules()).thenReturn(false);
        Date anHourAgo = new Date(System.currentTimeMillis() - 3600000);
        when(ruleService.getLastUpdate()).thenReturn(anHourAgo);
        //when
        mockMvc.perform(post("/reload/rules")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.success",is(false)))
                .andExpect(jsonPath("$.lastUpdateTime").exists());
    }

    @Test
    void lastUpdateDate() throws Exception{
        when(ruleService.getLastUpdate()).thenReturn(new Date());

        mockMvc.perform(get("/reload/rules"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.success",is(true)))
                .andExpect(jsonPath("$.lastUpdateTime").exists());
    }
}