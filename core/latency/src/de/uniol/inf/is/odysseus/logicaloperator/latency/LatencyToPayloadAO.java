package de.uniol.inf.is.odysseus.logicaloperator.latency;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name="LATENCYTOPAYLOAD", maxInputPorts=1, minInputPorts=1)
public class LatencyToPayloadAO extends AbstractLogicalOperator {
	
	public LatencyToPayloadAO() {
		
	}
	
	
	public LatencyToPayloadAO(LatencyToPayloadAO old) {
		
	}
	
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema old = super.getOutputSchemaIntern(0);
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute a = new SDFAttribute(old.getURI(), "latency", SDFDatatype.LONG);
		attributes.add(a);		
		SDFSchema schema = new SDFSchema(old.getURI(), attributes);
		return schema;
	}

	@Override
	public LatencyToPayloadAO clone() {
		return new LatencyToPayloadAO(this);
	}

}
