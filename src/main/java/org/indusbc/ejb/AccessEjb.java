package org.indusbc.ejb;

import javax.ejb.Stateless;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import org.indusbc.model.Access;
import org.indusbc.model.AccessType;
import org.indusbc.model.ExpenseParty;
import org.indusbc.model.RevenueParty;

/**
 *
 * @author user
 */
@Stateless
public class AccessEjb implements AccessEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(AccessEjb.class.getName());
    
    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;
    
    @Inject
    private EmailerEjbLocal emailerEjbLocal;

    @Override
    public Access createAccess(Access access) {
        em.persist(access);
        LOGGER.info(String.format("Access record persisted with ID: {1}"+access.getId()));
        return access;
    }
    
    @Override
    public Access createRevenuePartyAccess(RevenueParty revenueParty) {
        Access access =new Access();
        access.setAccessType(AccessType.REVENUE_PARTY.toString());
        access.setAccountLocked(false);
        access.setEmail(revenueParty.getEmail());
        access.setFailedAttempts(0);
        em.persist(access);
        em.flush();
        LOGGER.info(String.format("Access record for Revenue Party {1} persisted with ID: {2} ",revenueParty.getId(),access.getId()));
        return access;
    }
    
    @Override
    public Access createExpensePartyAccess(ExpenseParty expenseParty) {
        Access access =new Access();
        access.setAccessType(AccessType.EXPENSE_PARTY.toString());
        access.setAccountLocked(false);
        access.setEmail(expenseParty.getEmail());
        access.setFailedAttempts(0);
        em.persist(access);
        em.flush();
        LOGGER.info(String.format("Access record for Expense Party {1} persisted with ID: {2} ",expenseParty.getId(),access.getId()));
        return access;
    }

    @Override
    public Access updateAccess(Access access) {
        access = em.merge(access);
        em.flush();
        LOGGER.info(String.format("Access record updated with ID: {1}"+access.getId()));
        return access;
    }

    @Override
    public Access lockAccess(Access access) {
        access.setFailedAttempts(MAX_PERMITTED_FAILED_ATTEMPTS);
        access.setAccountLocked(true);
        access.setLockTime(LocalDateTime.now());
        return updateAccess(access);
    }

    @Override
    public Access unLockAccess(Access access) {
        access.setFailedAttempts(0);
        access.setAccountLocked(false);
        return updateAccess(access);
        
    }

    @Override
    public Access findByEmail(String email) {
        TypedQuery<Access> tQ=em.createQuery("select a from Access a where a.email=?1", Access.class);
        tQ.setParameter(1, email);
        try{
            return tQ.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }

    @Override
    public Access increaseFailedLoginnAttempt(Access access) {
        access.setFailedAttempts(access.getFailedAttempts()+1);
        if(access.getFailedAttempts()==MAX_PERMITTED_FAILED_ATTEMPTS);
        return lockAccess(access);
    }

    @Override
    public Access findByEmailAndAccessType(String email, String accessType) {
        TypedQuery<Access> tQ=em.createQuery("select a from Access a where a.email=?1 and a.accessType=?2", Access.class);
        tQ.setParameter(1, email);
        tQ.setParameter(2, accessType);
        try{
            return tQ.getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }

    

    
}
