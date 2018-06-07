package de.uniol.inf.is.odysseus.anomalydetection.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;

/**
 * This operator compares the occurrence frequency of tuples (using the
 * equal-method of the tuples) in two different windows. The first window (on
 * port 0) has to be bigger or equal in size than the second window (on port 1).
 * The relative occurrence frequency of the incoming tuple in the small and the
 * big window is compared. If it's significant different (with the tolerance the
 * user chose) an anomaly tuple will be generated.
 * 
 * The frequency-value: A value smaller than 1 means, that the tuple occurred
 * less frequent (seldom) in the small window (compared to the big window). A
 * value bigger than 1 means, that the tuple occurred more often in the small
 * window.
 * 
 * @author Tobias Brandt
 */
@LogicalOperator(maxInputPorts = 2, minInputPorts = 2, name = "FREQUENCYCOMPARE", doc = "Compares the frequency of tuples (states, equal tuples) in two different windows. Put the big window to the first input port and the small window to the second input port.", category = { LogicalOperatorCategory.PROCESSING })
public class FrequencyAnalysisAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 416192455429488684L;

	private double toleranceNegative;
	private double tolerancePositive;
	
	private boolean deliverFirstElements;

	public FrequencyAnalysisAO() {
		this.toleranceNegative = 0.3;
		this.tolerancePositive = 0.3;
		this.deliverFirstElements = false;
	}

	public FrequencyAnalysisAO(FrequencyAnalysisAO ao) {
		super(ao);
		this.toleranceNegative = ao.getToleranceNegative();
		this.tolerancePositive = ao.getTolerancePositive();
		this.deliverFirstElements = ao.isDeliverFirstElements();
	}

	@Parameter(type = DoubleParameter.class, name = "negativeTolerance", optional = true, doc = "The tolerance to the negative side (if less tuples of type 'a' occur in the small window in comparisson to the big window). Standard is 0.3, which means 30% less is allowed")
	public void setToleranceNegative(double toleranceNegative) {
		this.toleranceNegative = toleranceNegative;
	}

	@Parameter(type = DoubleParameter.class, name = "positiveTolerance", optional = true, doc = "The tolerance to the positive side (if more tuples of type 'a' occur in the small window in comparisson to the big window). Standard is 0.3, which means 30% more is allowed")
	public void setTolerancePositive(double tolerancePositive) {
		this.tolerancePositive = tolerancePositive;
	}
	
	@Parameter(type = BooleanParameter.class, name = "deliverFirstElements", optional = true, doc = "Normally the operator starts to compare the frequencies when the big window is at least as big as the small window. With this option set to true, the comparison will start with the first tuple reaching this operator. Default is false.")
	public void setDeliverFirstElements(boolean deliverFirstElements) {
		this.deliverFirstElements = deliverFirstElements;
	}

	public double getToleranceNegative() {
		return toleranceNegative;
	}

	public double getTolerancePositive() {
		return tolerancePositive;
	}
	
	public boolean isDeliverFirstElements() {
		return deliverFirstElements;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add the anomaly-score to the attributes and keep the old attributes
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute frequency = new SDFAttribute(null, "frequency", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute anomalyScore = new SDFAttribute(null, "anomalyScore", SDFDatatype.DOUBLE, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(frequency);
		outputAttributes.add(anomalyScore);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequencyAnalysisAO(this);
	}

}
