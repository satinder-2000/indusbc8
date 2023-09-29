package org.indusbc.ejb;

import javax.ejb.Local;
import java.util.List;
import org.indusbc.model.RevenueCategory;

/**
 *
 * @author root
 */
@Local
public interface RevenueCategoryEjbLocal {
    
    public List<RevenueCategory> getRevenueCategoriesForYear(int year);

    public RevenueCategory findByNameAndYear(String revCat, int year);

    public RevenueCategory findById(Integer revCatId);
    
}
