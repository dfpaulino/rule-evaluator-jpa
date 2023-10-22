package org.farmtec.res.jpa.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;
import java.util.HashSet;
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
    @NotNull
    private String logicalOperation;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name ="group_composite_to_group_composite",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id"))
    private Set<GroupComposite> groupComposites = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name ="composite_to_predicate",
            joinColumns = @JoinColumn(name = "composite_id"),
            inverseJoinColumns = @JoinColumn(name = "predicate_id"))
    private Set<PredicateLeaf> predicateLeaves = new HashSet<>();

}
