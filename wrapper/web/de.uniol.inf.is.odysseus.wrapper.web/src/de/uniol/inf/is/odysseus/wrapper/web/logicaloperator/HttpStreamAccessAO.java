package de.uniol.inf.is.odysseus.wrapper.web.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.web.physicaloperator.access.HTTPStreamTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "HttpStreamACCESS", doc="Connect to a http stream", category={LogicalOperatorCategory.SOURCE})
public class HttpStreamAccessAO extends AbstractAccessAO{

	private static final long serialVersionUID = 7944870097466194497L;

	public HttpStreamAccessAO() {
		super();
		setTransportHandler(HTTPStreamTransportHandler.NAME);
		setWrapper(Constants.GENERIC_PULL);
		// This is needed if line transport handler is used
		addOption(LineProtocolHandler.NODONE, "true");
	}
	
	public HttpStreamAccessAO(HttpStreamAccessAO httpStreamAccessAO) {
		super(httpStreamAccessAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new HttpStreamAccessAO(this);
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "protocol", optional = false, possibleValues="getProtocolValues", doc = "The name of the protocol handler to use, e.g. Csv or SizeByteBuffer.")
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
	
	@Parameter(type = StringParameter.class, name = HTTPStreamTransportHandler.URI, optional = false, doc = "URI")
	public void setURI(String uri){
		addOption(HTTPStreamTransportHandler.URI, uri);
	}

	public String getURI() {
		return getOption(HTTPStreamTransportHandler.URI);
	}

}
