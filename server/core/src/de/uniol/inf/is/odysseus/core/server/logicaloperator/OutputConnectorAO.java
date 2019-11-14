package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(name = "OutputConnector", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.PLAN }, doc = "This operator serves as a representer and defines a port for an outgoing connection")
public class OutputConnectorAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -3669762368140655617L;
	private int port = -1;
	
	public OutputConnectorAO() {
	}
	
	public OutputConnectorAO(OutputConnectorAO other) {
		super(other);
		this.port = other.port;
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
		return new OutputConnectorAO(this);
	}

}
