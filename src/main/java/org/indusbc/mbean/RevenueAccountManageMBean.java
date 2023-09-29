package org.indusbc.mbean;

import org.indusbc.ejb.RevenueAccountEjbLocal;
import org.indusbc.model.RevenueAccount;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author user
 */
@Named(value = "revenueAccountManageMBean")
@ViewScoped
public class RevenueAccountManageMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(RevenueAccountManageMBean.class.getName());
    private RevenueAccount revenueAccount;
   
    
    @Inject
    private RevenueAccountEjbLocal revenueAccountEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Integer accountId=Integer.parseInt(request.getParameter("acctId"));
        revenueAccount=revenueAccountEjbLocal.findById(accountId);
        LOGGER.info(String.format("RevenueAccount with ID: %d loaded succeessfully",revenueAccount.getId()));
    }
    
    public double addToBalance(double amount){
        return 0;
    }

    public RevenueAccount getRevenueAccount() {
        return revenueAccount;
    }

    public void setRevenueAccount(RevenueAccount revenueAccount) {
        this.revenueAccount = revenueAccount;
    }
   
}
