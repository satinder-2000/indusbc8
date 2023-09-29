package org.indusbc.ejb;

import javax.ejb.Local;
import java.util.List;
import org.indusbc.model.CentralAccount;

/**
 *
 * @author root
 */
@Local
public interface CentralAccountEjbLocal {
    
    public CentralAccount createAllocation(CentralAccount centralAccount);
    public List<CentralAccount> getAllForYear(int year); 
    public CentralAccount saveAllocation(CentralAccount ca);
    public void removeCentralAccount(CentralAccount ca);
    
}
