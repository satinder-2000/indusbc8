package org.indusbc.ejb;

import java.util.List;
import javax.ejb.Local;
import org.indusbc.model.ExpenseAllocation;

/**
 *
 * @author user
 */
@Local
public interface ExpenseAllocationEjbLocal {
    
    public List<ExpenseAllocation> getAllocationsForYear(int year);
    
}
