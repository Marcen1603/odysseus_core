package de.uniol.inf.is.odysseus.net.querydistribute;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;

public class QueryPartTransmissionException extends OdysseusNetException {

	private static final long serialVersionUID = 1L;
	
	private final Collection<IOdysseusNode> faultyNodes = Lists.newArrayList();

	public QueryPartTransmissionException(Collection<IOdysseusNode> faultyNodes) {
		super();

		this.faultyNodes.addAll(faultyNodes);
	}

	public QueryPartTransmissionException(Collection<IOdysseusNode> faultyNodes, String message, Throwable cause) {
		super(message, cause);
		
		this.faultyNodes.addAll(faultyNodes);
	}

	public QueryPartTransmissionException(Collection<IOdysseusNode> faultyNodes, String message) {
		super(message);

		this.faultyNodes.addAll(faultyNodes);
	}

	public QueryPartTransmissionException(Collection<IOdysseusNode> faultyNodes, Throwable cause) {
		super(cause);

		this.faultyNodes.addAll(faultyNodes);
	}

	public Collection<IOdysseusNode> getFaultyNodes() {
		return faultyNodes;
	}
}
