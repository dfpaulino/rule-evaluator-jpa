package org.farmtec.res.jpa.utils;

import org.farmtec.res.enums.Operation;
import org.farmtec.res.enums.SupportedTypes;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.service.builder.utils.RuleBuilderUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dp on 17/03/2021
 */
public class RulesValidator {
    public RulesValidator() {
    }

    public  static boolean validateRule(Rule rule) {
        List<PredicateLeaf> predicateLeaves = getAllPredicates(rule.getGroupComposite());
        return validatePredicate(predicateLeaves);
    }

    public  static boolean validateGroup(GroupComposite groupComposite) {
        List<PredicateLeaf> predicateLeaves = getAllPredicates(groupComposite);
        return validatePredicate(predicateLeaves);
    }

    public  static boolean validatePredicate(Collection<PredicateLeaf> predicateLeaves) {
        predicateLeaves.forEach(RulesValidator::accept);
        return true;
    }

    public  static List<PredicateLeaf> getAllPredicates(GroupComposite gc) {
        List<PredicateLeaf> predicateLeaves = new ArrayList<>();
        predicateLeaves.addAll(gc.getPredicateLeaves().stream().collect(Collectors.toList()));
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
