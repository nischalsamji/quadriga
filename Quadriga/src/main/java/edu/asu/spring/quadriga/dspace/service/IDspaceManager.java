package edu.asu.spring.quadriga.dspace.service;

import java.util.List;

import edu.asu.spring.quadriga.domain.ICollection;
import edu.asu.spring.quadriga.domain.ICommunity;
import edu.asu.spring.quadriga.domain.implementation.Community;

public interface IDspaceManager {

	public abstract List<ICommunity> getAllCommunities(String sUserName, String sPassword);

	public abstract List<ICollection> getAllCollections(String sUserName, String sPassword,String sCommunityTitle);

	public abstract void checkRestConnection(String sUserName, String sPassword);

	public abstract String getCollectionName(String sUserName, String sPassword,String sCollectionId);

}