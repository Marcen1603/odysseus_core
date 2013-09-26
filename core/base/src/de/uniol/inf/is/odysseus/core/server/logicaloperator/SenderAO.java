package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = Integer.MAX_VALUE, minInputPorts = 1, name = "Sender", category={LogicalOperatorCategory.SINK})
public class SenderAO extends AbstractSenderAO {

	private static final long serialVersionUID = -7035132852387239629L;

	public SenderAO() {
		super();
	}

	public SenderAO(Resource sink, String wrapper,
			Map<String, String> optionsMap) {
		super(sink, wrapper, optionsMap);
	}

	public SenderAO(Resource sink, String wrapper, String dataHandler,
			Map<String, String> optionsMap) {
		super(sink, wrapper, dataHandler, optionsMap);
	}

	public SenderAO(AbstractSenderAO senderAO) {
		super(senderAO);
	}
	
	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options for different handler.")
	public void setOptions(List<Option> value) {
		super.setOptions(value);
	}
	
	@Parameter(name = "wrapper", type = StringParameter.class, optional = false)
	public void setWrapper(String wrapper) {
		super.setWrapper(wrapper);
	}
	
	@Parameter(name = "dataHandler", type = StringParameter.class, optional = false)
	public void setDataHandler(String dataHandler) {
		super.setDataHandler(dataHandler);
	}
	
	@Parameter(name = "protocol", type = StringParameter.class, optional = false)
	public void setProtocolHandler(String protocolHandler) {
		super.setProtocolHandler(protocolHandler);
	}

	@Parameter(name = "transport", type = StringParameter.class, optional = false)
	public void setTransportHandler(String transportHandler) {
		super.setTransportHandler(transportHandler);
	}
	
	@Override
	public SenderAO clone() {
		return new SenderAO(this);
	}

}
