package de.uniol.inf.is.odysseus.processmining.lossycounting.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;

@LogicalOperator( name = "LOSSY", maxInputPorts = 1, minInputPorts = 1,
doc="Adapted Lossy Counting for Process Mining", category={LogicalOperatorCategory.MINING})
public class LossyCountingAO extends UnaryLogicalOp{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8222141477680554470L;
	private int bucketWidth;
	private double minFrequence = 0.01;
	
	public LossyCountingAO(){
		super();
	}
    	
	public LossyCountingAO(LossyCountingAO lossyCountingAO){
		super(lossyCountingAO);
		this.bucketWidth = lossyCountingAO.getBucketWidth();
		this.minFrequence = lossyCountingAO.getMinFrequence();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LossyCountingAO(this);
	}
	
	// Setter and Getter

	@Parameter(type = IntegerParameter.class, name = "bucketWidth", optional = false)
	public void setBucketWidth(int bucketWidth) {
		this.bucketWidth = bucketWidth;
	}
	
	@Parameter(type = DoubleParameter.class, name = "minFrequence", optional = true)
	public void setMinFreqBoundary(double minFrequence) {
		this.minFrequence = minFrequence;
	}
	

	public double getMinFrequence() {
		return minFrequence;
	}

	public void setMinFrequence(double minFrequence) {
		this.minFrequence = minFrequence;
	}

	public int getBucketWidth() {
		return bucketWidth;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		SDFAttribute attributeMap = new SDFAttribute(null, "Relation Map", SDFDatatype.INTEGER, null, null, null);
		attributes.add(attributeMap);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(attributes, getInputSchema());;
		return outSchema;
	}

}
