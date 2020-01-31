package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.InlineTransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "INLINE", category = {
		LogicalOperatorCategory.SOURCE }, doc = "A source than can contain a list of tuples")
public class InlineSource extends AbstractAccessAO {

	private static final long serialVersionUID = 4352955793855481978L;

	public InlineSource() {
		setTransportHandler(InlineTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
		setProtocolHandler(SimpleCSVProtocolHandler.NAME);
		addOption("csv.trim", "true");
	}

	public InlineSource(AbstractAccessAO ao) {
		super(ao);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new InlineSource(this);
	}

	@Parameter(name = "Period", type = LongParameter.class, optional = true, doc = "The timer period in ms")
	public void setPeriod(long period) {
		addOption(InlineTransportHandler.PERIOD, period + "");
	}

	@Parameter(name = "Content", type = StringParameter.class, optional = false, doc = "The content", isList = true)
	public void setContent(List<String> content) {
		StringBuilder contentForOption = new StringBuilder();
		if (!content.isEmpty()) {
			contentForOption.append(content.get(0));
			for (int i = 1; i < content.size(); i++) {
				contentForOption.append(InlineTransportHandler.SPLITTER).append(content.get(i));
			}
			addOption(InlineTransportHandler.CONTENT, contentForOption.toString());
		}
	}
	
	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = false, doc = "The output schema.")
	public void setAttributes(List<SDFAttribute> attributes) {
		super.setAttributes(attributes);;
	}

	
}
