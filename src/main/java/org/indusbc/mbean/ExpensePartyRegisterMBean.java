package org.indusbc.mbean;

import org.indusbc.ejb.ExpenseCategoryEjbLocal;
import org.indusbc.ejb.ExpensePartyEjbLocal;
import org.indusbc.model.ExpenseAccount;
import org.indusbc.model.ExpenseCategory;
import org.indusbc.model.ExpenseParty;
import org.indusbc.util.IndusConstants;
import org.indusbc.util.FinancialYear;
import org.indusbc.util.HashGenerator;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import java.util.ResourceBundle;
import org.indusbc.ejb.IdentityTypeEjbLocal;
import org.indusbc.model.IdentityType;

/**
 *
 * @author user
 */
@Named(value = "expensePartyRegisterMBean")
@FlowScoped("ExpensePartyRegister")
public class ExpensePartyRegisterMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(ExpensePartyRegisterMBean.class.getName());
    
    
    private ExpenseParty expenseParty;
    
    private List<ExpenseCategory> expenseCategories;
    private String[] partyExpenseCategories;
    private String memorableDateStr;
    private List<IdentityType> identityTypes;
    
    @Inject
    private ExpenseCategoryEjbLocal expenseCategoryEjbLocal;
    @Inject
    private ExpensePartyEjbLocal expensePartyEjbLocal;
    @Inject
    private IdentityTypeEjbLocal identityTypeEjbLocal;
    
    @PostConstruct
    public void init(){
        expenseParty= new ExpenseParty();
        expenseCategories= new ArrayList<>();
        expenseCategories.addAll(expenseCategoryEjbLocal.getExpenseCategoriesForYear(FinancialYear.financialYear()));
        LOGGER.info(String.format("Total ExpenseCategories for year %d are %d",FinancialYear.financialYear(), expenseCategories.size()));
        identityTypes=identityTypeEjbLocal.findAll();
        LOGGER.info("ExpenseParty initialised.");
    }
    
    public String amendDetails(){
        return "ExpensePartyRegister?faces-redirect=true";
    }
    
    public String validateExpenseParty(){
        String toReturn =null;
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle rb = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
        //Validate name
        if(expenseParty.getName().isEmpty()){
            context.addMessage("name",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name not entered","Name not entered"));
        }else if (expenseParty.getName().length()<2 || expenseParty.getName().length()>45){
            context.addMessage("name",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Chars in Name must be 2 to 45","Chars in Name must be 2 to 45"));
        }
        //Validate Email
        if(expenseParty.getEmail().isEmpty()){
            context.addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email not entered","Email not entered"));
        }else{
            Pattern p = Pattern.compile(IndusConstants.EMAIL_REGEX);
            Matcher m = p.matcher(expenseParty.getEmail());
            if (!m.find()){
                context.addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Email","Invalid Email"));
            }else{
                boolean isEmailRegistered = expensePartyEjbLocal.isEmailRegistered(expenseParty.getEmail());
                if (isEmailRegistered){
                   context.addMessage("email",new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email registered already.","Email registered already."));
                }
            }
        }
        //Memorable Date
        if(!memorableDateStr.isEmpty()){
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            expenseParty.setMemorableDate(LocalDate.parse(memorableDateStr,formatter));
        }
        //Party Hash
        String partyHash=HashGenerator.generateHash(expenseParty.getName().concat(expenseParty.getEmail().concat(expenseParty.getIdentityType()).concat(expenseParty.getIdentityId())));
        expenseParty.setPartyHash(partyHash);
        //Validate Identity Id 
        for (IdentityType dbIdType : identityTypes){
            if (dbIdType.getIdentityType().equals(expenseParty.getIdentityType())){
                Pattern p = Pattern.compile(dbIdType.getRegex());
                Matcher m = p.matcher(expenseParty.getIdentityId());
                if (!m.find()) {
                    FacesContext.getCurrentInstance().addMessage("identityId", new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("Invalid Identity Id"), rb.getString("Invalid Identity Id")));
                } 
            } 
        }
        
        //Direct now
        if(!context.getMessageList().isEmpty()){
            toReturn = null;
        }else{
            toReturn = "ExpensePartyRegisterConfirm?faces-redirect=true";
        }
        return toReturn;
    }
    
    private void submitExpenseParty(){
        try {
            List<String> expensePartyCategoriesList=Arrays.asList(partyExpenseCategories);
            expenseParty = expensePartyEjbLocal.createExpenseParty(expenseParty,expensePartyCategoriesList );
            LOGGER.info(String.format("Expense Party created with ID: %d",expenseParty.getId()));
        } catch (MessagingException ex) {
            Logger.getLogger(ExpensePartyRegisterMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getReturnValue(){
        submitExpenseParty();
        return "/flowreturns/ExpensePartyRegister-return?faces-redirect=true";
    }

    public ExpenseParty getExpenseParty() {
        return expenseParty;
    }

    public void setExpenseParty(ExpenseParty expenseParty) {
        this.expenseParty = expenseParty;
    }

    public List<ExpenseCategory> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    public String[] getPartyExpenseCategories() {
        return partyExpenseCategories;
    }

    public void setPartyExpenseCategories(String[] partyExpenseCategories) {
        this.partyExpenseCategories = partyExpenseCategories;
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