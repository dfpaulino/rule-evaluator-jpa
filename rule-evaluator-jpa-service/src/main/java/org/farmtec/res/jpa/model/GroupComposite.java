package org.farmtec.res.jpa.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by dp on 30/01/2021
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class GroupComposite extends BaseTable {
    private String logicalOperation;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name ="group_composite_to_group_composite",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    private Set<GroupComposite> groupComposites;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name ="composite_to_predicate",
            joinColumns = @JoinColumn(name = "composite_id"),
            inverseJoinColumns = @JoinColumn(name = "predicate_id"))
    private Set<PredicateLeaf> predicateLeaves;

}
