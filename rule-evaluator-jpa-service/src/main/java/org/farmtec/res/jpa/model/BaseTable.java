package org.farmtec.res.jpa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dp on 30/01/2021
 */
@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public class BaseTable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date createTime;
    private Date updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}
