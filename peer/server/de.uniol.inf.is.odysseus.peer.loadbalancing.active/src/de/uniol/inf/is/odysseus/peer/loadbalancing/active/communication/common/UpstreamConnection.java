package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Simple POJO used to hold some information about upstream connections between
 * Peers.
 * 
 * @author Carsten Cordes
 *
 */
public class UpstreamConnection {

	/***
	 * Remote Peer ID (the upstream Peer in the connection.)
	 */
	public String remotePeerID;

	/**
	 * Local operator which has this connection.
	 */
	public ILogicalOperator localOperator;

	/**
	 * Old Pipe ID (which is replaced in some strategies.)
	 */
	public String oldPipeID;

	/**
	 * Sink-in-Port Number
	 */
	public int port;

	/**
	 * Input-Schema
	 */
	public SDFSchema schema;

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
	public UpstreamConnection(ILogicalOperator operator, String remotePeerID,
			String oldPipeID, int port, SDFSchema schema) {
		this.localOperator = operator;
		this.remotePeerID = remotePeerID;
		this.oldPipeID = oldPipeID;
		this.port = port;
		this.schema = schema;
	}
}