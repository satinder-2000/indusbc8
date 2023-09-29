package org.indusbc.ejb;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.indusbc.model.ExpenseAllocation;

/**
 *
 * @author user
 */
@Stateless
public class ExpenseAllocationEjb implements ExpenseAllocationEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ExpenseAllocationEjb.class.getName());
    
    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;

    @Override
    public List<ExpenseAllocation> getAllocationsForYear(int year) {
        TypedQuery<ExpenseAllocation> tQ =em.createQuery("select eal from ExpenseAllocation eal where eal.year=?1", ExpenseAllocation.class);
        tQ.setParameter(1, year);
        List<ExpenseAllocation> expAllocs = tQ.getResultList();
        LOGGER.info(String.format("ExpenseAllocation %d extracted for year %d",expAllocs.size(),year));
        return expAllocs;
    }
}
