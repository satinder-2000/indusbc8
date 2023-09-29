package org.indusbc.ejb;

import javax.ejb.Local;
import java.util.List;
import org.indusbc.model.RevenueAllocation;

/**
 *
 * @author root
 */
@Local
public interface RevenueAllocationEjbLocal {
    
    public List<RevenueAllocation> getAllocationsForYear(int year);
    
}
