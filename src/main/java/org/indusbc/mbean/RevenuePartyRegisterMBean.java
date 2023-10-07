package org.indusbc.mbean;

import org.indusbc.ejb.RevenueCategoryEjbLocal;
import org.indusbc.ejb.RevenuePartyEjbLocal;
import org.indusbc.model.RevenueAccount;
import org.indusbc.model.RevenueCategory;
import org.indusbc.model.RevenueParty;
import org.indusbc.util.IndusConstants;
import org.indusbc.util.FinancialYear;
import org.indusbc.util.HashGenerator;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.indusbc.ejb.IdentityTypeEjbLocal;
import org.indusbc.ejb.RevenueAccountEjbLocal;
import org.indusbc.model.IdentityType;

/**
 *
 * @author user
 */
@Named(value = "revenuePartyRegisterMBean")
@FlowScoped("RevenuePartyRegister")
public class RevenuePartyRegisterMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(RevenuePartyRegisterMBean.class.getName());
    private RevenueParty revenueParty;
    private List<RevenueCategory> revenueCategories;
    private String[] partyRevenueCategories;
    private String memorableDateStr;
    private List<IdentityType> identityTypes;
    
    
    @Inject
    private RevenueCategoryEjbLocal revenueCategoryEjbLocal;
    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;
    @Inject
    private RevenueAccountEjbLocal revenueAccountEjbLocal;
    @Inject
    private IdentityTypeEjbLocal identityTypeEjbLocal;
    
    @PostConstruct
    public void init(){
        revenueParty=new RevenueParty();
        revenueCategories=new ArrayList<>();
        revenueCategories.addAll(revenueCategoryEjbLocal.getRevenueCategoriesForYear(FinancialYear.financialYear()));
        LOGGER.info(String.format("Total RevenueCategories for year %d are %d",FinancialYear.financialYear(), revenueCategories.size()));
        identityTypes=identityTypeEjbLocal.findAll();
        LOGGER.info("New Revenue Party initialised");
    }
    
    public String amendDetails(){
        return "RevenuePartyRegister?faces-redirect=true";
    }
    
    public String validateRevenueParty() {
        String toReturn = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Name
        if(revenueParty.getName().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("nameRequired"), rb.getString("nameRequired"))); 
        }else if(revenueParty.getName().length()<2 || revenueParty.getName().length()>45){
            FacesContext.getCurrentInstance().addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("nameCharsLimit"), rb.getString("nameCharsLimit")));
        }
        //Validate email if Exists
        if (revenueParty.getEmail().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailRequired"), rb.getString("emailRequired")));
        } else {//Email Regex validation
            Pattern p = Pattern.compile(IndusConstants.EMAIL_REGEX);
            Matcher m = p.matcher(revenueParty.getEmail());
            if (!m.find()) {
                FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailInvalid"), rb.getString("emailInvalid")));
            } else {
                boolean isEmailRegistered = revenuePartyEjbLocal.isEmailRegistered(revenueParty.getEmail());
                if (isEmailRegistered) {
                    FacesContext.getCurrentInstance().addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("emailTaken"), rb.getString("emailTaken")));
                }
            
            }

        }
        
        //Memorable Date now
        if (!memorableDateStr.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            revenueParty.setMemorableDate(LocalDate.parse(memorableDateStr, formatter));
        }
        
        //finally create PartyHash
        String partyHash=HashGenerator.generateHash(revenueParty.getName().concat(revenueParty.getEmail()).concat(revenueParty.getIdentityType()).concat(revenueParty.getIdentityId()));
	revenueParty.setPartyHash(partyHash);
        
        //Validate Identity Id 
        for (IdentityType dbIdType : identityTypes){
            if (dbIdType.getIdentityType().equals(revenueParty.getIdentityType())){
                Pattern p = Pattern.compile(dbIdType.getRegex());
                Matcher m = p.matcher(revenueParty.getIdentityId());
                if (!m.find()) {
                    FacesContext.getCurrentInstance().addMessage("identityId", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("Invalid Identity Id"), rb.getString("Invalid Identity Id")));
                } 
            } 
        }
        
        if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()){
            toReturn =null; //generate same page with errors
        }else{
            toReturn = "RevenuePartyRegisterConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    public void submitRevenueParty(){
        
        List<String> partyRevenueCategoriesList= Arrays.asList(partyRevenueCategories);
        
        
        try {
            revenueParty = revenuePartyEjbLocal.createRevenueParty(revenueParty,partyRevenueCategoriesList);
            LOGGER.log(Level.INFO, "Revenue Party persisted with ID: {0} ",revenueParty.getId());
        } catch (MessagingException ex) {
            Logger.getLogger(RevenuePartyRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getReturnValue() {
        submitRevenueParty();
        return "/flowreturns/RevenuePartyRegister-return?faces-redirect=true";
    }

    public List<RevenueCategory> getRevenueCategories() {
        return revenueCategories;
    }

    public void setRevenueCategories(List<RevenueCategory> revenueCategories) {
        this.revenueCategories = revenueCategories;
    }

    public RevenueParty getRevenueParty() {
        return revenueParty;
    }

    public void setRevenueParty(RevenueParty revenueParty) {
        this.revenueParty = revenueParty;
    }

    public String[] getPartyRevenueCategories() {
        return partyRevenueCategories;
    }

    public void setPartyRevenueCategories(String[] partyRevenueCategories) {
        this.partyRevenueCategories = partyRevenueCategories;
    }

    public String getMemorableDateStr() {
        return memorableDateStr;
    }

    public void setMemorableDateStr(String memorableDateStr) {
        this.memorableDateStr = memorableDateStr;
    }

    public List<IdentityType> getIdentityTypes() {
        return identityTypes;
    }

    public void setIdentityTypes(List<IdentityType> identityTypes) {
        this.identityTypes = identityTypes;
    }

}