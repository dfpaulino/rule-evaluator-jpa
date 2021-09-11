package org.farmtec.res.jpa.controller;

import javax.validation.Valid;
import org.farmtec.res.jpa.controller.dto.SimpleRuleDto;
import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.controller.representationModel.RepresentationModelConverterUtil;
import org.farmtec.res.jpa.controller.representationModel.RuleRepresentationModel;
import org.farmtec.res.jpa.model.Action;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.jpa.repositories.RulesRepository;
import org.farmtec.res.jpa.service.utils.RulesValidator;

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
    private final RulesValidator rulesValidator;

    public RuleController(RulesRepository rulesRepository, RulesValidator rulesValidator) {
        this.rulesRepository = rulesRepository;
        this.rulesValidator = rulesValidator;
    }

    @GetMapping
    public CollectionModel<EntityModel<SimpleRuleDto>> getAllRules() {
        List<EntityModel<SimpleRuleDto>> simpleRuleDtoModels = rulesRepository.findAll().stream()
                .map(RepresentationModelConverterUtil::ruleToSimpleRuleEntity)
                .collect(Collectors.toList());

        return CollectionModel.of(simpleRuleDtoModels);
    }

    @GetMapping("/filter")
    public CollectionModel<EntityModel<SimpleRuleDto>> getRulesByFilter(
        @RequestParam("filter") String filter) {
        List<EntityModel<SimpleRuleDto>> simpleRuleDtoModels = rulesRepository.findAll().stream()
            .filter(r -> r.getFilter()!=null?r.getFilter().equalsIgnoreCase(filter):false)
            .map(RepresentationModelConverterUtil::ruleToSimpleRuleEntity)
            .collect(Collectors.toList());

        return CollectionModel.of(simpleRuleDtoModels);
    }

    @GetMapping("/{id}")
    public RuleRepresentationModel getRuleById(@PathVariable("id") long id) {
        Rule rule = rulesRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Rule Not Found"));
        return RepresentationModelConverterUtil.ruleToRuleModel(rule);
    }

    /*
    @GetMapping("/name/{name}")
    public RuleRepresentationModel getRuleByName(@PathVariable("name") String name) {
        Rule rule = rulesRepository.findByName(name).orElseThrow(() -> new RuntimeException("Rule Not Found"));
        return RepresentationModelConverterUtil.ruleToRuleModel(rule);
    }
    */
    //TODO add validation of payload
    @PostMapping
    public RuleRepresentationModel addRule(@RequestBody Rule rule) {
        rulesValidator.validateRule(rule);
        Rule saved = rulesRepository.save(rule);
        return RepresentationModelConverterUtil.ruleToRuleModel(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteRuleById(@PathVariable("id") long id) {
        rulesRepository.deleteById(id);
    }

    // TODO tests
    @DeleteMapping("/{id}/action/{actionId}")
    @Transactional
    public RuleRepresentationModel deleteActionId(@PathVariable("id") long id,@PathVariable("actionId") long actionId) {
        Rule rule = rulesRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Rule Not Found"));
        rule.getActions().removeIf(action -> action.getId()==actionId);
        return RepresentationModelConverterUtil.ruleToRuleModel(rule);
    }

    @PutMapping("/{id}/action")
    @Transactional
    public RuleRepresentationModel addActionToRule(@PathVariable("id") long id,
        @Valid @RequestBody Action action) {
        Rule rule = rulesRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Rule Not Found"));
        rule.getActions().add(action);
        return RepresentationModelConverterUtil.ruleToRuleModel(rule);
    }

    @PutMapping("/{id}/activate")
    @Transactional
    public void activateRule(@PathVariable("id") long id) {
        Rule rule = rulesRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Rule Not Found"));
        rule.setActive(true);
    }

    @PutMapping("/{id}/deactivate")
    @Transactional
    public void deactivateRule(@PathVariable("id") long id) {
        Rule rule = rulesRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Rule Not Found"));
        rule.setActive(false);
    }


}
