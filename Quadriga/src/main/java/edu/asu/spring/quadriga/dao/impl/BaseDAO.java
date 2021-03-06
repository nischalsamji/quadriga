package edu.asu.spring.quadriga.dao.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.spring.quadriga.dao.IBaseDAO;
import edu.asu.spring.quadriga.dto.QuadrigaUserDTO;

/**
 * This class contains the common methods used in 
 * data access object classes.
 * @author Julia Damerow, kbatna
 */
public abstract class BaseDAO<T> implements IBaseDAO<T>  {

	@Autowired
	protected SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(BaseDAO.class);
	
	
	/* (non-Javadoc)
     * @see edu.asu.spring.quadriga.dao.impl.IBaseDAO#generateUniqueID()
     */
	@Override
    public String generateUniqueID()
	{
		return UUID.randomUUID().toString();
	}
	
	/* (non-Javadoc)
     * @see edu.asu.spring.quadriga.dao.impl.IBaseDAO#getUserDTO(java.lang.String)
     */
	@Override
    public QuadrigaUserDTO getUserDTO(String userName) {
		return (QuadrigaUserDTO) sessionFactory.getCurrentSession().get(QuadrigaUserDTO.class, userName);
	}
	
	/* (non-Javadoc)
     * @see edu.asu.spring.quadriga.dao.impl.IBaseDAO#getList(java.lang.String)
     */
	@Override
    public List<String> getList(String commaSeparatedList) {
		return Arrays.asList(commaSeparatedList.split(","));
	}
	
	@Override
    public void updateDTO(T dto) {
        sessionFactory.getCurrentSession().update(dto);
    }
	
	@Override
    public void updateObject(Object obj) {
	    sessionFactory.getCurrentSession().update(obj);
	}
	
	@Override
    public void saveNewDTO(T dto) {
	    sessionFactory.getCurrentSession().save(dto);
	}
	
	@Override
    public void deleteDTO(T dto) {
	    sessionFactory.getCurrentSession().delete(dto);
	}
	
	protected void deleteObject(Object object) {
	    sessionFactory.getCurrentSession().delete(object);
	}

	protected T getDTO(Class<T> clazz, String id) {
	    try {
	        return (T) sessionFactory.getCurrentSession().get(clazz, id);
	    } catch(HibernateException e) {
            logger.error("Retrieve workspace details method :",e);
            return null;
        }
	}
	
	protected T getDTO(Class<T> clazz, Serializable primKey) {
        try {
            return (T) sessionFactory.getCurrentSession().get(clazz, primKey);
        } catch(HibernateException e) {
            logger.error("Retrieve workspace details method :",e);
            return null;
        }
    }
	
	public abstract T getDTO(String id);
}
