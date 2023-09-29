package org.indusbc.mbean;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author root
 */
@Named(value = "errorMBean")
@RequestScoped
public class ErrorMBean {
    
    public void throwContextNotActiveException(){
        throw new ContextNotActiveException();
    }
    
    
    
}
