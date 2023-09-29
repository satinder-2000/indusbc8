package org.indusbc.mbean;

import org.indusbc.ejb.RevenueCategoryEjbLocal;
import org.indusbc.ejb.RevenuePartyEjbLocal;
import org.indusbc.ejb.exception.UserRegisteredAlreadyException;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.indusbc.ejb.IdentityTypeEjbLocal;
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
    private List<String> revenueCategoriesStr;
    private String[] partyRevenueCategories;
    private String memorableDateStr;
    private List<IdentityType> identityTypes;
    private List<String> identityTypesStr;
    
    
    @Inject
    private RevenueCategoryEjbLocal revenueCategoryEjbLocal;
    @Inject
    private RevenuePartyEjbLocal revenuePartyEjbLocal;
    @Inject
    private IdentityTypeEjbLocal identityTypeEjbLocal;
    
    @PostConstruct
    public void init(){
        revenueParty=new RevenueParty();
        revenueCategories=new ArrayList<>();
        revenueCategoriesStr= new ArrayList<>();
        revenueCategories.addAll(revenueCategoryEjbLocal.getRevenueCategoriesForYear(FinancialYear.financialYear()));
        for (RevenueCategory rc: revenueCategories){
           revenueCategoriesStr.add(rc.getRevenueCategory());
        }
        identityTypes=identityTypeEjbLocal.findAll();
        identityTypesStr=new ArrayList();
        for (IdentityType idT: identityTypes){
            identityTypesStr.add(idT.getIdentityType());
        }
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
        
        //Create RevenuePartyAccounts
        for(String revCat : partyRevenueCategories){
            RevenueAccount ra= new RevenueAccount();
            ra.setRevenueAccountHash(HashGenerator.generateHash(revCat));
            ra.setName(revCat);
            RevenueCategory rc=revenueCategoryEjbLocal.findByNameAndYear(revCat, FinancialYear.financialYear());
            ra.setRevenueCategoryId(rc.getId());
            //Will attach the RevenueParty Id in the EJB, when the ID becomes available.
            if (revenueParty.getRevenueAccounts()==null){
                revenueParty.setRevenueAccounts(new ArrayList<>());
            }
            revenueParty.getRevenueAccounts().add(ra);
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
        try {
            revenueParty = revenuePartyEjbLocal.createRevenueParty(revenueParty);
        } catch (UserRegisteredAlreadyException ex) {
            Logger.getLogger(RevenuePartyRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(RevenuePartyRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOGGER.log(Level.INFO, "Revenue Party persisted with ID: {0} ",revenueParty.getId());
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

    

    public List<String> getRevenueCategoriesStr() {
        return revenueCategoriesStr;
    }

    public void setRevenueCategoriesStr(List<String> revenueCategoriesStr) {
        this.revenueCategoriesStr = revenueCategoriesStr;
    }

    public List<IdentityType> getIdentityTypes() {
        return identityTypes;
    }

    public void setIdentityTypes(List<IdentityType> identityTypes) {
        this.identityTypes = identityTypes;
    }

    public List<String> getIdentityTypesStr() {
        return identityTypesStr;
    }

    public void setIdentityTypesStr(List<String> identityTypesStr) {
        this.identityTypesStr = identityTypesStr;
    }
    
}