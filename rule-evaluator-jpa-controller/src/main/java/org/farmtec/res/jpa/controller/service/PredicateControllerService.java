package org.farmtec.res.jpa.controller.service;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.farmtec.res.jpa.service.utils.RulesValidatorImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dp on 17/03/2021
 */
@Transactional
@Service
public class PredicateControllerService {

    private final PredicateLeafRepository predicateLeafRepository;
    private final RulesValidatorImpl rulesValidator;

    public PredicateControllerService(PredicateLeafRepository predicateLeafRepository, RulesValidatorImpl rulesValidator) {
        this.predicateLeafRepository = predicateLeafRepository;
        this.rulesValidator = rulesValidator;
    }

    public List<PredicateLeaf> getAllPredicates() {
        return predicateLeafRepository.findAll();
    }

    public PredicateLeaf getPredicateById(long id) {
        return predicateLeafRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Predicate Not Found"));
    }

    public PredicateLeaf updatePredicate(long id, PredicateLeaf predicateLeaf) {
        rulesValidator.validatePredicate(Arrays.asList(predicateLeaf));

        PredicateLeaf p = predicateLeafRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Predicate Not Found"));
        p.updateFrom(predicateLeaf);
        return p;
    }
}
