package org.indusbc.mbean;

import org.indusbc.model.Access;
import org.indusbc.util.IndusConstants;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@Named(value = "logoutMBean")
@ViewScoped
public class LogoutMBean implements Serializable{
    
    public String logout(){
        HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute(IndusConstants.ACCESS);
        session.removeAttribute(IndusConstants.ACCESS);
        session.removeAttribute(IndusConstants.LOGGED_IN_EMAIL);
        return "logoutconfirm";
    }
    
}
