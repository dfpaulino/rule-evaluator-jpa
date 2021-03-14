package org.farmtec.res.jpa.controller.service;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by dp on 14/03/2021
 */
@ExtendWith(MockitoExtension.class)
class GroupControllerServiceTest {

    @Mock
    GroupCompositeRepository groupCompositeRepository;
    GroupControllerService groupControllerService;
    GroupComposite g1, g2, gGroup, gGroupAdd;
    PredicateLeaf p1, p2, p3, p4, p5;


    @BeforeEach
    void setUp() {
        groupControllerService = new GroupControllerService(groupCompositeRepository);

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

        gGroupAdd = new GroupComposite();
        gGroupAdd.setId(5L);
        gGroupAdd.setCreateTime(new Date());
        gGroupAdd.setUpdateTime(new Date());
        gGroupAdd.setLogicalOperation("AND");
        gGroupAdd.setPredicateLeaves(new HashSet<PredicateLeaf>(Set.of(p5)));
    }

    @Test
    void getGroupById() {
        //given
        when(groupCompositeRepository.findById(1L)).thenReturn(Optional.of(g1));
        //when
        GroupComposite gc = groupControllerService.getGroupById(1L);
        //then
        assertThat(gc).isNotNull();
        assertThat(gc).isInstanceOf(GroupComposite.class);
        assertThat(gc).isEqualTo(g1);
    }

    @Test
    void getGroupById_WhenNotFound_ShouldThrowException() {
        //given
        //when
        //then
        assertThrows(ResourceNotFound.class, () -> groupControllerService.getGroupById(1L));
    }

    @Test
    void deleteGroupPredicate() {
        //given
        when(groupCompositeRepository.findById(1L)).thenReturn(Optional.of(g1));
        int currentPredicateSize = g1.getPredicateLeaves().size();
        //when
        GroupComposite gc = groupControllerService.deleteGroupPredicate(g1.getId(), p1.getId());
        //then
        assertThat(gc.getPredicateLeaves().size()).isEqualTo(currentPredicateSize - 1);
        assertThat(gc.getPredicateLeaves().contains(p1)).isFalse();
    }

    @Test
    void deleteGroupPredicate_whenGroupNotFound_throwsException() {
        assertThrows(ResourceNotFound.class, () -> groupControllerService.deleteGroupPredicate(20L, p1.getId()));
    }

    @Test
    void deleteChildGroup() {
        //given
        when(groupCompositeRepository.findById(3L)).thenReturn(Optional.of(gGroup));
        int groupSize = gGroup.getGroupComposites().size();
        //when
        GroupComposite gc = groupControllerService.deleteChildGroup(gGroup.getId(), g1.getId());
        //then
        assertThat(gc.getGroupComposites().size()).isEqualTo(groupSize - 1);
        assertThat(gc.getGroupComposites().contains(g1)).isFalse();
        assertThat(gc.getGroupComposites().contains(g2)).isTrue();
    }

    @Test
    void deleteChildGroup_whenGroupNotFound() {
        assertThrows(ResourceNotFound.class, () -> groupControllerService.deleteChildGroup(gGroup.getId(), g1.getId()));
    }


    @Test
    void addGroup_toParentGroup() {
        //given
        //given
        when(groupCompositeRepository.findById(3L)).thenReturn(Optional.of(gGroup));
        int groupSize = gGroup.getGroupComposites().size();
        //when
        GroupComposite gc = groupControllerService.addGroup(gGroup.getId(),gGroupAdd);
        //then
        assertThat(gc.getGroupComposites().size()).isEqualTo(groupSize + 1);
    }

    @Test
    void addGroup_parentNotFound() {
        //given
        //given
        when(groupCompositeRepository.save(any())).thenReturn(gGroup);
        //when
        GroupComposite gc = groupControllerService.addGroup(gGroup.getId(),gGroupAdd);
        //then
        assertThat(gc).isNotNull();
    }

    @Test
    void addPredicateToGroup() {
        //given
        when(groupCompositeRepository.findById(anyLong())).thenReturn(Optional.of(g1));
        int predicateSize = g1.getPredicateLeaves().size();
        //when
        GroupComposite gc = groupControllerService.addPredicateToGroup(g1.getId(), p5);
        //then
        assertThat(gc.getPredicateLeaves().size()).isEqualTo(predicateSize + 1);
        assertThat(gc.getPredicateLeaves().contains(p5)).isTrue();
    }

    @Test
    void addPredicateToGroup_groupNotFound_shouldThrowException() {
        //given
        //when
        //then
        assertThrows(ResourceNotFound.class, () -> groupControllerService.addPredicateToGroup(1L,p5));
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