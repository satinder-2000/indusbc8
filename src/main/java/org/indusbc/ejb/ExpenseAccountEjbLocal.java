package org.indusbc.ejb;

import java.util.List;
import javax.ejb.Local;
import org.indusbc.model.ExpenseAccount;
import org.indusbc.model.ExpenseAccountTransaction;

/**
 *
 * @author user
 */
@Local
public interface ExpenseAccountEjbLocal {
    
    public ExpenseAccount createExpenseAccount(ExpenseAccount expenseAccount);
    public ExpenseAccount findById(int id);
    public ExpenseAccount saveExpenseAccount(ExpenseAccount expenseAccount);
    public boolean addToBalanceExpenseAccount(int accountId, double balanceToAdd);
    public boolean withdrawFromBalanceExpenseAccount(int accountId, double balanceToWithdraw);
    public boolean createMoneyOutExpenseAccount(ExpenseAccountTransaction expenseAccountTransaction);
    public boolean createMoneyInExpenseAccount(ExpenseAccountTransaction eat);
    public List<ExpenseAccountTransaction> getExpenseAccountTransactions(int accountId, int year);
    public List<ExpenseAccount> getAll(int year);
    public List<ExpenseAccount> getAllByCategoryAndYear(String expenseCategory, int year);
    
}
