package edu.asu.spring.quadriga.domain.factory.impl.networks;

import org.springframework.stereotype.Service;

import edu.asu.spring.quadriga.domain.factory.networks.INetworkFactory;
import edu.asu.spring.quadriga.domain.impl.dictionary.Dictionary;
import edu.asu.spring.quadriga.domain.impl.networks.Network;
import edu.asu.spring.quadriga.domain.network.INetwork;

/**
 * Factory class for creating {@link Dictionary}.
 * 
 * @author Lohith Dwaraka
 *
 */
@Service
public class NetworkFactory implements INetworkFactory {
	
	public INetwork createNetworkObject() {
		
		return new Network();
	}

	@Override
	public INetwork cloneNetworkObject(INetwork network) 
	{
		INetwork clone = createNetworkObject();
		clone.setNetworkId(network.getNetworkId());
		clone.setNetworkName(network.getNetworkName());
		clone.setNetworksAccess(network.getNetworksAccess());
		clone.setStatus(network.getStatus());
		clone.setCreator(network.getCreator());
		clone.setTextUrl(network.getTextUrl());
		clone.setVersionNumber(network.getVersionNumber());
		clone.setAssignedUser(network.getAssignedUser());
		clone.setCreationTime(network.getCreationTime());
		clone.setNetworkNodes(network.getNetworkNodes());
		clone.setNetworkWorkspace(network.getNetworkWorkspace());
		clone.setCreatedBy(network.getCreatedBy());
		clone.setCreatedDate(network.getCreatedDate());
		clone.setUpdatedBy(network.getUpdatedBy());
		clone.setUpdatedDate(network.getUpdatedDate());
		return clone;
	}

}
