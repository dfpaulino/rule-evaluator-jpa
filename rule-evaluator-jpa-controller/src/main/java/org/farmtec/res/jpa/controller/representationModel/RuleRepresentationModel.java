package org.farmtec.res.jpa.controller.representationModel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Created by dp on 01/03/2021
 */
@Getter @Setter
public class RuleRepresentationModel extends RepresentationModel<RuleRepresentationModel> {
    long id;
    String name;
    int priority;
    GroupCompositeRepresentationModel group;
}
