package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;

/**
 * don't even ask...
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class SourceTriplet {
	SDFSchema receiverMetadataCreationSubSchema;
	SDFSchema metadataCreationMetadataUpdateSubSchema;
	ReceiverPO receiver;
	MetadataCreationPO metadataCreation;
	MetadataUpdatePO metadataUpdate;
	
	
	
	public SourceTriplet(ReceiverPO receiver, MetadataCreationPO metadataCreation, MetadataUpdatePO metadataUpdate) {
		this.receiver = receiver;
		this.metadataCreation = metadataCreation;
		this.metadataUpdate = metadataUpdate;
		this.receiverMetadataCreationSubSchema = ((Subscription<IPhysicalOperator>)receiver.getSubscriptions().get(0)).getSchema();
		this.metadataCreationMetadataUpdateSubSchema = ((Subscription<IPhysicalOperator>)metadataCreation.getSubscriptions().get(0)).getSchema();
	}
	
	public void reconnect() {
		this.receiver.subscribeSink(metadataCreation, 0, 0, receiverMetadataCreationSubSchema);
		this.metadataCreation.subscribeSink(metadataUpdate, 0, 0, metadataCreationMetadataUpdateSubSchema);
	}

	public ReceiverPO<?, ?> getReceiver() {
		return receiver;
	}

	public void setReceiver(ReceiverPO<?, ?> receiver) {
		this.receiver = receiver;
	}

	public MetadataCreationPO<?, ?> getMetadataCreation() {
		return metadataCreation;
	}

	public void setMetadataCreation(MetadataCreationPO<?, ?> metadataCreation) {
		this.metadataCreation = metadataCreation;
	}

	public MetadataUpdatePO<?, ?> getMetadataUpdate() {
		return metadataUpdate;
	}

	public void setMetadataUpdate(MetadataUpdatePO<?, ?> metadataUpdate) {
		this.metadataUpdate = metadataUpdate;
	}
}
