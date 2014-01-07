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
	private boolean useUDP;

	public JxtaSenderAO() {
		super();
	}

	public JxtaSenderAO(JxtaSenderAO other) {
		super(other);
		
		this.pipeID = other.pipeID;
		this.useUDP = other.useUDP;
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
}
