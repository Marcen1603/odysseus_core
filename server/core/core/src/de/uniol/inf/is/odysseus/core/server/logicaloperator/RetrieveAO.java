package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;
import de.uniol.inf.is.odysseus.core.util.Constants;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "RETRIEVE", doc="Generic operator to connect to an input which input must be retrieved (i.e. pulled from source).", category={LogicalOperatorCategory.SOURCE})
public class RetrieveAO extends AbstractAccessAO {

	private static final long serialVersionUID = 3913899451565703944L;
	
	public RetrieveAO() {
		super();
		setWrapper(Constants.GENERIC_PULL);
	}

	public RetrieveAO(RetrieveAO other){
		super(other);
	}
	
	public RetrieveAO(Resource resource, String wrapper, String transport,
			String protocol, String datahandler, OptionMap options) {
		super(resource, wrapper, transport, protocol,datahandler,options);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RetrieveAO(this);
	}
	
	public List<String> getWrapperValues(){
		return WrapperRegistry.getWrapperNames();
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "transport", optional = false, possibleValues="getTransportValues", doc = "The name of the transport handler to use, e.g. File or TcpServer.")
	public void setTransportHandler(String transportHandler) {
		super.setTransportHandler(transportHandler);
	}
	
	public List<String> getTransportValues(){
		return TransportHandlerRegistry.getHandlerNames();
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "protocol", optional = true, possibleValues="getProtocolValues", doc = "The name of the protocol handler to use, e.g. Csv or SizeByteBuffer.")
	public void setProtocolHandler(String protocolHandler) {
		super.setProtocolHandler(protocolHandler);
	}
	
	public List<String> getProtocolValues(){
		return ProtocolHandlerRegistry.getHandlerNames();
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "DataHandler", optional = false, possibleValues="getDataHandlerValues", doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		super.setDataHandler(dataHandler);
	}
	
}
