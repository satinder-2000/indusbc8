package org.indusbc.ejb;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.indusbc.model.CentralAccount;

/**
 *
 * @author user
 */
@Stateless
public class CentralAccountEjb implements CentralAccountEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(CentralAccountEjb.class.getName());
    
    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;


    @Override
    public CentralAccount createAllocation(CentralAccount centralAccount) {
        em.persist(centralAccount);
        em.flush();
        LOGGER.info(String.format("CentralAccount record %d created",centralAccount.getId()));
        return centralAccount;
    }

    @Override
    public List<CentralAccount> getAllForYear(int year) {
        TypedQuery<CentralAccount> tQ = em.createQuery("select ca from CentralAccount ca where ca.year = ?1", CentralAccount.class);
        tQ.setParameter(1, year);
        List<CentralAccount> toReturn=tQ.getResultList();
        LOGGER.info(String.format("CentralAccount record %d extracted",toReturn.size()));
        return toReturn;
    }

    @Override
    public CentralAccount saveAllocation(CentralAccount ca) {
        ca =em.merge(ca);
        em.persist(ca);
        LOGGER.info(String.format("CentralAccount record %d merged",ca.getId()));
        return ca;
    }

    @Override
    public void removeCentralAccount(CentralAccount ca) {
        int caId=ca.getId();
        em.remove(ca);
        em.flush();
        LOGGER.info(String.format("CentralAccount record %d removed",caId));
    }

    
}