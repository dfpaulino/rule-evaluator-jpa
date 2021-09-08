package org.farmtec.res.jpa.model;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
public class Action extends BaseTable {
  @NotNull
  private String type;
  @NotNull
  private String data;
  @Min(0)
  private int priority = 0;
}
