package org.farmtec.res.jpa.parser.util;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 07/02/2021
 */
class GroupCompositeMapGroupCompositeDtoTest {

    private GroupComposite gp1;
    private GroupComposite gp12;
    private GroupComposite gp11;
    private PredicateLeaf p1;
    private PredicateLeaf p2;
    private PredicateLeaf p3;
    private PredicateLeaf p4;
    @BeforeEach
    void setUp() {
        p1 = new PredicateLeaf();
        p1.setId(1L);
        p1.setCreateTime(new Date());
        p1.setType("String");
        p1.setTag("tag1");
        p1.setValue("a");
        p1.setOperation("EQ");

        p2 = new PredicateLeaf();
        p2.setId(2L);
        p2.setCreateTime(new Date());
        p2.setType("String");
        p2.setTag("tag2");
        p2.setValue("a");
        p2.setOperation("EQ");

        p3 = new PredicateLeaf();
        p3.setId(3L);
        p3.setCreateTime(new Date());
        p3.setType("String");
        p3.setTag("tag3");
        p3.setValue("a");
        p3.setOperation("EQ");

        p4 = new PredicateLeaf();
        p4.setId(4L);
        p4.setCreateTime(new Date());
        p4.setType("String");
        p4.setTag("tag4");
        p4.setValue("a");
        p4.setOperation("EQ");

        gp11 = new GroupComposite();
        gp11.setId(1L);
        gp11.setLogicalOperation("AND");
        gp11.setPredicateLeaves(new HashSet<>(Arrays.asList(p1,p2)));

        gp12 = new GroupComposite();
        gp12.setId(2L);
        gp12.setLogicalOperation("AND");
        gp12.setPredicateLeaves(new HashSet<>(Arrays.asList(p3,p4)));

        gp1 = new GroupComposite();
        gp1.setId(3L);
        gp1.setLogicalOperation("OR");
        gp1.setGroupComposites(new HashSet<>(Arrays.asList(gp11,gp12)));
    }

    @Test
    void groupCompositeToGroupCompositeDto_G1() {
        //given
        //the groups on setUp
        //when
        GroupCompositeDto dto = GroupCompositeMapGroupCompositeDto.groupCompositeToGroupCompositeDto(gp1);

        //then
        assertAll(
                () -> assertThat(dto.getPredicateNames()).containsExactlyInAnyOrder("G_1","G_2"),
                () -> assertThat(dto.getOperation()).isEqualTo(gp1.getLogicalOperation())
        );
    }
    @Test
    void groupCompositeToGroupCompositeDto_G11() {
        //given
        //the groups on setUp
        //when
        GroupCompositeDto dto = GroupCompositeMapGroupCompositeDto.groupCompositeToGroupCompositeDto(gp11);

        //then
        assertAll(
                () -> assertThat(dto.getPredicateNames()).containsExactlyInAnyOrder("P_1","P_2"),
                () -> assertThat(dto.getOperation()).isEqualTo(gp11.getLogicalOperation())
        );
    }

    @Test
    void groupCompositeToGroupCompositeDto_G12() {
        //given
        //the groups on setUp
        //when
        GroupCompositeDto dto = GroupCompositeMapGroupCompositeDto.groupCompositeToGroupCompositeDto(gp12);

        //then
        assertAll(
                () -> assertThat(dto.getPredicateNames()).containsExactlyInAnyOrder("P_3","P_4"),
                () -> assertThat(dto.getOperation()).isEqualTo(gp12.getLogicalOperation())
                );
    }
}