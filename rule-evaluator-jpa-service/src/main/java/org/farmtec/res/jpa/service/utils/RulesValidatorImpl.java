package org.farmtec.res.jpa.service.utils;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.enums.SupportedTypes;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;
import org.farmtec.res.service.exceptions.InvalidOperation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dp on 17/03/2021
 */
@Service
public class RulesValidatorImpl implements RulesValidator{
    public RulesValidatorImpl() {
    }

    public  boolean validateRule(Rule rule) throws InvalidOperation,IllegalArgumentException{
        List<PredicateLeaf> predicateLeaves = getAllPredicates(rule.getGroupComposite());
        return validatePredicate(predicateLeaves);
    }

    public  boolean validateGroup(GroupComposite groupComposite) throws InvalidOperation,IllegalArgumentException{
        List<PredicateLeaf> predicateLeaves = getAllPredicates(groupComposite);
        return validatePredicate(predicateLeaves);
    }

    public  boolean validatePredicate(Collection<PredicateLeaf> predicateLeaves) throws InvalidOperation,IllegalArgumentException {
        predicateLeaves.forEach(RulesValidatorImpl::accept);
        return true;
    }

    public  static List<PredicateLeaf> getAllPredicates(GroupComposite gc) {
        List<PredicateLeaf> predicateLeaves = new ArrayList<>(gc.getPredicateLeaves().stream().collect(Collectors.toList()));
        if (gc.getGroupComposites().size() > 0) {
            gc.getGroupComposites().forEach(g -> predicateLeaves.addAll(getAllPredicates(g)));
        }
        return predicateLeaves;
    }

    private static void accept(PredicateLeaf p) {
        RuleBuilderUtil.RulePredicateBuilder.newInstance()
                .setType(SupportedTypes.getSupportedTypeFrom(p.getType()))
                .setOperation(Operation.fromString(p.getOperation()))
                .setTag(p.getTag())
                .setValue(p.getValue())
                .build();
    }
}
