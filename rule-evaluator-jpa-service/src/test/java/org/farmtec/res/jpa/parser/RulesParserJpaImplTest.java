package org.farmtec.res.jpa.parser;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.farmtec.res.jpa.repositories.RulesRepository;
import org.farmtec.res.service.rule.loader.RulesParser;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.farmtec.res.service.rule.loader.dto.LeafDto;
import org.farmtec.res.service.rule.loader.dto.RuleDto;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.farmtec.res.jpa.parser.RulesParserJpaImpl.PREDICATE_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Created by dp on 10/02/2021
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RulesParserJpaImplTest {
    private GroupComposite gp1;
    private GroupComposite gp12;
    private GroupComposite gp11;
    private PredicateLeaf p1;
    private PredicateLeaf p2;
    private PredicateLeaf p3;
    private PredicateLeaf p4;

    @Mock
    private RulesRepository rulesRepository;
    @Mock
    private GroupCompositeRepository groupCompositeRepository;
    @Mock
    private PredicateLeafRepository predicateLeafRepository;
    private AutoCloseable closeable;

    @BeforeAll
    void setUp() {
        System.out.println(">>>>>>>setting up");
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

        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    void tearDown() throws Exception{
        System.out.println("<<<<<<<<<tear down");
        closeable.close();
    }

    @Test
    void loadRules() {
    }

    @Test
    void getRuleLeafsDto() {
        //given
        when(predicateLeafRepository.findAll()).thenReturn(Arrays.asList(p1,p2,p3,p4));
        RulesParser rulesParser = new RulesParserJpaImpl(rulesRepository,groupCompositeRepository,
                predicateLeafRepository);
        //when
        Map<String, LeafDto> leafDto = rulesParser.getRuleLeafsDto();
        //then
        assertThat(leafDto.size()).isEqualTo(4);
        assertThat(leafDto.keySet()).containsExactlyInAnyOrder("P_2","P_1","P_3","P_4");
        leafDto.forEach((k,v) -> assertThat(v).isInstanceOf(LeafDto.class));
    }

    @Test
    void getGroupCompositesDto() {

        //given
        when(groupCompositeRepository.findAll()).thenReturn(Arrays.asList(gp1,gp11,gp12));
        RulesParser rulesParser = new RulesParserJpaImpl(rulesRepository,groupCompositeRepository,
                predicateLeafRepository);
        //when
        Map<String, GroupCompositeDto> groupCompositesDto = rulesParser.getGroupCompositesDto();
        //then
        assertThat(groupCompositesDto.size()).isEqualTo(3);
        assertThat(groupCompositesDto.keySet()).containsExactlyInAnyOrder("G_1","G_2","G_3");
        groupCompositesDto.forEach((k,v) -> assertThat(v).isInstanceOf(GroupCompositeDto.class));
    }

    @Test
    void getRulesDto() {
        //given
        Rule ruleDom = new Rule();
        ruleDom.setPriority(1);
        ruleDom.setName("Rule_Name");
        ruleDom.setId(1L);
        ruleDom.setGroupComposite(gp1);
        when(rulesRepository.findAll()).thenReturn(Collections.singletonList(ruleDom));
        RulesParser rulesParser = new RulesParserJpaImpl(rulesRepository,groupCompositeRepository,
                predicateLeafRepository);
        //when
        Map<String, RuleDto> ruleDtoMap = rulesParser.getRulesDto();
        //then
        assertThat(ruleDtoMap.size()).isEqualTo(1);
        assertThat(ruleDtoMap.keySet()).containsExactlyInAnyOrder(ruleDom.getName());
        ruleDtoMap.forEach((k,v) -> assertThat(v).isInstanceOf(RuleDto.class));
    }
}