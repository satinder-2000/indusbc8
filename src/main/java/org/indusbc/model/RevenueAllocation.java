package org.indusbc.model;

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
@Table(name = "REVENUE_ALLOCATION")
public class RevenueAllocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "YEAR")
    private int year;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "ALLOCATION")
    private String allocation;
    @Column(name = "PERCENT_ALLOCATION")
    private String percentAllocation;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAllocation() {
        return allocation;
    }

    public void setAllocation(String allocation) {
        this.allocation = allocation;
    }

    public String getPercentAllocation() {
        return percentAllocation;
    }

    public void setPercentAllocation(String percentAllocation) {
        this.percentAllocation = percentAllocation;
    }

}