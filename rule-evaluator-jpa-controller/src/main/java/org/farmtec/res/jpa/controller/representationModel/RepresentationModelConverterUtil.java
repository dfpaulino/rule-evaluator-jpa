package org.farmtec.res.jpa.controller.representationModel;

import org.farmtec.res.jpa.controller.GroupCompositeController;
import org.farmtec.res.jpa.controller.PredicateLeafController;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by dp on 23/02/2021
 */
public class RepresentationModelConverterUtil {

    public static EntityModel<PredicateLeaf> fromPredicateLeaf(PredicateLeaf p, long groupId) {
        return EntityModel.of(p,
                linkTo(methodOn(PredicateLeafController.class).getPredicateLeafById(p.getId())).withSelfRel(),
                linkTo(methodOn(GroupCompositeController.class).deletePredicate(groupId,p.getId())).withRel("deletePredicate"));
    }

    public static CollectionModel<EntityModel<PredicateLeaf>> fromPredicateLeaf(Set<PredicateLeaf> predicateLeaves, long groupId) {
        return CollectionModel.of(predicateLeaves.stream().map(p -> RepresentationModelConverterUtil.fromPredicateLeaf(p, groupId))
                .collect(Collectors.toUnmodifiableSet()));
    }

    public static GroupCompositeRepresentationModel fromGroupComposite(GroupComposite groupComposite,long parentGroupId) {

        GroupCompositeRepresentationModel groupCompositeRepresentationModel = new GroupCompositeRepresentationModel();

        groupCompositeRepresentationModel.add(linkTo(methodOn(GroupCompositeController.class).getGroupById(groupComposite.getId())).withSelfRel());
        groupCompositeRepresentationModel.add(linkTo(methodOn(GroupCompositeController.class).deleteChildGroup(parentGroupId,groupComposite.getId())).withRel("deleteChildGroup"));

        groupCompositeRepresentationModel.setLogicalOperation(groupComposite.getLogicalOperation());
        //convert the PredicateLeafs
        if (null != groupComposite.getPredicateLeaves()) {
            groupCompositeRepresentationModel.setPredicateRepresentationModels(
                    RepresentationModelConverterUtil.fromPredicateLeaf(groupComposite.getPredicateLeaves(), groupComposite.getId())
            );
        }
        //convert the nested groupComposits recursively
        if (null != groupComposite.getGroupComposites()) {
            groupCompositeRepresentationModel.setGroupCompositeRepresentationModels(
                    CollectionModel.of(groupComposite.getGroupComposites().stream()
                            .map(g -> RepresentationModelConverterUtil.fromGroupComposite(g,parentGroupId)
                            ).collect(Collectors.toUnmodifiableSet()))
            );
        }
        return groupCompositeRepresentationModel;
    }
}
