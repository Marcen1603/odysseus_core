package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * A logical operator than can be used to connect other plans. Typically, this operator is used in combination with the
 * SubQuery operator. A Connector is an access operator (with all its capabilities, e.g. to define schemas, types and metadata)
 * 
 * @author Marco Grawunder
 *
 */

@LogicalOperator(name = "Connector", minInputPorts = 0, maxInputPorts = Integer.MAX_VALUE, category = {
		LogicalOperatorCategory.PLAN }, doc = "This operator serves as a representer. Other plans can connect to this Connector")
public class ConnectorAO extends AbstractAccessAO{

	private static final long serialVersionUID = -2867000369059509011L;

	private int port = -1;
	
	public ConnectorAO() {
		setWrapper("GenericPush");
	}

	public ConnectorAO(ConnectorAO connectorAO) {
		super(connectorAO);
		this.port = connectorAO.port;
	}

	@Parameter(type = StringParameter.class, name = "Type", isList = false, optional = true, doc = "What kind of object are delivered. Default is 'tuple'")
	public void setType(String type) {
		super.setDataHandler(type);
	}
	
	@Parameter(type = IntegerParameter.class, name = "Port", isList = false, optional = false, doc = "The inport port that this connector represents.")
	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new ConnectorAO(this);
	}

}
