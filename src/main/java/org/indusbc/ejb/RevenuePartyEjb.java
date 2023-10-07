package org.indusbc.ejb;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.indusbc.model.Access;
import org.indusbc.model.RevenueAccount;
import org.indusbc.model.RevenueCategory;
import org.indusbc.model.RevenueParty;
import org.indusbc.util.FinancialYear;
import org.indusbc.util.HashGenerator;

/**
 *
 * @author user
 */
@Stateless
public class RevenuePartyEjb implements RevenuePartyEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(RevenuePartyEjb.class.getName());

    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;
    
    @Inject
    private RevenueCategoryEjbLocal revenueCategoryEjbLocal;
    @Inject
    RevenueAccountEjbLocal revenueAccountEjbLocal;
    
    @Inject
    EmailerEjbLocal emailerEjbLocal;
    
    @Inject
    private AccessEjbLocal accessEjbLocal;

    @Override
    public RevenueParty createRevenueParty(RevenueParty revenueParty, List<String> partyRevenueCategoriesList) throws MessagingException {
        em.persist(revenueParty);
        em.flush();
        //Create RevenuePartyAccounts
        for (String revCat : partyRevenueCategoriesList) {
            RevenueAccount ra = new RevenueAccount();
            ra.setRevenueAccountHash(HashGenerator.generateHash(revCat));
            ra.setName(revCat);
            RevenueCategory rc = revenueCategoryEjbLocal.findByNameAndYear(revCat, FinancialYear.financialYear());
            ra.setRevenueCategoryId(rc.getId());
            ra.setRevenuePartyId(revenueParty.getId());
            ra = revenueAccountEjbLocal.createRevenueAccount(ra);
            LOGGER.log(Level.INFO, "Revenue Account persisted with ID: {0} ", ra.getId());
        }
        //Create Acccess record now.
        Access access = accessEjbLocal.createRevenuePartyAccess(revenueParty);
        LOGGER.info(String.format("Access ID for the Revenue Party %d is %d", revenueParty.getId(), access.getId()));
        return revenueParty;
    }

    @Override
    public RevenueParty findById(int id) {
        return em.find(RevenueParty.class, id);
    }
    
    @Override
    public boolean isEmailRegistered(String email) {
        TypedQuery<RevenueParty> tQ=em.createQuery("select rp from RevenueParty rp where rp.email=?1", RevenueParty.class);
        tQ.setParameter(1, email);
        try{
            tQ.getSingleResult();
            return true;
        }catch(NoResultException ex){
            //good for us
            return false;
        }
    }

    @Override
    public RevenueParty updateRevenueParty(RevenueParty revenueParty) {
        String partyHash=HashGenerator.generateHash(revenueParty.getName().concat(revenueParty.getEmail()).concat(revenueParty.getIdentityType()).concat(revenueParty.getIdentityType()));
        revenueParty.setPartyHash(partyHash);
        em.merge(revenueParty);
        LOGGER.info(String.format("Revenue Party with ID: %d updated",revenueParty.getId()));
        return revenueParty;
    }

    @Override
    public List<RevenueAccount> findRevenueAccountsOfParty(String email) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public RevenueParty addMoreRevenueAccounts(RevenueParty revenueParty, List<RevenueAccount> moreRevenueAccounts) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public RevenueParty findByEmail(String email) {
        TypedQuery<RevenueParty> tQ=em.createQuery("select rp from RevenueParty rp where rp.email=?1", RevenueParty.class);
        tQ.setParameter(1, email);
        return tQ.getSingleResult();
    }
}
