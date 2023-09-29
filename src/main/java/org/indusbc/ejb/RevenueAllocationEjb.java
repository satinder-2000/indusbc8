package org.indusbc.ejb;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.indusbc.model.RevenueAllocation;

/**
 *
 * @author user
 */
@Stateless
public class RevenueAllocationEjb implements RevenueAllocationEjbLocal {
  
     private static Logger LOGGER = Logger.getLogger(RevenueAllocationEjb.class.getName());
    
    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;

    @Override
    public List<RevenueAllocation> getAllocationsForYear(int year) {
        TypedQuery<RevenueAllocation> tQ =em.createQuery("select ral from RevenueAllocation ral where ral.year=?1 and ral.allocation >0", RevenueAllocation.class);
        tQ.setParameter(1, year);
        List<RevenueAllocation> revAllocs = tQ.getResultList();
        LOGGER.info(String.format("RevenueAllocation %d extracted for year %d",revAllocs.size(),year));
        return revAllocs;
    }
    
}