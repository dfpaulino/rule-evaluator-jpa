package org.farmtec.res.jpa.model;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
public class Action extends BaseTable {
  private String type;
  private String data;
  private int priority = 0;
}
