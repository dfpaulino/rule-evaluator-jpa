package org.farmtec.res.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dp on 23/02/2021
 */
@ExtendWith(SpringExtension.class)
class GroupCompositeControllerTest {

    MockMvc mockMvc;
    @Mock
    GroupCompositeRepository groupCompositeRepository;
    GroupComposite g1, g2, gGroup, gPadd, gGroupAdd;
    PredicateLeaf p1, p2, p3, p4, p5;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(new GroupCompositeController(groupCompositeRepository))
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

        gPadd = new GroupComposite();
        gPadd.setId(4L);
        gPadd.setCreateTime(new Date());
        gPadd.setUpdateTime(new Date());
        gPadd.setLogicalOperation("AND");
        gPadd.setPredicateLeaves(new HashSet<PredicateLeaf>(Set.of(p1, p2, p5)));

        gGroupAdd = new GroupComposite();
        gGroupAdd.setId(5L);
        gGroupAdd.setCreateTime(new Date());
        gGroupAdd.setUpdateTime(new Date());
        gGroupAdd.setLogicalOperation("AND");
        gGroupAdd.setPredicateLeaves(new HashSet<PredicateLeaf>(Set.of(p5)));

        when(groupCompositeRepository.findById(1L)).thenReturn(Optional.of(g1));
        when(groupCompositeRepository.findById(2L)).thenReturn(Optional.of(g2));
        when(groupCompositeRepository.findById(3L)).thenReturn(Optional.of(gGroup));
        when(groupCompositeRepository.findById(4L)).thenReturn(Optional.of(gPadd));
        when(groupCompositeRepository.save(any(GroupComposite.class))).thenReturn(gPadd);
    }

    @Test
    void getGroupById_shouldReturnNestedPredicates() throws Exception {
        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.logicalOperation", is("AND")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content", hasSize(2)))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].operation").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].type").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].tag ").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].value").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].links[0].href",
                        containsString("http://localhost/predicates/")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].links[1].rel", is("deletePredicate")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].links[1].href",
                        containsString("http://localhost/groups/1/predicate/")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[1].operation").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[1].type").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[1].tag ").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[1].value").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[1].links[1].rel", is("deletePredicate")));
    }

    @Test
    void getGroupByIdComposedByOtherGroups_shouldReturnNestedGroupWithNestedPredicates() throws Exception {
        mockMvc.perform(get("/groups/3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.logicalOperation", is("OR")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content", hasSize(0)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content", hasSize(2)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].logicalOperation", is("AND")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content", hasSize(2)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[1].rel", is("deleteChildGroup")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].tag ").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].value").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].links[1].rel", is("deletePredicate")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].operation").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].type").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].tag ").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].value").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].links[1].rel", is("deletePredicate")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[0].href",
                        containsString("http://localhost/groups/")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[1].href",
                        containsString("http://localhost/groups/3/group/")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[1].rel", is("deleteChildGroup")))


                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].logicalOperation", is("AND")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content", hasSize(2)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].links[1].rel", is("deleteChildGroup")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[0].tag ").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[0].value").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[0].links[1].rel", is("deletePredicate")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[1].operation").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[1].type").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[1].tag ").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[1].value").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].predicateRepresentationModels.content[1].links[1].rel", is("deletePredicate")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].links[0].href",
                        containsString("http://localhost/groups/")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].links[1].href",
                        containsString("http://localhost/groups/3/group/")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[1].links[1].rel", is("deleteChildGroup")));

    }

    @Test
    public void deletePredicateFromGroup() throws Exception {
        mockMvc.perform(delete("/groups/1/predicate/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.logicalOperation", is("AND")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content", hasSize(1)))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].operation").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].type").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].tag ").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].value").exists())
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content[0].links[1].rel", is("deletePredicate")));
    }

    @Test
    public void deleteGroupFromParentGroup() throws Exception {
        mockMvc.perform(delete("/groups/3/group/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.logicalOperation", is("OR")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content", hasSize(0)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content", hasSize(1)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].logicalOperation", is("AND")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content", hasSize(2)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].links[1].rel", is("deleteChildGroup")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].tag ").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].value").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[0].links[1].rel", is("deletePredicate")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].operation").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].type").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].tag ").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].value").exists())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].links[0].rel", is("self")))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content[1].links[1].rel", is("deletePredicate")));
    }

    @Test
    public void addPredicateToGroup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PredicateLeaf p = new PredicateLeaf();
        p.setOperation("GTE");
        p.setTag("tagInt");
        p.setType("integer");
        p.setValue("40");
        String content = objectMapper.writeValueAsString(p);

        System.out.println(">>>>" + content);

        mockMvc.perform(post("/groups/1/predicate").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.logicalOperation", is("AND")))
                .andExpect(jsonPath("$.predicateRepresentationModels.content", hasSize(3)));
    }

    @Test
    public void addGroupToExistentGroup() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        //given
        PredicateLeaf p = new PredicateLeaf();
        p.setOperation("GTE");
        p.setTag("tagInt");
        p.setType("integer");
        p.setValue("40");

        GroupComposite groupCompositeToAdd = new GroupComposite();
        groupCompositeToAdd.setLogicalOperation("AND");
        groupCompositeToAdd.setPredicateLeaves(new HashSet<>(Set.of(p)));

        String content = objectMapper.writeValueAsString(groupCompositeToAdd);

        System.out.println(">>>>" + content);
        g1.getGroupComposites().add(gGroupAdd);
        when(groupCompositeRepository.save(any(GroupComposite.class))).thenReturn(g1);
        //when//then
        mockMvc.perform(post("/groups/1/").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content", hasSize(1)))
                .andExpect(jsonPath("$.groupCompositeRepresentationModels.content[0].predicateRepresentationModels.content", hasSize(1)));
    }

    @Test
    public void addNewGroup() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        //given
        PredicateLeaf p = new PredicateLeaf();
        p.setOperation("GTE");
        p.setTag("tagInt");
        p.setType("integer");
        p.setValue("40");

        GroupComposite groupCompositeToAdd = new GroupComposite();
        groupCompositeToAdd.setLogicalOperation("AND");
        groupCompositeToAdd.setPredicateLeaves(new HashSet<>(Set.of(p)));

        String content = objectMapper.writeValueAsString(groupCompositeToAdd);

        System.out.println(">>>>" + content);
        g1.getGroupComposites().add(gGroupAdd);
        when(groupCompositeRepository.save(any(GroupComposite.class))).thenReturn(gGroupAdd);
        //when//then
        mockMvc.perform(post("/groups/6").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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

        p5 = new PredicateLeaf();
        p5.setId(5L);
        p5.setCreateTime(new Date());
        p5.setUpdateTime(new Date());
        p5.setOperation("GTE");
        p5.setTag("tagInt");
        p5.setType("integer");
        p5.setValue("40");
    }
}