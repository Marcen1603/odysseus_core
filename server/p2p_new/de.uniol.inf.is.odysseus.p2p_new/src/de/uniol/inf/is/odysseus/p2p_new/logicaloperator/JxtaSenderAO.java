package de.uniol.inf.is.odysseus.p2p_new.logicaloperator;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "JXTASENDER", doc = "Send data with JXTA", minInputPorts = 1, maxInputPorts = 1, category={LogicalOperatorCategory.SINK}, hidden = true)
public class JxtaSenderAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private String pipeID;
	private String peerID;
	private boolean useUDP;
	private boolean useMultiple;
	private boolean writeResourceUsage;

	public JxtaSenderAO() {
		super();
	}

	public JxtaSenderAO(JxtaSenderAO other) {
		super(other);
		
		this.pipeID = other.pipeID;
		this.peerID = other.peerID;
		this.useUDP = other.useUDP;
		this.useMultiple = other.useMultiple;
		this.writeResourceUsage = other.writeResourceUsage;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new JxtaSenderAO(this);
	}

	public String getPipeID() {
		return this.pipeID;
	}
	
	@Parameter(name = "useUDP", doc = "Specifies if an unreliable but fast udp-connection should be used", type = BooleanParameter.class, optional = true)
	public void setUseUDP(boolean useUDP) {
		this.useUDP = useUDP;
		addParameterInfo("useUDP", "'" + (useUDP ? "true" : "false") + "'");
	}
	
	public boolean isUseUDP() {
		return useUDP;
	}

	public boolean isUseMultiple() {
		return useMultiple;
	}
	
	@Parameter(name = "useMultiple", doc = "Specifies if the sender should support multiple connections", type = BooleanParameter.class, optional = true)
	public void setUseMultiple(boolean useMultiple) {
		this.useMultiple = useMultiple;
		addParameterInfo("useMultiple", "'" + (useMultiple ? "true" : "false") + "'");
	}
	
	@Override
	public boolean isValid() {
		return !Strings.isNullOrEmpty(this.pipeID);
	}

	@Parameter(name = "PIPEID", doc = "Jxta Pipe ID to communicate with", type = StringParameter.class, optional = false)
	public void setPipeID(String pipeID) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(pipeID), "PipeID for sender ao must not be null or empty!");
		this.pipeID = pipeID;
		addParameterInfo("PIPEID", "'" + pipeID + "'");
	}
	
	@Parameter(name = "PEERID", doc = "Jxta Peer ID to communicate with", type = StringParameter.class, optional = true)
	public void setPeerID( String peerID ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(peerID), "PeerID for sender ao must not be null or empty!");
		
		this.peerID = peerID;
		addParameterInfo("PEERID", "'" + peerID + "'");
	}
	
	public String getPeerID() {
		return peerID;
	}
	
	@Override
	public boolean isSourceOperator() {
		return false;
	}
		
	@Parameter(name = "WRITERESOURCEUSAGE", doc = "Should local resource usage be appended as metadata?", type = BooleanParameter.class, optional = true)
	public void setWriteResourceUsage( boolean write ) {
		writeResourceUsage = write;
	}
	
	public boolean isWriteResourceUsage() {
		return writeResourceUsage;
	}

}
