/**
 * 
 */
package edu.asu.spring.quadriga.service;

import java.util.List;

import edu.asu.spring.quadriga.domain.IConceptCollection;
import edu.asu.spring.quadriga.domain.implementation.ConceptCollection;
import edu.asu.spring.quadriga.domain.implementation.ConceptpowerReply;


/**
 * 
 * 
 * Interface that places restraints on the ConceptCollectionManager class to implement
 * the required behaviors.
 * 
 *
 * @author satyaswaroop
 *
 */
public interface IConceptCollectionManager {

	public abstract List<IConceptCollection> getCollectionsOwnedbyUser(String sUserId);
	public abstract List<IConceptCollection> getUserCollaborations(String sUserId);
	public abstract String updateConceptCollection(ConceptCollection conceptCollection);	
	public abstract int deleteConceptCollection(String id);	
	public abstract int addConceptCollection(ConceptCollection newConcept);	
	public ConceptpowerReply search(String item, String pos);
	public abstract void getCollectionDetails(IConceptCollection concept);
}
