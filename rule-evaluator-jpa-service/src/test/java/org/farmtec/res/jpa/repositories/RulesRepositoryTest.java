package org.farmtec.res.jpa.repositories;

import org.farmtec.res.jpa.config.JpaConfig;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 31/01/2021
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig.class)
@DataJpaTest
class RulesRepositoryTest {
    @Test
    public void testContext(){
    }

    @Test
    public void PredicateCreatePresist(){
        PredicateLeaf p = new PredicateLeaf();
        p.setOperation("EQ");
        p.setTag("tag");
        p.setType("integer");
        p.setValue("123");

    }



}