package org.farmtec.res.jpa.repositories;

import org.farmtec.res.jpa.config.JpaConfig;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;

import org.farmtec.res.jpa.model.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 31/01/2021
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig.class)
@DataJpaTest
@Transactional
@Rollback(value = true)
class RuleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RulesRepository rulesRepository;


    private PredicateLeaf p1;
    private PredicateLeaf p2;
    private PredicateLeaf p3;
    private PredicateLeaf p4;
    private GroupComposite G1;
    private GroupComposite G11;
    private GroupComposite G12;


    @BeforeEach
    void setUp() {
        p1 = new PredicateLeaf();
        p1.setOperation("EQ");
        p1.setTag("tag");
        p1.setType("integer");
        p1.setValue("123");

        p2 = new PredicateLeaf();
        p2.setOperation("CONTAINS");
        p2.setTag("tagStr");
        p2.setType("string");
        p2.setValue("spring");

        p3 = new PredicateLeaf();
        p3.setOperation("EQ");
        p3.setTag("tagStr2");
        p3.setType("string");
        p3.setValue("water");

        p4 = new PredicateLeaf();
        p4.setOperation("GTE");
        p4.setTag("tag2");
        p4.setType("long");
        p4.setValue("999999999999999");

        G11 = new GroupComposite();
        G11.setPredicateLeaves(Set.of(p1,p2));
        G11.setLogicalOperation("AND");

        G12 = new GroupComposite();
        G12.setPredicateLeaves(Set.of(p3,p4));
        G12.setLogicalOperation("AND");

        G1 = new GroupComposite();
        G1.setGroupComposites(Set.of(G11,G12));
        G1.setLogicalOperation("OR");
    }


    @Test
    public void persistRule(){
        //given
        Rule rule = new Rule();
        rule.setName("rule_1");
        rule.setPriority(1);
        rule.setGroupComposite(G1);

        //when
        entityManager.persist(rule);

        List<Rule> fetched = rulesRepository.findAll();
        System.out.println(rule);

        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(1),
                () -> assertThat(fetched.contains(rule)).isTrue()
        );


        }
}