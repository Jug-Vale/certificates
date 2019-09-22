package org.jugvale.certificate.generator.rest.conf;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

@Provider
@ApplicationScoped
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    Logger logger = Logger.getLogger(WebApplicationExceptionMapper.class);
    
    @Override
    public Response toResponse(WebApplicationException exception) {
        Response response = exception.getResponse();
        logger.warnv("Caught WebApplicationException, returning status {0} with message {1}", 
                     response.getStatus(),
                     exception.getMessage());
        logger.debug("Logging WebApplicationException", exception);
        return response;
    }

}
