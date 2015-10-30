package de.uniol.inf.is.odysseus.wrapper.rdf.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;
import de.uniol.inf.is.odysseus.wrapper.rdf.RDFProtocolHandler;
import handler.TripleDataHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "RDFSource", category = {
		LogicalOperatorCategory.SOURCE }, doc = "Allows to read rdf input ")
public class RDFSource extends AbstractAccessAO {

	private static final long serialVersionUID = -1573273412235154421L;

	public RDFSource() {
		super();
		setDataHandler(new TripleDataHandler().getSupportedDataTypes().get(0));
		setProtocolHandler(RDFProtocolHandler.NAME);
		SDFAttribute sub = new SDFAttribute("", "subject", SDFDatatype.STRING);
		SDFAttribute pred = new SDFAttribute("", "predicate", SDFDatatype.STRING);
		SDFAttribute obj = new SDFAttribute("", "object", SDFDatatype.STRING);
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(sub);
		attributes.add(pred);
		attributes.add(obj);
		setAttributes(attributes);
	}

	public RDFSource(RDFSource other) {
		super(other);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RDFSource(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return super.getOutputSchemaIntern(pos);
	}

	@Parameter(name = RDFProtocolHandler.FORMAT, type = StringParameter.class, optional = false, doc = "The type of the rdf format (RDFXML, RDFJSON, N3, NTRIPLES, ...")
	public void setRDFFormat(String rdfFormat) {
		addOption(RDFProtocolHandler.FORMAT, rdfFormat);
	}

	public String getRDFFormat() {
		return getOption(RDFProtocolHandler.FORMAT);
	}
	
	@Override
	@Parameter(type = StringParameter.class, name = "Wrapper", optional = false, possibleValues="getWrapperValues", doc = "The name of the wrapper to use, e.g. GenericPush or GenericPull.")
	public void setWrapper(String wrapper) {
		super.setWrapper(wrapper);
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

}
