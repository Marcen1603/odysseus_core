package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "ACCESS", doc="Generic operator to connect to an input.", category={LogicalOperatorCategory.SOURCE})
public class AccessAO extends AbstractAccessAO {

	private static final long serialVersionUID = 3913899451565703944L;
	
	public AccessAO() {
		super();
	}

	public AccessAO(AccessAO other){
		super(other);
	}
	
	public AccessAO(Resource resource, String wrapper, String transport,
			String protocol, String datahandler, Map<String, String> options) {
		super(resource, wrapper, transport, protocol,datahandler,options);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new AccessAO(this);
	}

	@Parameter(type = StringParameter.class, name = "Wrapper", optional = false, possibleValues="getWrapperValues", doc = "The name of the wrapper to use, e.g. GenericPush or GenericPull.")
	public void setWrapper(String wrapper) {
		super.setWrapper(wrapper);
	}
	
	public List<String> getWrapperValues(){
		return WrapperRegistry.getWrapperNames();
	}
	
	@Parameter(type = StringParameter.class, name = "transport", optional = false, possibleValues="getTransportValues", doc = "The name of the transport handler to use, e.g. File or TcpServer.")
	public void setTransportHandler(String transportHandler) {
		super.setTransportHandler(transportHandler);
	}
	
	public List<String> getTransportValues(){
		return TransportHandlerRegistry.getHandlerNames();
	}
	
	@Parameter(type = StringParameter.class, name = "protocol", optional = false, possibleValues="getProtocolValues", doc = "The name of the protocol handler to use, e.g. Csv or SizeByteBuffer.")
	public void setProtocolHandler(String protocolHandler) {
		super.setProtocolHandler(protocolHandler);
	}
	
	public List<String> getProtocolValues(){
		return ProtocolHandlerRegistry.getHandlerNames();
	}

	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options for different handler.")
	public void setOptions(List<Option> value) {
		super.setOptions(value);
	}
	
}
