package org.indusbc.ejb;

import javax.ejb.Local;
import java.util.List;
import org.indusbc.model.ExpenseCategory;

/**
 *
 * @author user
 */
@Local
public interface ExpenseCategoryEjbLocal {
    
    public List<ExpenseCategory> getExpenseCategories();
    public List<ExpenseCategory> getExpenseCategoriesForYear(int year);
    public ExpenseCategory findByNameAndYear(String expCat, int year);
    public ExpenseCategory findById(Integer expCatId);
}
