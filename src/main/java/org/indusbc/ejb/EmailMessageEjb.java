package org.indusbc.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.indusbc.model.EmailMessage;
import org.indusbc.util.IndusConstants;

/**
 *
 * @author root
 */
@Stateless
public class EmailMessageEjb implements EmailMessageEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(EmailMessageEjb.class.getName());
    
    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;

    @Override
    public List<EmailMessage> getAllEmailMessages() {
        TypedQuery<EmailMessage> tQ = em.createQuery("select em from EmailMessage em", EmailMessage.class);
        List<EmailMessage> toReturn=tQ.getResultList();
        LOGGER.info(String.format("EmailMessage count is %d", toReturn.size()));
        return toReturn;
    }

    @Override
    public List<EmailMessage> getAllForRevenuePartyRegister() {
        TypedQuery<EmailMessage> tQ = em.createQuery("select em from EmailMessage em where em.template=?1", EmailMessage.class);
        tQ.setParameter(1, IndusConstants.REVENUE_PARTY_REGISTER);
        List<EmailMessage> toReturn=tQ.getResultList();
        LOGGER.info(String.format("EmailMessage for %s count is %d",IndusConstants.REVENUE_PARTY_REGISTER ,toReturn.size()));
        return toReturn;
    }

    @Override
    public List<EmailMessage> getAllForExpensePartyRegister() {
        TypedQuery<EmailMessage> tQ = em.createQuery("select em from EmailMessage em where em.template=?1", EmailMessage.class);
        tQ.setParameter(1, IndusConstants.EXPENSE_PARTY_REGISTER);
        List<EmailMessage> toReturn=tQ.getResultList();
        LOGGER.info(String.format("EmailMessage for %s count is %d",IndusConstants.EXPENSE_PARTY_REGISTER ,toReturn.size()));
        return toReturn;
    }

    @Override
    public Map<String, String> getAllForRevenuePartyRegisterAsMap() {
        Map<String,String> toReturn = new HashMap<>();
        List<EmailMessage> allForRevenueParty=getAllForRevenuePartyRegister();
        for (EmailMessage em : allForRevenueParty){
            toReturn.put(em.getMessageKey(), em.getMessageText());
        }
        return toReturn;
    }

    @Override
    public Map<String, String> getAllForExpensePartyRegisterAsMap() {
        Map<String,String> toReturn = new HashMap<>();
        List<EmailMessage> allForExpenseParty=getAllForExpensePartyRegister();
        for (EmailMessage em : allForExpenseParty){
            toReturn.put(em.getMessageKey(), em.getMessageText());
        }
        return toReturn;
    }

    @Override
    public List<EmailMessage> getAllForAccessCreated() {
        TypedQuery<EmailMessage> tQ = em.createQuery("select em from EmailMessage em where em.template=?1", EmailMessage.class);
        tQ.setParameter(1, IndusConstants.ACCESS_CREATED);
        List<EmailMessage> toReturn=tQ.getResultList();
        LOGGER.info(String.format("EmailMessage for %s count is %d",IndusConstants.ACCESS_CREATED ,toReturn.size()));
        return toReturn;
    }

    @Override
    public Map<String, String> getAllForAccessCreatedAsMap() {
        Map<String,String> toReturn = new HashMap<>();
        List<EmailMessage> allForAccessCreated=getAllForAccessCreated();
        for (EmailMessage em : allForAccessCreated){
            toReturn.put(em.getMessageKey(), em.getMessageText());
        }
        return toReturn;
    }

    @Override
    public List<EmailMessage> getAllForAccountOverdrawn() {
        TypedQuery<EmailMessage> tQ = em.createQuery("select em from EmailMessage em where em.template=?1", EmailMessage.class);
        tQ.setParameter(1, IndusConstants.ACCOUNT_OVERDRAWN);
        List<EmailMessage> toReturn=tQ.getResultList();
        LOGGER.info(String.format("EmailMessage for %s count is %d",IndusConstants.ACCOUNT_OVERDRAWN ,toReturn.size()));
        return toReturn;
    }

    @Override
    public Map<String, String> getAllForAccountOverdrawnAsMap() {
        Map<String,String> toReturn = new HashMap<>();
        List<EmailMessage> allForAccountOverdrawn=getAllForAccountOverdrawn();
        for (EmailMessage em : allForAccountOverdrawn){
            toReturn.put(em.getMessageKey(), em.getMessageText());
        }
        return toReturn;
    }
}
