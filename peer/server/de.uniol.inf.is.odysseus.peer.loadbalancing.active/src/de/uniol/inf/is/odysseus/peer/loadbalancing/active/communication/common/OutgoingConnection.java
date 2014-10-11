package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Simple POJO used to hold some information about incoming and outgoing connections between Peers.
 * @author Carsten Cordes
 *
 */
public class OutgoingConnection {

	/**
	 * Constructor
	 * 
	 * @param operator
	 *            Operator-sided Endpoint of the connection
	 * @param remotePeerID
	 *            Peer to connect to
	 * @param oldPipeID
	 *            oldPipe Id, which should be replaced
	 * @param port
	 *            connection port (source-out or sink-in depends on context)
	 * @param schema
	 *            connection schema
	 */
	public OutgoingConnection(ILogicalOperator operator, String remotePeerID,
			String oldPipeID, int port, SDFSchema schema) {
		this.localOperator = operator;
		this.remotePeerID = remotePeerID;
		this.oldPipeID = oldPipeID;
		this.port = port;
		this.schema = schema;
	}

	public String remotePeerID;
	public ILogicalOperator localOperator;
	public String oldPipeID;
	public int port;
	public SDFSchema schema;
};