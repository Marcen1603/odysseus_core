package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator;

import java.util.UUID;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQuerySenderAO;
import de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.data.DatastreamSender;

@SuppressWarnings("rawtypes")
public class SharedQuerySenderPO<T extends IStreamObject> extends AbstractSink<T> {

	private final OdysseusNodeID odysseusNodeID;
	private final UUID connectionID;
	
	private final DatastreamSender streamSender;
	
	public SharedQuerySenderPO(SharedQuerySenderAO operatorAO) {
		super();
		
		odysseusNodeID = OdysseusNodeID.fromString(operatorAO.getOdysseusNodeID());
		connectionID = UUID.fromString(operatorAO.getConnectionID());
		streamSender = new DatastreamSender(odysseusNodeID, connectionID);
	}

	public SharedQuerySenderPO(SharedQuerySenderPO<T> other) {
		super(other);
		
		this.odysseusNodeID = other.odysseusNodeID;
		this.connectionID = other.connectionID;
		this.streamSender = other.streamSender;
	}
	
	@Override
	protected AbstractSink clone() throws CloneNotSupportedException {
		return new SharedQuerySenderPO<T>(this);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			streamSender.checkConnection();
		} catch (Exception e) {
			throw new OpenFailedException(e);
		}
	}

	@Override
	protected void process_next(T object, int port) {

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

	}

	@Override
	protected void process_close() {
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (ipo == this) {
			return true;
		}

		return false;
	}

}
