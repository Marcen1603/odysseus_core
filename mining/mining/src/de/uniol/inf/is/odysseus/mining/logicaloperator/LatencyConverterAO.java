package de.uniol.inf.is.odysseus.mining.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(name="LatencyConvert", minInputPorts=1, maxInputPorts=1)
public class LatencyConverterAO extends UnaryLogicalOp{

	private int factor = 100000;
	private int sample = 1;
	
	private static final long serialVersionUID = 2736049206496339043L;

	public LatencyConverterAO(){
		super();
	}
	
	public LatencyConverterAO(LatencyConverterAO clone){
		super();
		this.factor = clone.factor;
		this.sample = clone.sample;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new LatencyConverterAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if(pos == 0){		
			SDFSchema old = super.getOutputSchemaIntern(0);
			List<SDFAttribute> attributes = new ArrayList<>();
			SDFAttribute a = new SDFAttribute(old.getURI(), "latency", SDFDatatype.DOUBLE);
			attributes.add(a);
			SDFAttribute b = new SDFAttribute(old.getURI(), "duration", SDFDatatype.DOUBLE);
			attributes.add(b);
			SDFSchema schema = new SDFSchema(old.getURI(), attributes);
			return schema;
		}
		return super.getOutputSchemaIntern(pos);
	}

	public int getFactor() {
		return factor;
	}

	@Parameter(name="factor", type=IntegerParameter.class, optional = true)
	public void setFactor(int factor) {
		this.factor = factor;
	}

	public int getSample() {
		return sample;
	}

	@Parameter(name="sample", type=IntegerParameter.class, optional = true)
	public void setSample(int sample) {
		this.sample = sample;
	}

}
