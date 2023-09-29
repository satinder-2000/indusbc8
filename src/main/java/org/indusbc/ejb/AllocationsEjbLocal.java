package org.indusbc.ejb;

import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import java.util.concurrent.Future;

/**
 *
 * @author root
 */
@Local
public interface AllocationsEjbLocal {
    
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public Future<String> performAllocations(String allocationJob, String granularity);
    
}
