package de.uniol.inf.is.odysseus.logicaloperator.latency;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;

@LogicalOperator(name = "LATENCYTOPAYLOAD", maxInputPorts = 1, minInputPorts = 1, doc = "Deprecated: You Latency.start, Latency.end, Latency.latency etc. directly as attributes! Adds attributes with the current latency information (start,end,latency,max_start,max_latency) to each tuple.", category = { LogicalOperatorCategory.BENCHMARK }, deprecation=true)
public class LatencyToPayloadAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -8208189869158067885L;
	boolean append;
	boolean small;

	public LatencyToPayloadAO() {
		append = true;
		small =false;
	}

	public LatencyToPayloadAO(boolean append, boolean small) {
		this.append = append;
		this.small = small;
	}

	public LatencyToPayloadAO(LatencyToPayloadAO ao) {
		this.append = ao.append;
		this.small = ao.small;
	}

	public SDFSchema buildOutputSchema(SDFSchema inputschema) {
		final List<SDFAttribute> attributes;
		final String[] names;
		if (append) {
			attributes = new ArrayList<>(inputschema.getAttributes());
		} else {
			if (small){
				attributes = new ArrayList<>(1);
			}else{
				attributes = new ArrayList<>(5);
			}
		}
		if (small) {
			names = new String[]{"latency"};			
		} else {
			names = new String[] {"latency", "latency_start", "latency_end", 
					"latency_max_start", "latency_max" };
		}
		for (String n : names) {
			SDFAttribute a = new SDFAttribute(inputschema.getURI(), n,
					SDFDatatype.LONG, null, null, null);
			attributes.add(a);
		}
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(attributes, inputschema);
		return schema;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema old = super.getOutputSchemaIntern(0);
		return buildOutputSchema(old);
	}

	@Parameter(optional=true, type = BooleanParameter.class)
	public void setSmall(boolean small) {
		this.small = small;
	}
	
	public boolean isSmall() {
		return small;
	}
	
	@Parameter(name="append",optional=true, type = BooleanParameter.class)
	public void setAppend2(boolean append) {
		this.append = append;
	}
	
	public boolean isAppend2() {
		return this.append;
	}
	
	public boolean isAppend() {
		return append;
	}
	
	@Override
	public LatencyToPayloadAO clone() {
		return new LatencyToPayloadAO(this);
	}

}
