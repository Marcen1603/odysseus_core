package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "Connector", minInputPorts = 0, maxInputPorts = Integer.MAX_VALUE, category = {
		LogicalOperatorCategory.PLAN }, doc = "This operator serves as a representer. Other plans can connect to this Connector")
public class ConnectorAO extends AbstractAccessAO{

	private static final long serialVersionUID = -2867000369059509011L;

	public ConnectorAO() {
		setWrapper("GenericPush");
		// Because of AbstractAccessAO
		setTransportHandler(null);
	}

	public ConnectorAO(ConnectorAO connectorAO) {
		super(connectorAO);
	}

	@Parameter(type = StringParameter.class, name = "Type", isList = false, optional = true, doc = "What kind of object are delivered. Default is 'tuple'")
	public void setType(String type) {
		super.setDataHandler(type);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ConnectorAO(this);
	}

}
