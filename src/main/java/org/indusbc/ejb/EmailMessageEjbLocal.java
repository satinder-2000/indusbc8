package org.indusbc.ejb;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;
import org.indusbc.model.EmailMessage;

/**
 *
 * @author root
 */
@Local
public interface EmailMessageEjbLocal {
    
    public List<EmailMessage> getAllEmailMessages();
    public List<EmailMessage> getAllForRevenuePartyRegister();
    public List<EmailMessage> getAllForExpensePartyRegister();
    public List<EmailMessage> getAllForAccessCreated();
    public List<EmailMessage> getAllForAccountOverdrawn();
    public Map<String, String> getAllForRevenuePartyRegisterAsMap();
    public Map<String, String> getAllForExpensePartyRegisterAsMap();
    public Map<String, String> getAllForAccessCreatedAsMap();
    public Map<String, String> getAllForAccountOverdrawnAsMap();
    
    
}
