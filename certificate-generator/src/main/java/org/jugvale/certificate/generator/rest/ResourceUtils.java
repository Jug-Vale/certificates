package org.jugvale.certificate.generator.rest;

import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

public class ResourceUtils {

    
    public static void exceptionIfPresent(Optional<?> op, String message, Status status) {
        if (op.isPresent()) throw new WebApplicationException(message, status);
    }    
    
    public static <T> T exceptionIfNotPresent(Optional<T> op, String message, Status status) {
        return op.orElseThrow(() -> new WebApplicationException(message, status));
    }
    
    public static void exceptionIfNotNull(Object object, String message, Status status) {
        webApplicationExceptionIfTrue(! Objects.isNull(object), message, status);
    }
    
    public static void exceptionIfNull(Object object, String message, Status status) {
        webApplicationExceptionIfTrue(Objects.isNull(object), message, status);
    }
    
    private static void webApplicationExceptionIfTrue(boolean test, String message, Status status) {
        if(test) {
            throw new WebApplicationException(message, status);
        }
    }
}
