package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

// TODO This operator exists only for test purposes.
@LogicalOperator(category = {
		LogicalOperatorCategory.SINK }, doc = "sends ASDUs via IEC 60870-5-104", maxInputPorts = 1, minInputPorts = 1, name = "IEC104Sender")
public class IEC104SenderAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 2560894615344383129L;

	private String host;

	private int port;

	private int timeout = 5000;

	public IEC104SenderAO() {
	}

	public IEC104SenderAO(IEC104SenderAO other) {
		host = other.host;
		port = other.port;
		timeout = other.timeout;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new IEC104SenderAO(this);
	}

	public String getHost() {
		return host;
	}

	@Parameter(type = StringParameter.class, name = "host")
	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	@Parameter(type = IntegerParameter.class, name = "port")
	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	@Parameter(type = IntegerParameter.class, name = "timeout", optional = true)
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}