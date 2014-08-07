package de.uniol.inf.is.odysseus.wrapper.webcrawler.logicaloperator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.webcrawler.physicaloperator.access.WebCrawlerTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "WebCrawlerSourceAO", category = {LogicalOperatorCategory.SOURCE}, doc = "")
public class WebCrawlerSourceAO extends AbstractAccessAO
{
	private static final long serialVersionUID = -5775255960835766506L;
	
	public WebCrawlerSourceAO()
	{
		setWrapper(Constants.GENERIC_PULL);
		
		List<SDFAttribute> schema = new LinkedList<>();
		
		schema.add(new SDFAttribute(null,  "url", SDFDatatype.STRING, null));
		schema.add(new SDFAttribute(null, "text", SDFDatatype.STRING, null));
		
		setAttributes(schema);
	}

	public WebCrawlerSourceAO(WebCrawlerSourceAO webcrawlersource)
	{
		super(webcrawlersource);
	}
	
	public WebCrawlerSourceAO(Resource name, String wrapper, String transportHandler, String protocolHandler, String dataHandler, Map<String,String> optionsMap)
	{
		super(name, wrapper, transportHandler, protocolHandler, dataHandler, optionsMap);
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "DataHandler", optional = false, possibleValues="getDataHandlerValues", doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		super.setDataHandler(dataHandler);
	}

	@Override
	@Parameter(type = StringParameter.class, name = "protocol", optional = false, possibleValues="getProtocolValues", doc = "The name of the protocol handler to use, e.g. Csv or SizeByteBuffer.")
	public void setProtocolHandler(String protocolHandler) {
		super.setProtocolHandler(protocolHandler);
	}
	
	public List<String> getProtocolValues(){
		return ProtocolHandlerRegistry.getHandlerNames();
	}

	@Parameter(type = StringParameter.class, name = WebCrawlerTransportHandler.DEPTH, optional = false, doc = "")
	public void setDepth(String depth) {
		addOption(WebCrawlerTransportHandler.DEPTH, depth);
	}
		
	@Parameter(type = StringParameter.class, name = WebCrawlerTransportHandler.WEBSITES, optional = false, doc = "")
	public void setWebsites(String websites) {
		addOption(WebCrawlerTransportHandler.WEBSITES, websites);
	}
	
	@Parameter(type = StringParameter.class, name = WebCrawlerTransportHandler.FETCH, optional = false, doc = "")
	public void setFetch(String fetch) {
		addOption(WebCrawlerTransportHandler.FETCH, fetch);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new WebCrawlerSourceAO(this);
	}

}
