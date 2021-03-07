package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.dto.SimpleRuleDto;
import org.farmtec.res.jpa.controller.representationModel.RepresentationModelConverterUtil;
import org.farmtec.res.jpa.controller.representationModel.RuleRepresentationModel;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.jpa.repositories.RulesRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dp on 18/02/2021
 */
@RestController
@RequestMapping("/rules")
@Transactional
public class RuleController {

    private final RulesRepository rulesRepository;

    public RuleController(RulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<SimpleRuleDto>> getAllRules() {

        List<EntityModel<SimpleRuleDto>> simpleRuleDtoModels = rulesRepository.findAll().stream()
                .map(RepresentationModelConverterUtil::ruleToSimpleRuleEntity)
                .collect(Collectors.toList());

        return CollectionModel.of(simpleRuleDtoModels);
    }

    @GetMapping("/{id}")
    public RuleRepresentationModel getRuleById(@PathVariable("id") long id) {
        Rule rule = rulesRepository.findById(id).orElseThrow(() -> new RuntimeException("Rule Not Found"));
        return RepresentationModelConverterUtil.ruleToRuleModel(rule);
    }

    /*
    @GetMapping("/name/{name}")
    public RuleRepresentationModel getRuleByName(@PathVariable("name") String name) {
        Rule rule = rulesRepository.findByName(name).orElseThrow(() -> new RuntimeException("Rule Not Found"));
        return RepresentationModelConverterUtil.ruleToRuleModel(rule);
    }

    @PostMapping
    public CollectionModel<EntityModel<SimpleRuleDto>> addRule(@RequestBody Rule rule) {

    }
    */
    @DeleteMapping("/{id}")
    public void deleteRuleById(@PathVariable("id") long id) {
        rulesRepository.deleteById(id);
    }

}
