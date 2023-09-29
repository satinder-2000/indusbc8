package org.indusbc.ejb;

import javax.ejb.Local;
import org.indusbc.model.Access;
import org.indusbc.model.ExpenseParty;
import org.indusbc.model.RevenueParty;

/**
 *
 * @author root
 */
@Local
public interface AccessEjbLocal {
    
    static final int MAX_PERMITTED_FAILED_ATTEMPTS =3;
    public Access createAccess(Access access);
    public Access createRevenuePartyAccess(RevenueParty revenueParty);
    public Access updateAccess(Access access);
    public Access lockAccess(Access access);
    public Access unLockAccess(Access access);
    public Access findByEmail(String email);
    public Access findByEmailAndAccessType(String email, String accessType);
    public Access increaseFailedLoginnAttempt(Access access);
    public Access createExpensePartyAccess(ExpenseParty expenseParty);
    
}
