package edu.asu.spring.quadriga.domain.implementation;

import edu.asu.spring.quadriga.domain.ICollaboratorRole;
/**
 * @description : CollaboratorRole class describing the properties 
 *                of a CollaboratorRole object
 * 
 * @author      : Kiran
 *
 */
public class CollaboratorRole implements ICollaboratorRole 
{
	private String roleid;
	private String rolename;
	private String roledescription;
	@Override
	public String getRoleid() {
		return roleid;
	}
	@Override
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	@Override
	public String getRolename() {
		return rolename;
	}
	@Override
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	@Override
	public String getRoledescription() {
		return roledescription;
	}
	@Override
	public void setRoledescription(String roledescription) {
		this.roledescription = roledescription;
	}
	
	
	

}
