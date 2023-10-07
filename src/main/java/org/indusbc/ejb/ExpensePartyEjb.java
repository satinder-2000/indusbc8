package org.indusbc.ejb;

import java.sql.Timestamp;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.indusbc.ejb.exception.UserRegisteredAlreadyException;
import org.indusbc.model.Access;
import org.indusbc.model.ExpenseAccount;
import org.indusbc.model.ExpenseCategory;
import org.indusbc.model.ExpenseParty;
import org.indusbc.model.RevenueAccount;
import org.indusbc.model.RevenueCategory;
import org.indusbc.util.FinancialYear;
import org.indusbc.util.HashGenerator;

/**
 *
 * @author user
 */
@Stateless
public class ExpensePartyEjb implements ExpensePartyEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(ExpensePartyEjb.class.getName());

    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;
    @Inject
    private ExpenseCategoryEjbLocal expenseCategoryEjbLocal;
    @Inject
    ExpenseAccountEjbLocal expenseAccountEjbLocal;
    @Inject
    EmailerEjbLocal emailerEjbLocal;
    @Inject
    private AccessEjbLocal accessEjbLocal;
    
    
    
    @Override
    public ExpenseParty findById(int id) {
        //return em.find(ExpenseParty.class, id);
        return em.find(ExpenseParty.class, id);
    }

    @Override
    public ExpenseParty createExpenseParty(ExpenseParty expenseParty, List<String> partyExpenseCategoriesList) throws MessagingException {
        em.persist(expenseParty);
        em.flush();
        LOGGER.info(String.format("Expense Party created with ID: %d",expenseParty.getId()));
        //Create RevenuePartyAccounts
        for (String expCat : partyExpenseCategoriesList) {
            ExpenseAccount ea = new ExpenseAccount();
            ea.setExpenseAccountHash(HashGenerator.generateHash(expCat));
            ea.setName(expCat);
            ExpenseCategory ec = expenseCategoryEjbLocal.findByNameAndYear(expCat, FinancialYear.financialYear());
            ea.setExpenseCategoryId(ec.getId());
            ea.setExpensePartyId(expenseParty.getId());
            ea = expenseAccountEjbLocal.createExpenseAccount(ea);
            LOGGER.log(Level.INFO, "Expense Account persisted with ID: {0} ", ea.getId());
        }
        //Create Acccess record now.
        Access access= accessEjbLocal.createExpensePartyAccess(expenseParty);
        LOGGER.info(String.format("Access ID for the Expense Party %d is %d",expenseParty.getId(),access.getId()));
        return expenseParty;
    }

    @Override
    public ExpenseParty updateExpenseParty(ExpenseParty expenseParty) {
        String partyHash=HashGenerator.generateHash(expenseParty.getName().concat(expenseParty.getEmail()).concat(expenseParty.getIdentityType()).concat(expenseParty.getIdentityId()));
        expenseParty.setPartyHash(partyHash);
        em.merge(expenseParty);
        em.flush();
        LOGGER.info(String.format("Expense Party with ID: %d updated",expenseParty.getId()));
        return expenseParty;
        
    }

    @Override
    public List<ExpenseAccount> findExpenseAccountsOfParty(String email) {
        LOGGER.warning("Method findExpenseAccountsOfParty is umimplemented");
        /*pedQuery<ExpenseParty> tQ = em.createQuery("select ep from ExpenseParty ep where ep.email =?1", ExpenseParty.class);
        tQ.setParameter(1, email);
        ExpenseParty ep = tQ.getSingleResult();
        List<ExpenseAccount> expenseAccounts= ep.getExpenseAccounts();
        LOGGER.info(String.format("Expense Party %s had %d Expense Accounts",email, expenseAccounts.size()));*/
        return null;
    }

    @Override
    public ExpenseParty addMoreExpenseAccounts(ExpenseParty expenseParty, List<ExpenseAccount> moreExpenseAccounts) {
        LOGGER.warning("Method addMoreExpenseAccounts is umimplemented");
       /* expenseParty.getExpenseAccounts().addAll(moreExpenseAccounts);
        for(ExpenseAccount ea: moreExpenseAccounts){
            ea.setExpensePartyId(expenseParty.getId());
            em.persist(ea);
        }
        em.persist(expenseParty);*/
       return expenseParty;
    }

    @Override
    public ExpenseParty findByEmail(String email) {
        TypedQuery<ExpenseParty> tQ=em.createQuery("select ep from ExpenseParty ep where ep.email=?1", ExpenseParty.class);
        tQ.setParameter(1, email);
        return tQ.getSingleResult();
    }

    @Override
    public boolean isEmailRegistered(String email) {
        TypedQuery<ExpenseParty> tQ=em.createQuery("select ep from ExpenseParty ep where ep.email=?1", ExpenseParty.class);
        tQ.setParameter(1, email);
        try{
            tQ.getSingleResult();
            return true;
        }catch(NoResultException ex){
            //good for us
            return false;
        }
    }
}
