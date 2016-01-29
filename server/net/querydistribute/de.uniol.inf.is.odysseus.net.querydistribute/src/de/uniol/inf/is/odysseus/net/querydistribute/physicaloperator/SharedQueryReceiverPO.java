package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator;

import java.util.UUID;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQueryReceiverAO;
import de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.data.DatastreamReceiver;

@SuppressWarnings("rawtypes")
public class SharedQueryReceiverPO<T extends IStreamObject> extends AbstractSource<T> {

	private final OdysseusNodeID odysseusNodeID;
	private final UUID connectionID;
	
	private final DatastreamReceiver streamReceiver;

	public SharedQueryReceiverPO( SharedQueryReceiverAO operatorAO ) {
		super();
		
		odysseusNodeID = OdysseusNodeID.fromString(operatorAO.getOdysseusNodeID());
		connectionID = UUID.fromString(operatorAO.getConnectionID());
		
		streamReceiver = new DatastreamReceiver(odysseusNodeID, connectionID);
		streamReceiver.connect();
	}
	
	public SharedQueryReceiverPO( SharedQueryReceiverPO<T> other) {
		super(other);
		
		this.odysseusNodeID = other.odysseusNodeID;
		this.connectionID = other.connectionID;
		this.streamReceiver = other.streamReceiver;
	}
	
	@Override
	protected AbstractSource clone() throws CloneNotSupportedException {
		return new SharedQueryReceiverPO<T>(this);
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		try {
			streamReceiver.checkConnection();
			
		} catch (Exception e) {
			throw new OpenFailedException(e);
		}
	}
	
	@Override
	protected void process_close() {
	}
}
