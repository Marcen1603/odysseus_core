package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;


/**
 * Bundles more than one receiver in a receiver List to allow for dynamic Load Balancing. Logical Operator
 * @author Carsten Cordes
 *
 */
@LogicalOperator(name = "JXTABUNDLERECEIVER", doc = "Received data from multiple JXTA Receivers", minInputPorts = 0, maxInputPorts = 0, category = { LogicalOperatorCategory.SOURCE }, hidden = true)
public class JxtaBundleReceiverAO extends JxtaReceiverAO {

	private static final long serialVersionUID = 1L;

	public JxtaBundleReceiverAO() {
		super();
	}

	public JxtaBundleReceiverAO(JxtaBundleReceiverAO other) {
		super(other);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new JxtaBundleReceiverAO(this);
	}

	@Override
	@Parameter(name = "Name", type = StringParameter.class, optional = true)
	public void setName(String name) {
		super.setName(name);
	}

	@Parameter(name = "PIPEID", doc = "Jxta Pipe ID to communicate with", type = StringParameter.class, optional = false)
	public void setPipeID(String pipeID) {
		super.setPipeID(pipeID);
	}

	@Parameter(name = "PEERID", doc = "Jxta Peer ID to communicate with", type = StringParameter.class, optional = false)
	public void setPeerID(String peerID) {
		super.setPeerID(peerID);
	}

	@Parameter(name = "SCHEMA", type = CreateSDFAttributeParameter.class, isList = true, optional = false)
	public void setSchema(List<SDFAttribute> outputSchema) {
		super.setSchema(outputSchema);
	}

}
