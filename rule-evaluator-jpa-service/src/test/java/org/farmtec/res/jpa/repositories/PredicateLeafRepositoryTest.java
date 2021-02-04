package org.farmtec.res.jpa.repositories;

import org.farmtec.res.jpa.config.JpaConfig;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 04/02/2021
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpaConfig.class)
@DataJpaTest
@Transactional
@Rollback(value = true)
class PredicateLeafRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    PredicateLeafRepository predicateLeafRepository;

    @Test
    public void PredicateCreatePersist(){
        PredicateLeaf p = new PredicateLeaf();
        p.setOperation("EQ");
        p.setTag("tag");
        p.setType("integer");
        p.setValue("123");

        entityManager.persist(p);
        entityManager.flush();
        //predicateLeafRepository.save(p);

        List<PredicateLeaf> pDbAll= predicateLeafRepository.findAll();
        System.out.println(pDbAll.get(0).toString());
        System.out.println("ID>>>"+pDbAll.get(0).getId());
        System.out.println("Time>>>"+pDbAll.get(0).getCreateTime());
        assertAll(
                () -> assertThat(pDbAll.size()).isEqualTo(1),

                () -> assertThat(pDbAll.get(0).getOperation()).isEqualTo(p.getOperation()),
                () -> assertThat(pDbAll.get(0).getTag()).isEqualTo(p.getTag()),
                () -> assertThat(pDbAll.get(0).getValue()).isEqualTo(p.getValue()),
                () -> assertThat(pDbAll.get(0).getType()).isEqualTo(p.getType())
        );
    }
}