package org.farmtec.res.jpa.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dp on 08/03/2021
 */
@Getter @Setter
public class RuleReloadStatus {
    private boolean isSuccess;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;
}
