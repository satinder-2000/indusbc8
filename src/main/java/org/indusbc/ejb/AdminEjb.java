package org.indusbc.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.indusbc.model.Admin;

/**
 *
 * @author user
 */
@Stateless
public class AdminEjb implements AdminEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(AdminEjb.class.getName());
    
    @PersistenceContext(name = "indusbcPU")
    private EntityManager em;

    @Override
    public Admin createAdmin(Admin admin) {
        em.persist(admin);
        em.flush();
        LOGGER.info("Admin persisted in database");
        return admin;
    }

    @Override
    public Admin findAdmin() {
        Admin admin=null;
        TypedQuery<Admin> tQ=em.createQuery("select a from Admin a", Admin.class);
        try{
            admin=tQ.getSingleResult();
        }catch(NoResultException ex){
            admin=null; //good for us
        }
        return admin; 
    }

    @Override
    public Admin changeAdminPassword(Admin admin) {
        em.merge(admin);
        em.flush();
        LOGGER.info("Admin saved in database");
        return admin;
        
    }
}