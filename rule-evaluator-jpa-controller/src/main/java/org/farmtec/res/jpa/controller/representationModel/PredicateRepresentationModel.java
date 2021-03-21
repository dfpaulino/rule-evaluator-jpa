package org.farmtec.res.jpa.controller.representationModel;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.springframework.hateoas.RepresentationModel;

/**
 * Created by dp on 23/02/2021
 */
public class PredicateRepresentationModel extends RepresentationModel<PredicateRepresentationModel> {
    private PredicateLeaf predicate;

    public PredicateLeaf getPredicate() {
        return predicate;
    }

    public void setPredicate(PredicateLeaf predicate) {
        this.predicate = predicate;
    }
}
