package org.farmtec.res.jpa.service.utils;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.model.Rule;
import org.farmtec.res.service.exceptions.InvalidOperation;

import java.util.Collection;

/**
 * Created by dp on 20/03/2021
 */
public interface RulesValidator {

    boolean validateRule(Rule rule) throws InvalidOperation, IllegalArgumentException;

    boolean validateGroup(GroupComposite groupComposite) throws InvalidOperation, IllegalArgumentException;

    boolean validatePredicate(Collection<PredicateLeaf> predicateLeaves) throws InvalidOperation, IllegalArgumentException;

}