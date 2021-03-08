package org.farmtec.res.jpa.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dp on 08/03/2021
 */
@Getter @Setter
public class RuleReloadStatus {
    private boolean isSuccess;
    private Date lastUpdateTime;
}
