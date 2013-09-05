package edu.asu.spring.quadriga.domain.implementation;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.asu.spring.db.conceptcollection.IDBConnectionCCManager;
import edu.asu.spring.quadriga.exceptions.QuadrigaStorageException;
@Service
public class CollectionsValidator implements Validator {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CollectionsValidator.class);
	@Autowired
	@Qualifier("DBConnectionCCManagerBean")
	private IDBConnectionCCManager dbConnect;
	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return arg0.isAssignableFrom(ConceptCollection.class);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ConceptCollection c = (ConceptCollection) obj;
		String id = c.getName();
		
			try {
				validateId(id, errors);
			} catch (QuadrigaStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private void validateId(String id, Errors errors) throws QuadrigaStorageException {
		// TODO Auto-generated method stub
		String ret=dbConnect.validateId(id);
		logger.info("After Validater:"+ret);
		if(!( ret == null || ret.isEmpty()) )
		{
			errors.rejectValue("name", "CollectionsValidator.id.notValid",
			          "Not a Unique Id");
		}
	}

}
