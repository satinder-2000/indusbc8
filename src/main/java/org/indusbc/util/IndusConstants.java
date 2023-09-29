package org.indusbc.util;

/**
 *
 * @author root
 */
public interface IndusConstants {
    
    public static String EMAIL_REGEX="^(.+)@(.+)$";
    public static String PW_REGEX="^(?=.*\\d).{8,14}$";
    public static String ACCESS="access";
    public static String LOGGED_IN_EMAIL="loggedInEmai";
    public static String EXPENSE_PARTY_REGISTER="ExpensePartyRegister";
    public static String REVENUE_PARTY_REGISTER="RevenuePartyRegister";
    public static String ACCESS_CREATED="AccessCreated";
    public static String ACCOUNT_OVERDRAWN="AccountOverdrawn";
    //Constants used while sending Registration Confirm emails to ExpenseParty and RevenueParty
    public static String EMAIL_SUBJECT="SUBJECT";
    public static String EMAIL_SALUTE="SALUTE";//This is used in Access confirmation email as well
    public static String EMAIL_CONGRATULATIONS="CONGRATULATIONS";//This is used in Access created email as well
    public static String EMAIL_CREATE_ACCESS="CREATE_ACCESS";
    public static String EMAIL_BEST_WISHES="BEST_WISHES";//This is used in Access created email as well
    //Constants used while sending Access created emails to ExpenseParty and RevenueParty
    public static String EMAIL_LOOK_FORWARD_MSG="LOOK_FORWARD_MSG";
    public static String EMAIL_RESUME_ACTIVITY="RESUME_ACTIVITY";
    //Constants used while Account Overdrawn
    public static String EMAIL_WARNING_OVERDRAWN="WARNING";
    
}
