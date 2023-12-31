package org.indusbc.mbean;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author root
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
    
    private ExceptionHandlerFactory exceptionHandlerFactory;

    public CustomExceptionHandlerFactory() {
    }

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }
    
    @Override
    public ExceptionHandler getExceptionHandler() {
        return new CustomExceptionHandler(exceptionHandlerFactory.getExceptionHandler());
    }
    
}
