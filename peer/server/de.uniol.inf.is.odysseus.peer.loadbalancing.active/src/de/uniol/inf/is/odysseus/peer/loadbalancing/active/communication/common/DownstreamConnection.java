package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Simple POJO used to hold some information about downstream connections between Peers.
 * @author Carsten Cordes
 *
 */
public class DownstreamConnection {
	


	/**
	 * Peer ID of downstream Peer.
	 */
	public String remotePeerID;
	
	/**
	 * Local operator which holds connection.
	 */
	public ILogicalOperator localOperator;
	
	/**
	 * Old pipe ID (needs to be replaced in some Strategies)
	 */
	public String oldPipeID;
	
	/**
	 * Port number (source out)
	 */
	public int port;
	
	/**
	 * Output Schema
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
	public DownstreamConnection(ILogicalOperator operator, String remotePeerID,
			String oldPipeID, int port, SDFSchema schema) {
		this.localOperator = operator;
		this.remotePeerID = remotePeerID;
		this.oldPipeID = oldPipeID;
		this.port = port;
		this.schema = schema;
	}
}