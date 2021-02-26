package org.farmtec.res.jpa.repositories;

import org.farmtec.res.jpa.config.JpaConfig;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
class GroupCompositeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    private GroupCompositeRepository groupCompositeRepository;
    @Autowired
    private PredicateLeafRepository predicateLeafRepository;

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
        G11.setPredicateLeaves(new HashSet<>(Set.of(p1,p2)));

        G11.setLogicalOperation("AND");

        G12 = new GroupComposite();
        G12.setPredicateLeaves(Set.of(p3,p4));
        G12.setLogicalOperation("AND");

        G1 = new GroupComposite();
        G1.setGroupComposites(Set.of(G11,G12));
        G1.setLogicalOperation("OR");
    }

    @Test
    public void groupCompositePersistG11() {
        //GroupComposite saved = groupCompositeRepository.save(G11);
        //System.out.println(saved.toString());
        entityManager.persist(G11);

        //entityManager.flush();
        List<GroupComposite> fetched = groupCompositeRepository.findAll();
        System.out.println(fetched.toString());
        System.out.println(p1.getId());

        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(1),
                () -> assertThat(fetched.get(0).getLogicalOperation()).isEqualTo("AND"),
                () -> assertThat(fetched.get(0).getPredicateLeaves().size()).isEqualTo(2),
                //p1 is in a managed state, so should have an Id
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p1)).isTrue(),
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p2)).isTrue(),
                () -> assertThat(fetched.get(0).getGroupComposites()).isNull()
        );
    }

    @Test
    public void groupCompositePersistG12() {
        //GroupComposite saved = groupCompositeRepository.save(G11);
        //System.out.println(saved.toString());
        entityManager.persist(G12);

        //entityManager.flush();
        List<GroupComposite> fetched = groupCompositeRepository.findAll();
        System.out.println(fetched.toString());
        System.out.println(p3.getId());

        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(1),
                () -> assertThat(fetched.get(0).getLogicalOperation()).isEqualTo("AND"),
                () -> assertThat(fetched.get(0).getPredicateLeaves().size()).isEqualTo(2),
                //p3 is in a managed state, so should have an Id
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p3)).isTrue(),
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p4)).isTrue(),
                () -> assertThat(fetched.get(0).getGroupComposites()).isNull()
        );
    }

    @Test
    public void groupCompositePersistG1() {
        //GroupComposite saved = groupCompositeRepository.save(G11);
        //System.out.println(saved.toString());
        entityManager.persist(G1);

        //entityManager.flush();
        List<GroupComposite> fetched = groupCompositeRepository.findAll();
        System.out.println(fetched.toString());

        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(3),
                () -> assertThat(fetched.contains(G1)).isTrue(),
                () -> assertThat(fetched.contains(G11)).isTrue(),
                () -> assertThat(fetched.contains(G12)).isTrue(),
                () -> assertThat(groupCompositeRepository.findById(G1.getId()).get().getLogicalOperation()).isEqualTo("OR"),
                () -> assertThat(groupCompositeRepository.findById(G1.getId()).get().getGroupComposites().contains(G11)).isTrue(),
                () -> assertThat(groupCompositeRepository.findById(G1.getId()).get().getGroupComposites().contains(G12)).isTrue(),
                () -> assertThat(groupCompositeRepository.findById(G1.getId()).get().getPredicateLeaves()).isNull(),
                () -> assertThat(predicateLeafRepository.count()).isEqualTo(4)
        );
    }

    @Test
    public void groupCompositeRemoveG11_shouldRemoveGroupAndPredicate() {
        //GroupComposite saved = groupCompositeRepository.save(G11);
        //System.out.println(saved.toString());
        GroupComposite saved = entityManager.persist(G11);

        //entityManager.flush();
        List<GroupComposite> fetched = groupCompositeRepository.findAll();
        System.out.println(fetched.toString());
        System.out.println(p1.getId());
        //assert that the object G11 is composed correctly
        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(1),
                () -> assertThat(fetched.get(0).getLogicalOperation()).isEqualTo("AND"),
                () -> assertThat(fetched.get(0).getPredicateLeaves().size()).isEqualTo(2),
                //p1 is in a managed state, so should have an Id
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p1)).isTrue(),
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p2)).isTrue()
        );

        groupCompositeRepository.deleteById(saved.getId());

        assertThat(groupCompositeRepository.findById(saved.getId()).isPresent()).isFalse();
        //assert that predicate were deleted to
        assertThat(predicateLeafRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void groupCompositeRemovePredicateOfG11() {
        //GroupComposite saved = groupCompositeRepository.save(G11);
        //System.out.println(saved.toString());
        GroupComposite saved = entityManager.persist(G11);

        //entityManager.flush();
        List<GroupComposite> fetched = groupCompositeRepository.findAll();
        System.out.println(fetched.toString());
        System.out.println(p1.getId());
        //assert that the object G11 is composed correctly
        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(1),
                () -> assertThat(fetched.get(0).getLogicalOperation()).isEqualTo("AND"),
                () -> assertThat(fetched.get(0).getPredicateLeaves().size()).isEqualTo(2),
                //p1 is in a managed state, so should have an Id
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p1)).isTrue(),
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p2)).isTrue()
        );
        GroupComposite g = groupCompositeRepository.getOne(fetched.get(0).getId());
        Iterator<PredicateLeaf> it = g.getPredicateLeaves().iterator();
        Set<PredicateLeaf> ps =g.getPredicateLeaves();
        System.out.println("Removing");
        g.getPredicateLeaves().removeIf(p -> p.getId()==p1.getId());

        groupCompositeRepository.save(fetched.get(0));
        entityManager.flush();

        assertThat(groupCompositeRepository.findById(fetched.get(0).getId()).isPresent()).isTrue();
        assertThat(groupCompositeRepository.findById(fetched.get(0).getId()).get().getPredicateLeaves().size())
                .isEqualTo(1);
        //assert that predicate were deleted to
        assertThat(groupCompositeRepository.findById(fetched.get(0).getId()).get().getPredicateLeaves().size())
                .isEqualTo(1);
        assertThat(predicateLeafRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void groupCompositeRemoveG11_shouldRemoveGroupAndPredicateButNotG12() {
        //GroupComposite saved = groupCompositeRepository.save(G11);
        //System.out.println(saved.toString());
        GroupComposite saved = entityManager.persist(G11);
        GroupComposite saved2 = entityManager.persist(G12);

        //entityManager.flush();
        List<GroupComposite> fetched = groupCompositeRepository.findAll();
        System.out.println(fetched.toString());
        System.out.println(p1.getId());

        //assert that the object G11 is composed correctly
        assertAll(
                () -> assertThat(fetched.size()).isEqualTo(2),
                () -> assertThat(fetched.get(0).getLogicalOperation()).isEqualTo("AND"),
                () -> assertThat(fetched.get(0).getPredicateLeaves().size()).isEqualTo(2),
                //p1 is in a managed state, so should have an Id
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p1)).isTrue(),
                () -> assertThat(fetched.get(0).getPredicateLeaves().contains(p2)).isTrue()
        );

        //delete G11
        groupCompositeRepository.deleteById(saved.getId());
        //assert that delete was done ONLY on G11 and its predicate
        assertThat(groupCompositeRepository.findById(saved.getId()).isPresent()).isFalse();
        //we remain with the predicates of the other G12
        assertThat(groupCompositeRepository.findById(saved2.getId()).isPresent()).isTrue();
        assertThat(predicateLeafRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void addPredicateToGroup() {
        GroupComposite saved = entityManager.persist(G11);
        entityManager.flush();
        PredicateLeaf pToAdd = new PredicateLeaf();
        pToAdd.setType("integer");
        pToAdd.setTag("tagInt");
        pToAdd.setOperation("GTE");
        pToAdd.setValue("40");

        GroupComposite groupComposite = groupCompositeRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException());
        groupComposite.getPredicateLeaves().add(pToAdd);
        groupCompositeRepository.save(groupComposite);
        entityManager.flush();
        assertThat(groupCompositeRepository.findById(saved.getId()).get().getPredicateLeaves().size()).isEqualTo(3);

    }
}