package org.indusbc.ejb;

import javax.ejb.Local;
import javax.mail.MessagingException;
import java.util.List;
import org.indusbc.ejb.exception.UserRegisteredAlreadyException;
import org.indusbc.model.ExpenseAccount;
import org.indusbc.model.ExpenseParty;

/**
 *
 * @author user
 */
@Local
public interface ExpensePartyEjbLocal {
    
    public ExpenseParty createExpenseParty(ExpenseParty expenseParty) throws UserRegisteredAlreadyException, MessagingException;
    public ExpenseParty findById(int id);
    public ExpenseParty updateExpenseParty(ExpenseParty expenseParty);
    public List<ExpenseAccount> findExpenseAccountsOfParty(String email);
    public ExpenseParty addMoreExpenseAccounts(ExpenseParty expenseParty, List<ExpenseAccount> moreExpenseAccounts); 
    public ExpenseParty findByEmail(String email);
    public boolean isEmailRegistered(String email);
}
