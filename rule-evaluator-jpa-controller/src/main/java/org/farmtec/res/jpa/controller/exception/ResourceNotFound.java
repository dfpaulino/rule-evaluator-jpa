package org.farmtec.res.jpa.controller.exception;

/**
 * Created by dp on 14/03/2021
 */
public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound(String message) {
        super(message);
    }
}
