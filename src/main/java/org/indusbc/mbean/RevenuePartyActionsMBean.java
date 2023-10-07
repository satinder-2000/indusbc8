package org.indusbc.mbean;

import org.indusbc.ejb.RevenueAccountEjbLocal;
import org.indusbc.ejb.RevenuePartyEjbLocal;
import org.indusbc.model.Access;
import org.indusbc.model.AccessType;
import org.indusbc.model.RevenueAccount;
import org.indusbc.model.RevenueAccountTransaction;
import org.indusbc.model.RevenueParty;
import org.indusbc.util.IndusConstants;
import org.indusbc.util.FinancialYear;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "revenuePartyActionsMBean")
@SessionScoped
public class RevenuePartyActionsMBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RevenuePartyActionsMBean.class.getName());

    private RevenueParty revenueParty;

    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;

    @Inject
    private RevenueAccountEjbLocal revenueAccountEjbLocal;

    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Access access = (Access) session.getAttribute(IndusConstants.ACCESS);
        assert (access.getAccessType().equals(AccessType.REVENUE_PARTY.toString()));
        revenueParty = revenuePartyEjbLocal.findByEmail(access.getEmail());
        //LOGGER.info(String.format("RevenueParty with ID %d initialised with %d Revenue Accounts", revenueParty.getId(),
        //        revenueParty.getRevenueAccounts().size()));

    }

    public void ajaxTypeListener(AjaxBehaviorEvent event) {
        LOGGER.info("AJAX invoked.");
        /*List<RevenueAccount> partyAccounts = revenueParty.getRevenueAccounts();
        for (RevenueAccount ra : partyAccounts) {
            if (!ra.getMoneyIn().isEmpty() && (new BigDecimal(ra.getMoneyIn()).compareTo(new BigDecimal("0")) == 1)) {
                RevenueAccountTransaction rat = new RevenueAccountTransaction();
                rat.setMoneyIn(ra.getMoneyIn());
                //rat.setYtdBalance(new BigDecimal(rat.getYtdBalance()).add(new BigDecimal(ra.getMoneyIn())).toString());
                ra.setYtdBalance(new BigDecimal(ra.getYtdBalance()).add(new BigDecimal(rat.getMoneyIn())).toString());
                rat.setYtdBalance(ra.getYtdBalance());
                rat.setYear(FinancialYear.financialYear());
                rat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                rat.setRevenueAccountId(ra.getId());
                ra.setMoneyIn("0");
                revenueAccountEjbLocal.createMoneyInRevenueAccount(rat);
                ra=revenueAccountEjbLocal.saveRevenueAccount(ra);
            } else if (!ra.getMoneyOut().isEmpty() && (new BigDecimal(ra.getMoneyOut()).compareTo(new BigDecimal("0")) == 1)) {
                //perform check first - money should be available in the account.
                if (new BigDecimal(ra.getMoneyOut()).compareTo(new BigDecimal(ra.getYtdBalance())) == 1) {
                    FacesContext.getCurrentInstance().addMessage("*", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Withdrawal exceeds the available funds", "Withdrawal exceeds the available funds"));
                } else {
                    RevenueAccountTransaction rat = new RevenueAccountTransaction();
                    rat.setMoneyOut(ra.getMoneyOut());
                    //rat.setYtdBalance(new BigDecimal(rat.getYtdBalance()).subtract(new BigDecimal(ra.getMoneyOut())).toString());
                    ra.setYtdBalance(new BigDecimal(ra.getYtdBalance()).subtract(new BigDecimal(ra.getMoneyOut())).toString());
                    rat.setYtdBalance(ra.getYtdBalance());
                    rat.setYear(FinancialYear.financialYear());
                    rat.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                    rat.setRevenueAccountId(ra.getId());
                    ra.setMoneyOut("0");
                    revenueAccountEjbLocal.createMoneyOutRevenueAccount(rat);
                    ra=revenueAccountEjbLocal.saveRevenueAccount(ra);

                }

            }

        }*/
    }

    public RevenueParty getRevenueParty() {
        return revenueParty;
    }

    public void setRevenueParty(RevenueParty revenueParty) {
        this.revenueParty = revenueParty;
    }

}
