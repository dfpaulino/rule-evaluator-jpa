package org.farmtec.res.jpa.parser;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.jpa.parser.util.GroupCompositeMapGroupCompositeDto;
import org.farmtec.res.jpa.parser.util.PredicateLeafMapLeafDto;
import org.farmtec.res.jpa.parser.util.RuleMapRuleDto;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.farmtec.res.jpa.repositories.RulesRepository;
import org.farmtec.res.service.rule.loader.RulesParser;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;
import org.farmtec.res.service.rule.loader.dto.LeafDto;
import org.farmtec.res.service.rule.loader.dto.RuleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dp on 07/02/2021
 */
@Service
public class RulesParserJpaImpl implements RulesParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesParserJpaImpl.class);

    public static final String GROUP_PREFIX = "G_";
    public static final String PREDICATE_PREFIX = "P_";

    private final RulesRepository rulesRepository;
    private final GroupCompositeRepository groupCompositeRepository;
    private final PredicateLeafRepository predicateLeafRepository;

    public RulesParserJpaImpl(RulesRepository rulesRepository,
                              GroupCompositeRepository groupCompositeRepository,
                              PredicateLeafRepository predicateLeafRepository) {
        this.rulesRepository = rulesRepository;
        this.groupCompositeRepository = groupCompositeRepository;
        this.predicateLeafRepository = predicateLeafRepository;
    }


    @Override
    public boolean loadRules() {
        return true;
    }

    @Override
    @Transactional
    public Map<String, LeafDto> getRuleLeafsDto() {
        LOGGER.info("getting predicate leafs");
        return predicateLeafRepository.findAll().stream()
                .collect(
                        Collectors.toMap((p) ->PREDICATE_PREFIX+p.getId(),
                                PredicateLeafMapLeafDto::PredicateLeafToLeafDto)
                );
    }

    @Override
    @Transactional
    public Map<String, GroupCompositeDto> getGroupCompositesDto() {
        LOGGER.info("getting Group Components");
        return  groupCompositeRepository.findAll().stream()
                .collect(
                        Collectors.toMap(
                                (g) -> GROUP_PREFIX+g.getId(),
                                GroupCompositeMapGroupCompositeDto::groupCompositeToGroupCompositeDto
                        )
                );
    }

    @Override
    public Map<String, RuleDto> getRulesDto() {
        LOGGER.info("getting Rules");
        return rulesRepository.findAll().stream()
                .collect(
                        Collectors.toMap(
                                Rule::getName,
                                RuleMapRuleDto::RuleToRuleDto
                        )
                );
    }
}
