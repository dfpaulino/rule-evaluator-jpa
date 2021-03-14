package org.farmtec.res.jpa.controller.exception;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dp on 14/03/2021
 */
@Getter
public class ErrorMessage  implements Serializable {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }


}
