package org.farmtec.res.jpa.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * Created by dp on 30/01/2021
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class PredicateLeaf extends BaseTable {
    private String type;
    private String operation;
    private String tag;
    @Column(name = "`value`")
    private String value;

    public PredicateLeaf updateFrom(PredicateLeaf p) {
        this.type=p.getType();
        this.setTag(p.getTag());
        this.setOperation(p.getOperation());
        this.setValue(p.value);
        return this;
    }
}
