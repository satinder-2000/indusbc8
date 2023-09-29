package org.indusbc.ejb;

import javax.ejb.Singleton;
import java.util.concurrent.Future;

/**
 *
 * @author root
 */
@Singleton
public class AllocationsEjb implements AllocationsEjbLocal {

    @Override
    public Future<String> performAllocations(String allocationJob, String granularity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
