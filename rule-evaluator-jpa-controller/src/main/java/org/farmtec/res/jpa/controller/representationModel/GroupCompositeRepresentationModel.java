package org.farmtec.res.jpa.controller.representationModel;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

/**
 * Created by dp on 23/02/2021
 */
public class GroupCompositeRepresentationModel extends RepresentationModel<GroupCompositeRepresentationModel> {
    private String logicalOperation;
    private CollectionModel<GroupCompositeRepresentationModel> groups;
    private  CollectionModel<EntityModel<PredicateLeaf>> predicates;


    public String getLogicalOperation() {
        return logicalOperation;
    }

    public void setLogicalOperation(String logicalOperation) {
        this.logicalOperation = logicalOperation;
    }

    public CollectionModel<GroupCompositeRepresentationModel> getGroups() {
        return groups;
    }

    public void setGroups(CollectionModel<GroupCompositeRepresentationModel> groups) {
        this.groups = groups;
    }

    public CollectionModel<EntityModel<PredicateLeaf>> getPredicates() {
        return predicates;
    }

    public void setPredicates(CollectionModel<EntityModel<PredicateLeaf>> predicates) {
        this.predicates = predicates;
    }
}
