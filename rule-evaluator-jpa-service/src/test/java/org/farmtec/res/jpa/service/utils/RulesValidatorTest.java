package org.farmtec.res.jpa.service.utils;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 17/03/2021
 */
class RulesValidatorTest {

    private GroupComposite gp1;
    private GroupComposite gp12;
    private GroupComposite gp11;
    private Rule rule;
    private PredicateLeaf p1,p2,p3,p4,p5;
    private RulesValidatorImpl rulesValidator = new RulesValidatorImpl();

    @BeforeEach
    void setUp() {
        System.out.println(">>>>>>>setting up");
        p1 = new PredicateLeaf();
        p1.setType("integer");
        p1.setTag("tag1");
        p1.setValue("123");
        p1.setOperation("EQ");

        p2 = new PredicateLeaf();
        p2.setType("String");
        p2.setTag("tag2");
        p2.setValue("a");
        p2.setOperation("EQ");

        p3 = new PredicateLeaf();
        p3.setType("String");
        p3.setTag("tag3");
        p3.setValue("a");
        p3.setOperation("EQ");

        p4 = new PredicateLeaf();
        p4.setType("String");
        p4.setTag("tag4");
        p4.setValue("a");
        p4.setOperation("EQ");

        p5 = new PredicateLeaf();
        p5.setType("String");
        p5.setTag("tag4");
        p5.setValue("a");
        p5.setOperation("EQ");

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
        gp1.setPredicateLeaves(new HashSet<>(Arrays.asList(p5)));
        gp1.setGroupComposites(new HashSet<>(Arrays.asList(gp11,gp12)));

        rule = new Rule();
        rule.setGroupComposite(gp1);
    }

    @Test
    void validateRule() {
        assertThat(rulesValidator.validateRule(rule)).isTrue();
    }

    @Test
    void validateRule_whenTypeNotSupported() {
        p1.setType("dummyType");
        assertThrows(IllegalArgumentException.class,()->rulesValidator.validateRule(rule));
    }

    @Test
    void validateRule_whenCantCOnvertInteger() {
        p1.setValue("i should be a number");
        assertThrows(NumberFormatException.class,()->rulesValidator.validateRule(rule));
    }

    @Test
    void validateRule_whenOperationNotSupported() {
        p1.setOperation("CONTAINS");
        assertThrows(InvalidOperation.class,()->rulesValidator.validateRule(rule));
    }

    @Test
    void getAllPredicates() {
        assertThat(rulesValidator.getAllPredicates(gp1).size()).isEqualTo(5);
    }
}