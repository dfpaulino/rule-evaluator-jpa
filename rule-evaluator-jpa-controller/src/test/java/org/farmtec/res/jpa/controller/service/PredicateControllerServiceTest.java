package org.farmtec.res.jpa.controller.service;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.farmtec.res.jpa.service.utils.RulesValidatorImpl;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by dp on 17/03/2021
 */
@ExtendWith(MockitoExtension.class)
class PredicateControllerServiceTest {

    @Mock
    PredicateLeafRepository predicateLeafRepository;
    @Mock
    RulesValidatorImpl rulesValidator;
    PredicateControllerService predicateControllerService;
    PredicateLeaf p1, p2, p3, p4;

    @BeforeEach
    void setUp() {
        predicateControllerService = new PredicateControllerService(predicateLeafRepository, rulesValidator);
        setPredicates();
    }

    @Test
    void getAllPredicates() {
        //given
        when(predicateLeafRepository.findAll()).thenReturn(Arrays.asList(p1,p2,p3,p4));
        //when
        List<PredicateLeaf> predicateLeafs =predicateControllerService.getAllPredicates();
        //then
        assertThat(predicateLeafs.size()).isEqualTo(4);
    }

    @Test
    void getPredicateById() {
        //given
        when(predicateLeafRepository.findById(anyLong())).thenReturn(Optional.of(p1));
        //when
        PredicateLeaf predicateLeaf = predicateControllerService.getPredicateById(1L);
        //then
        assertThat(predicateLeaf).isEqualTo(p1);
    }

    @Test
    void getPredicateById_whenNotFound_thenThrowException() {
        //given
        //when
        assertThrows(ResourceNotFound.class,() -> predicateControllerService.getPredicateById(1L));
    }

    @Test
    void updatePredicate() {
        //given
        PredicateLeaf updatedPredicate = new PredicateLeaf();
        updatedPredicate.setType("string");
        updatedPredicate.setOperation("CONTAINS");
        updatedPredicate.setTag("framework");
        updatedPredicate.setValue("spring");

        when(predicateLeafRepository.findById(anyLong())).thenReturn(Optional.of(p1));
        //when
        PredicateLeaf predicateLeaf = predicateControllerService.updatePredicate(p1.getId(),updatedPredicate);
        //then
        assertThat(predicateLeaf).isNotNull();
        assertThat(predicateLeaf).isEqualTo(updatedPredicate);
    }

    @Test
    void updatePredicate_whenIdNotFound_throwsException() {
        //given
        PredicateLeaf updatedPredicate = new PredicateLeaf();
        updatedPredicate.setType("string");
        updatedPredicate.setOperation("CONTAINS");
        updatedPredicate.setTag("framework");
        updatedPredicate.setValue("spring");

        //when
        assertThrows(ResourceNotFound.class,() ->predicateControllerService.updatePredicate(2L,updatedPredicate));
    }

    @Test
    void updatePredicate_whenInvalid_throwsException() {
        //given
        PredicateLeaf updatedPredicate = new PredicateLeaf();
        updatedPredicate.setType("string");
        updatedPredicate.setOperation("CONTAINS");
        updatedPredicate.setTag("framework");
        updatedPredicate.setValue("spring");
        when(rulesValidator.validatePredicate(any())).thenThrow(new InvalidOperation("some rror"));
        //when
        assertThrows(InvalidOperation.class,() ->predicateControllerService.updatePredicate(2L,updatedPredicate));
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