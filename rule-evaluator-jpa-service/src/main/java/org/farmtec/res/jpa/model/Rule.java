package org.farmtec.res.jpa.model;

import java.util.List;
import lombok.*;

import jakarta.persistence.*;

/**
 * Created by dp on 31/01/2021
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Rule extends BaseTable{
    private String name;
    private int priority;
    private String filter;
    private boolean active = true;
    @OneToOne(cascade = CascadeType.ALL)
    private GroupComposite groupComposite;

    @OneToMany(cascade = CascadeType.ALL,
        orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinTable(name ="rule_to_actions",
        joinColumns = @JoinColumn(name = "rule_id"),
        inverseJoinColumns = @JoinColumn(name = "action_id"))
    private List<Action> actions;
}
