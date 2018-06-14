package de.uniol.inf.is.odysseus.net.data.impl.message;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;

public class ContainsOdysseusNodeIDMessage implements IMessage {

	private OdysseusNodeID nodeID;
	
	public ContainsOdysseusNodeIDMessage() {
		
	}
	
	public ContainsOdysseusNodeIDMessage( OdysseusNodeID nodeID ) {
		Preconditions.checkNotNull(nodeID, "nodeID must not be null!");

		this.nodeID = nodeID;
	}
	
	@Override
	public byte[] toBytes() {
		return nodeID.toString().getBytes();
	}

	@Override
	public void fromBytes(byte[] data) {
		nodeID = OdysseusNodeID.fromString(new String(data));
	}

	public OdysseusNodeID getOdysseusNodeID() {
		return nodeID;
	}
}
