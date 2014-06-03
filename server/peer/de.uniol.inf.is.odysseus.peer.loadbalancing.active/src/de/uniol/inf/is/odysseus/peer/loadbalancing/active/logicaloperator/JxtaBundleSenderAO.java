package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

@LogicalOperator(name = "JXTABUNDLESENDER", doc = "Send data with multiple JXTA senders", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.SINK }, hidden = true)
public class JxtaBundleSenderAO extends JxtaSenderAO {
	private static final long serialVersionUID = 1L;

	private String pipeID;
	private String peerID;
	private boolean useUDP;
	private boolean useMultiple;
	private boolean writeResourceUsage;

	public JxtaBundleSenderAO() {
		super();
	}

	public JxtaBundleSenderAO(JxtaBundleSenderAO other) {
		super(other);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new JxtaBundleSenderAO(this);
	}

	@Parameter(name = "useUDP", doc = "Specifies if an unreliable but fast udp-connection should be used", type = BooleanParameter.class, optional = true)
	public void setUseUDP(boolean useUDP) {
		this.useUDP = useUDP;
		addParameterInfo("useUDP", "'" + (useUDP ? "true" : "false") + "'");
	}

	@Override
	@Parameter(name = "useMultiple", doc = "Specifies if the sender should support multiple connections", type = BooleanParameter.class, optional = true)
	public void setUseMultiple(boolean useMultiple) {
		super.setUseMultiple(useMultiple);
	}

	@Override
	@Parameter(name = "PIPEID", doc = "Jxta Pipe ID to communicate with", type = StringParameter.class, optional = false)
	public void setPipeID(String pipeID) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(pipeID),
				"PipeID for sender ao must not be null or empty!");
		this.pipeID = pipeID;
		addParameterInfo("PIPEID", "'" + pipeID + "'");
	}

	@Override
	@Parameter(name = "PEERID", doc = "Jxta Peer ID to communicate with", type = StringParameter.class, optional = true)
	public void setPeerID(String peerID) {
		super.setPeerID(peerID);
	}

	@Override
	@Parameter(name = "WRITERESOURCEUSAGE", doc = "Should local resource usage be appended as metadata?", type = BooleanParameter.class, optional = true)
	public void setWriteResourceUsage(boolean write) {
		super.setWriteResourceUsage(write);
	}

}
