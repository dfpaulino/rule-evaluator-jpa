package org.farmtec.res.jpa.controller.service;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dp on 17/03/2021
 */
@Transactional
@Service
public class PredicateControllerService {

    private final PredicateLeafRepository predicateLeafRepository;

    public PredicateControllerService(PredicateLeafRepository predicateLeafRepository) {
        this.predicateLeafRepository = predicateLeafRepository;
    }


    public List<PredicateLeaf> getAllPredicates() {
        return predicateLeafRepository.findAll();
    }

    public PredicateLeaf getPredicateById(long id) {
        return predicateLeafRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Predicate Not Found"));
    }

    public PredicateLeaf updatePredicate(long id, PredicateLeaf predicateLeaf) {
        //TODO validate predicate
        PredicateLeaf p = predicateLeafRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Predicate Not Found"));
        p.updateFrom(predicateLeaf);
        return p;
    }
}
