package org.indusbc.ejb;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import org.indusbc.model.IdentityType;

/**
 *
 * @author root
 */
@Singleton
public class IdentityTypeEjb implements IdentityTypeEjbLocal {
    
    private static final Logger LOGGER=Logger.getLogger(IdentityTypeEjb.class.getName());
    
    @PersistenceContext(name = "indusBCPU")
    EntityManager em;

    public IdentityTypeEjb() {
    }

    @Override
    public List<IdentityType> findAll() {
        TypedQuery<IdentityType> tQ= em.createQuery("select it from IdentityType it", IdentityType.class);
        List<IdentityType> toReturn = tQ.getResultList();
        LOGGER.info(String.format("There are %d IdentityType(s)", toReturn.size()));
        return toReturn;
    }
}
