package de.uniol.inf.is.odysseus.logicaloperator.latency;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="LATENCYTOPAYLOAD", maxInputPorts=1, minInputPorts=1, doc="Add an attribute with the current latency information to each tuple.", category={LogicalOperatorCategory.BENCHMARK})
public class LatencyToPayloadAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = -8208189869158067885L;

	public LatencyToPayloadAO() {
		
	}
	
	
	public LatencyToPayloadAO(LatencyToPayloadAO old) {
		
	}
	
	public SDFSchema buildOutputSchema(SDFSchema inputschema){
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute a = new SDFAttribute(inputschema.getURI(), "latency", SDFDatatype.LONG, null, null, null);
		attributes.add(a);		
		SDFSchema schema = new SDFSchema(inputschema, attributes);
		return schema;
	}
	
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema old = super.getOutputSchemaIntern(0);
		return buildOutputSchema(old);
	}

	@Override
	public LatencyToPayloadAO clone() {
		return new LatencyToPayloadAO(this);
	}

}
