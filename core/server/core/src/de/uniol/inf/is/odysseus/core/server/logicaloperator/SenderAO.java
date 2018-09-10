package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = Integer.MAX_VALUE, minInputPorts = 0, name = "Sender", doc="This operator can be used to publish processing results to multiple endpoints using different transport and application protocols.", category={LogicalOperatorCategory.SINK})
public class SenderAO extends AbstractSenderAO {

	private static final long serialVersionUID = -7035132852387239629L;

	private List<Option> optionList = null;
	
	public SenderAO() {
		super();
	}

	public SenderAO(Resource sink, String wrapper,
			OptionMap optionsMap) {
		super(sink, wrapper, optionsMap);
	}

	public SenderAO(Resource sink, String wrapper, String dataHandler,
			OptionMap optionsMap) {
		super(sink, wrapper, dataHandler, optionsMap);
	}

	public SenderAO(AbstractSenderAO senderAO) {
		super(senderAO);
	}
	
	@Override
	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options for different handler.")
	public void setOptions(List<Option> value) {
		super.setOptions(value);
		
		optionList = value;
	}
	
	public List<Option> getOptions() {
		return optionList;
	}
	
	@Override
	@Parameter(name = "wrapper", type = StringParameter.class, optional = false)
	public void setWrapper(String wrapper) {
		super.setWrapper(wrapper);
	}
	
	@Override
	@Parameter(name = "dataHandler", type = StringParameter.class, optional = true)
	public void setDataHandler(String dataHandler) {
		super.setDataHandler(dataHandler);
	}
	
	@Override
	@Parameter(name = "protocol", type = StringParameter.class, optional = false)
	public void setProtocolHandler(String protocolHandler) {
		super.setProtocolHandler(protocolHandler);
	}

	@Override
	@Parameter(name = "transport", type = StringParameter.class, optional = false)
	public void setTransportHandler(String transportHandler) {
		super.setTransportHandler(transportHandler);
	}
	


	@Override
	public SenderAO clone() {
		return new SenderAO(this);
	}
}
