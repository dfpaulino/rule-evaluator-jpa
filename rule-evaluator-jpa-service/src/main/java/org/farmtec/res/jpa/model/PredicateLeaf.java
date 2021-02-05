package org.farmtec.res.jpa.model;

import lombok.*;

import javax.persistence.Entity;

/**
 * Created by dp on 30/01/2021
 */
@Entity
@NoArgsConstructor
@Getter @Setter
@ToString
@EqualsAndHashCode
public class PredicateLeaf extends BaseTable {
    private String type;
    private String operation;
    private String tag;
    private String value;
}
