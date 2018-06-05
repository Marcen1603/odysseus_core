package de.uniol.inf.is.odysseus.net.data.impl.message;

import java.nio.ByteBuffer;
import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.MessageUtils;

public class OdysseusNodeIDsMessage implements IMessage {

	private Collection<OdysseusNodeID> nodeIDs;
	
	public OdysseusNodeIDsMessage() {
	}
	
	public OdysseusNodeIDsMessage( Collection<OdysseusNodeID> nodeIDs ) {
		Preconditions.checkNotNull(nodeIDs, "nodeIDs must not be null!");

		this.nodeIDs = nodeIDs;
	}
	
	@Override
	public byte[] toBytes() {
		Collection<String> nodeIDStrs = Lists.newArrayList();
		int size = 0;
		for( OdysseusNodeID nodeID : nodeIDs ) {
			String nodeIDStr = nodeID.toString();
			nodeIDStrs.add(nodeIDStr);
			size += nodeIDStr.length();
		}
		
		ByteBuffer bb = ByteBuffer.allocate(nodeIDStrs.size() * 4 + size);
		for( String nodeIDStr : nodeIDStrs ) {
			MessageUtils.putString(bb, nodeIDStr);
		}
		return bb.array();
	}

	@Override
	public void fromBytes(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		nodeIDs = Lists.newArrayList();
		while(bb.remaining() > 0 ) {
			String nodeIDStr = MessageUtils.getString(bb);
			nodeIDs.add( OdysseusNodeID.fromString(nodeIDStr));
		}
	}

	public Collection<OdysseusNodeID> getOdysseusNodeIDs() {
		return nodeIDs;
	}
}
