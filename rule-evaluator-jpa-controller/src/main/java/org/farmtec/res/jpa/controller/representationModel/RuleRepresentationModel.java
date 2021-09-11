package org.farmtec.res.jpa.controller.representationModel;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.farmtec.res.jpa.model.Action;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

/**
 * Created by dp on 01/03/2021
 */
@Getter @Setter
public class RuleRepresentationModel extends RepresentationModel<RuleRepresentationModel> {
    private long id;
    private String name;
    private int priority;
    private CollectionModel<EntityModel<Action>> actions;
    private String filter;
    private boolean active = true;
    private GroupCompositeRepresentationModel group;
}
