package com.example.resjpademo;

import java.util.Arrays;
import org.farmtec.res.jpa.model.Action;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.farmtec.res.jpa.repositories.RulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dp on 01/03/2021
 */
@Component
public class LoadRulesSample implements CommandLineRunner {

    @Autowired
    GroupCompositeRepository groupCompositeRepository;
    @Autowired
    RulesRepository rulesRepository;
    private Rule rule;
    private GroupComposite G11,G12,G1;
    private PredicateLeaf p1, p2, p3, p4, p5;
    @Override
    public void run(String... args) throws Exception {
        setUp();
        rulesRepository.save(rule);
        //groupCompositeRepository.save(G1);
    }

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
        G11.setPredicateLeaves(new HashSet<>(Set.of(p1, p2)));

        G11.setLogicalOperation("AND");

        G12 = new GroupComposite();
        G12.setPredicateLeaves(Set.of(p3, p4));
        G12.setLogicalOperation("AND");

        G1 = new GroupComposite();
        G1.setGroupComposites(Set.of(G11, G12));
        G1.setLogicalOperation("OR");

        Action action1 = new Action();
        action1.setType("SMS");
        action1.setData("message SMS");
        action1.setPriority(1);

        Action action2 = new Action();
        action2.setType("EMAIL");
        action2.setData("message EMAIL");
        action2.setPriority(2);

        rule = new Rule();
        rule.setName("Rule_1");
        rule.setPriority(1);
        rule.setActions(Arrays.asList(action1,action2));
        rule.setGroupComposite(G1);
    }

}
