package org.indusbc.mbean;

import org.indusbc.ejb.ExpenseAccountEjbLocal;
import org.indusbc.ejb.ExpensePartyEjbLocal;
import org.indusbc.model.Access;
import org.indusbc.model.AccessType;
import org.indusbc.model.ExpenseAccount;
import org.indusbc.model.ExpenseAccountTransaction;
import org.indusbc.model.ExpenseParty;
import org.indusbc.util.IndusConstants;
import org.indusbc.util.FinancialYear;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "expensePartyActionsMBean")
@ViewScoped
public class ExpensePartyActionsMBean implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(ExpensePartyActionsMBean.class.getName());
    
    private ExpenseParty expenseParty;
    
    @Inject
    private ExpensePartyEjbLocal expensePartyEjbLocal;
    
    @Inject
    private ExpenseAccountEjbLocal expenseAccountEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Access access = (Access) session.getAttribute(IndusConstants.ACCESS);
        assert(access.getAccessType().equals(AccessType.EXPENSE_PARTY));
        expenseParty = expensePartyEjbLocal.findByEmail(access.getEmail());
        LOGGER.info(String.format("ExpenseParty with ID %d initialise with %d Expense Accounts",expenseParty.getId(),
                            expenseParty.getExpenseAccounts().size()));
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent event) {
        List<ExpenseAccount> partyAccounts = expenseParty.getExpenseAccounts();
        for (ExpenseAccount ea : partyAccounts) {
            if (!ea.getMoneyIn().isEmpty() && (new BigDecimal(ea.getMoneyIn()).compareTo(new BigDecimal("0")) == 1)) {
                ExpenseAccountTransaction eat = new ExpenseAccountTransaction();
                eat.setMoneyIn(ea.getMoneyIn());
                eat.setYtdBalance(new BigDecimal(eat.getYtdBalance()).add(new BigDecimal(ea.getMoneyIn())).toString());
                ea.setYtdBalance(new BigDecimal(ea.getYtdBalance()).add(new BigDecimal(eat.getYtdBalance())).toString());
                eat.setYear(FinancialYear.financialYear());
                eat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                eat.setExpenseAccountId(ea.getId());
                ea.setMoneyIn("0");
                expenseAccountEjbLocal.createMoneyInExpenseAccount(eat);
                ea=expenseAccountEjbLocal.saveExpenseAccount(ea);
            } else if (!ea.getMoneyOut().isEmpty() && (new BigDecimal(ea.getMoneyOut()).compareTo(new BigDecimal("0")) == 1)) {
                //perform check first - money should be available in the account.
                if (new BigDecimal(ea.getMoneyOut()).compareTo(new BigDecimal(ea.getYtdBalance())) == 1) {
                    FacesContext.getCurrentInstance().addMessage("*", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Withdrawal exceeds the available funds", "Withdrawal exceeds the available funds"));
                } else {
                    ExpenseAccountTransaction eat = new ExpenseAccountTransaction();
                    eat.setMoneyOut(ea.getMoneyOut());
                    //eat.setYtdBalance(new BigDecimal(eat.getYtdBalance()).subtract(new BigDecimal(ea.getMoneyOut())).toString());
                    ea.setYtdBalance(new BigDecimal(ea.getYtdBalance()).subtract(new BigDecimal(ea.getMoneyOut())).toString());
                    eat.setYtdBalance(ea.getYtdBalance());
                    eat.setYear(FinancialYear.financialYear());
                    eat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                    eat.setExpenseAccountId(ea.getId());
                    ea.setMoneyOut("0");
                    expenseAccountEjbLocal.createMoneyOutExpenseAccount(eat);
                    ea=expenseAccountEjbLocal.saveExpenseAccount(ea);

                }

            }
        }
    }

    public ExpenseParty getExpenseParty() {
        return expenseParty;
    }

    public void setExpenseParty(ExpenseParty expenseParty) {
        this.expenseParty = expenseParty;
    }
    
    
    
    

    
}