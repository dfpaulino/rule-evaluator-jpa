package org.farmtec.res.jpa.parser.util;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.service.rule.loader.dto.LeafDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 09/02/2021
 */
class PredicateLeafMapLeafDtoTest {

    PredicateLeaf p1;
    @BeforeEach
    void setUp() {
        p1 = new PredicateLeaf();
        p1.setId(1L);
        p1.setCreateTime(new Date());
        p1.setType("String");
        p1.setTag("tag1");
        p1.setValue("a");
        p1.setOperation("EQ");

    }

    @Test
    void predicateLeafToLeafDto_whenIsNull_shouldReturnEmtyLeaf() {
        //when
        LeafDto leafDto = PredicateLeafMapLeafDto.PredicateLeafToLeafDto(null);
        //then
        assertThat(leafDto).isNotNull();
        assertThat(leafDto.isComplete()).isFalse();
    }

    @Test
    void predicateLeafToLeafDto_whenIsNotNull_shouldReturn() {
        //when
        LeafDto leafDto = PredicateLeafMapLeafDto.PredicateLeafToLeafDto(p1);
        //then
        assertAll (
                () -> assertThat(leafDto).isNotNull(),
                () -> assertThat(PredicateLeafMapLeafDto.PredicateLeafToLeafDto(p1)).isInstanceOf(LeafDto.class),
                () -> assertThat(leafDto.isComplete()).isTrue(),
                () -> assertThat(leafDto.getType()).isEqualTo(p1.getType()),
                () -> assertThat(leafDto.getTag()).isEqualTo(p1.getTag()),
                () -> assertThat(leafDto.getOperation()).isEqualTo(p1.getOperation()),
                () -> assertThat(leafDto.getValue()).isEqualTo(p1.getValue())
        );

    }
}