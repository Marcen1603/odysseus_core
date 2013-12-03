package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="SAMPLE", doc="This operator can reduce load by throwing away tuples.",category={LogicalOperatorCategory.PROCESSING})
public class SampleAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2100883143405405327L;
	private int sampleRate = 1;
	
	public SampleAO(){
		
	}
	
	public int getSampleRate() {
		return sampleRate;
	}
	
	@Parameter(type = IntegerParameter.class, name = "samplerate", optional=false)
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	public SampleAO(SampleAO sampleAO) {
		super(sampleAO);
		this.sampleRate = sampleAO.sampleRate;
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SampleAO(this);
	}

}
