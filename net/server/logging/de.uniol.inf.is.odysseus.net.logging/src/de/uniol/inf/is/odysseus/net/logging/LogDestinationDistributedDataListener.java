package de.uniol.inf.is.odysseus.net.logging;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.data.DistributedDataAdapter;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class LogDestinationDistributedDataListener extends DistributedDataAdapter {

	private static IOdysseusNodeManager nodeManager;

	// called by OSGi-DS
	public static void bindOdysseusNodeManager(IOdysseusNodeManager serv) {
		nodeManager = serv;
	}

	// called by OSGi-DS
	public static void unbindOdysseusNodeManager(IOdysseusNodeManager serv) {
		if (nodeManager == serv) {
			nodeManager = null;
		}
	}
	
	@Override
	public void distributedDataAdded(IDistributedDataManager sender, IDistributedData addedData) {
		if( isLoggingDistributedData(addedData)) {
			try {
				OdysseusNodeID creatorNodeID = addedData.getCreator();
				Optional<IOdysseusNode> optCreatorNode = nodeManager.getNode(creatorNodeID);
				
				if( optCreatorNode.isPresent() ) {
					IOdysseusNode creatorNode = optCreatorNode.get();
					if( !creatorNode.equals(nodeManager.getLocalNode())) {
						LoggingDestinations.add(creatorNode);
					}
				} 
			} catch( OdysseusNetException e ) {
			}
		}
	}

	@Override
	public void distributedDataModified(IDistributedDataManager sender, IDistributedData oldData, IDistributedData newData) {
		distributedDataRemoved(sender, oldData);
		distributedDataAdded(sender, newData);
	}

	@Override
	public void distributedDataRemoved(IDistributedDataManager sender, IDistributedData removedData) {
		if( isLoggingDistributedData(removedData)) {
			OdysseusNodeID creatorNodeID = removedData.getCreator();
			Optional<IOdysseusNode> optCreatorNode = nodeManager.getNode(creatorNodeID);
			
			if( optCreatorNode.isPresent() ) {
				LoggingDestinations.remove(optCreatorNode.get());
			} 
		}
	}

	private static boolean isLoggingDistributedData( IDistributedData data ) {
		return data.getName().equals(OdysseusNetLoggingComponent.LOGGING_DISTRIBUTED_DATA_NAME);
	}

}
