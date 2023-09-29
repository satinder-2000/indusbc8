package org.indusbc.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "EXPENSE_ACCOUNT_TRANSACTION")
public class ExpenseAccountTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "MONEY_IN")
    private String moneyIn;
    @Column(name = "MONEY_OUT")
    private String moneyOut;
    @Column(name = "YEAR")
    private int year;
    @Column(name = "YTD_BALANCE")
    // It is important to initialse ytdBalance to 0 String to avoid null pointer exception 
    //  as the field participates in BigDecimal arithmatic operations.
    private String ytdBalance="0";
    @Column(name = "EXPENSE_ACCOUNT_ID")
    private int expenseAccountId;
    @Column(name = "CREATED_ON")
    private Timestamp createdOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(String moneyIn) {
        this.moneyIn = moneyIn;
    }

    public String getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(String moneyOut) {
        this.moneyOut = moneyOut;
    }

    public String getYtdBalance() {
        return ytdBalance;
    }

    public void setYtdBalance(String ytdBalance) {
        this.ytdBalance = ytdBalance;
    }

    

    

    public int getExpenseAccountId() {
        return expenseAccountId;
    }

    public void setExpenseAccountId(int expenseAccountId) {
        this.expenseAccountId = expenseAccountId;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
}
