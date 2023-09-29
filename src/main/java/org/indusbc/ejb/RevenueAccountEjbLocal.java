package org.indusbc.ejb;

import javax.ejb.Local;
import java.util.List;
import org.indusbc.model.RevenueAccount;
import org.indusbc.model.RevenueAccountTransaction;

/**
 *
 * @author root
 */
@Local
public interface RevenueAccountEjbLocal {
    
    public RevenueAccount createRevenueAccount(RevenueAccount revenueAccount);
    public RevenueAccount findById(int id);
    public RevenueAccount saveRevenueAccount(RevenueAccount revenueAccount);
    public boolean addToBalanceRevenueAccount(int accountId, double balanceToAdd);
    public boolean createMoneyInRevenueAccount(RevenueAccountTransaction revenueAccountTransaction);
    public boolean createMoneyOutRevenueAccount(RevenueAccountTransaction revenueAccountTransaction);
    public List<RevenueAccountTransaction> getRevenueAccountTransactions(int accountId, int year);
    public List<RevenueAccount> getAll(int year);
    public List<RevenueAccount> getAllByCategoryAndYear(String revenueCategory, int year);
    
}
