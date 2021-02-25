package org.farmtec.res.jpa.controller.representationModel;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

/**
 * Created by dp on 23/02/2021
 */
public class GroupCompositeRepresentationModel extends RepresentationModel<GroupCompositeRepresentationModel> {
    private String logicalOperation;
    private CollectionModel<GroupCompositeRepresentationModel> groupCompositeRepresentationModels;
    private  CollectionModel<EntityModel<PredicateLeaf>> predicateRepresentationModels;


    public String getLogicalOperation() {
        return logicalOperation;
    }

    public void setLogicalOperation(String logicalOperation) {
        this.logicalOperation = logicalOperation;
    }

    public CollectionModel<GroupCompositeRepresentationModel> getGroupCompositeRepresentationModels() {
        return groupCompositeRepresentationModels;
    }

    public void setGroupCompositeRepresentationModels(CollectionModel<GroupCompositeRepresentationModel> groupCompositeRepresentationModels) {
        this.groupCompositeRepresentationModels = groupCompositeRepresentationModels;
    }

    public CollectionModel<EntityModel<PredicateLeaf>> getPredicateRepresentationModels() {
        return predicateRepresentationModels;
    }

    public void setPredicateRepresentationModels(CollectionModel<EntityModel<PredicateLeaf>> predicateRepresentationModels) {
        this.predicateRepresentationModels = predicateRepresentationModels;
    }
}
