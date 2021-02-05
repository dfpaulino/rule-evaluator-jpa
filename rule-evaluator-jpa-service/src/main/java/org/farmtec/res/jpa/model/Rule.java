package org.farmtec.res.jpa.model;

import lombok.*;

import javax.persistence.*;

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
    @OneToOne(cascade = CascadeType.ALL)
    private GroupComposite groupComposite;

}
