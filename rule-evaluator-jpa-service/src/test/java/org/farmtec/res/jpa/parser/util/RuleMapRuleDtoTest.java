package org.farmtec.res.jpa.parser.util;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.service.rule.loader.dto.RuleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.farmtec.res.jpa.parser.RulesParserJpaImpl.GROUP_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by dp on 09/02/2021
 */
class RuleMapRuleDtoTest {

    Rule r1;
    @BeforeEach
    void setUp() {
        GroupComposite gp = new GroupComposite();
        gp.setId(10L);
        r1 = new Rule();
        r1.setName("R1");
        r1.setPriority(1);
        r1.setGroupComposite(gp);
    }

    @Test
    void ruleToRuleDto_whenIsNull() {
        //when
        RuleDto ruleDto = RuleMapRuleDto.RuleToRuleDto(null);
        //then
        assertThat(ruleDto).isNotNull();
        assertThat(ruleDto.isComplete()).isFalse();
    }

    @Test
    void ruleToRuleDto() {
        //when
        RuleDto ruleDto = RuleMapRuleDto.RuleToRuleDto(r1);
        //then
        assertThat(ruleDto).isNotNull();
        assertThat(ruleDto.isComplete()).isTrue();
        assertThat(ruleDto.getPriority()).isEqualTo(r1.getPriority());
        assertThat(ruleDto.getPredicateName()).isEqualTo(GROUP_PREFIX+r1.getGroupComposite().getId());
    }
}