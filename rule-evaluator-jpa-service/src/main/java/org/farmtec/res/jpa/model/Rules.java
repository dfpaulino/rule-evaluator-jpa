package org.farmtec.res.jpa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by dp on 31/01/2021
 */
@Entity
@Getter @Setter
@NoArgsConstructor
public class Rules extends BaseTable{
    private String name;
    private int priority;
    @OneToOne(cascade = CascadeType.ALL)
    private GroupComposite groupComposite;

}
