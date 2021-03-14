package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.PredicateLeafRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by dp on 18/02/2021
 */
@RestController
@RequestMapping("/predicates")
@Relation(collectionRelation = "predicates")
@ExposesResourceFor(PredicateLeaf.class)
public class PredicateLeafController {

    private final PredicateLeafRepository predicateLeafRepository;

    public PredicateLeafController(PredicateLeafRepository predicateLeafRepository) {
        this.predicateLeafRepository = predicateLeafRepository;
    }

    @GetMapping(produces = { "application/hal+json" })
    public CollectionModel<EntityModel<PredicateLeaf>> getAll() {
        List<EntityModel<PredicateLeaf>> predicatesEntityModels = predicateLeafRepository.findAll().stream()
                .map(p ->
                            EntityModel.of(p,
                                linkTo(methodOn(PredicateLeafController.class).getPredicateLeafById(p.getId()))
                                        .withSelfRel(),
                                    linkTo(methodOn(PredicateLeafController.class).getAll())
                                            .withRel("predicates")
                        )
                ).collect(Collectors.toList());

        return CollectionModel.of(predicatesEntityModels,
                linkTo(methodOn(PredicateLeafController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<PredicateLeaf> getPredicateLeafById(@PathVariable("id") long id) {
        PredicateLeaf p = predicateLeafRepository.findById(id).orElseThrow(() -> new RuntimeException("Predicate Not found"));
        return EntityModel.of(p,
                linkTo(methodOn(PredicateLeafController.class).getPredicateLeafById(id)).withSelfRel()
        );
    }
    @PutMapping("/{id}")
    EntityModel<PredicateLeaf> updatePredicate(@RequestBody PredicateLeaf predicateLeaf,@PathVariable("id") long id) {
        PredicateLeaf p = predicateLeafRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Predicate Not found"));
        //TODO create a update method on PredicateLeaf class
        p.setOperation(predicateLeaf.getOperation());
        p.setValue(predicateLeaf.getValue());
        p.setTag(predicateLeaf.getTag());
        p.setType(predicateLeaf.getType());
        //TODO perhaps use merge. we have an Id... so we can consider the object detached state
        PredicateLeaf saved = predicateLeafRepository.save(p);
        return EntityModel.of(saved,
                linkTo(methodOn(PredicateLeafController.class).getPredicateLeafById(saved.getId())).withSelfRel()
        );
    }
}
