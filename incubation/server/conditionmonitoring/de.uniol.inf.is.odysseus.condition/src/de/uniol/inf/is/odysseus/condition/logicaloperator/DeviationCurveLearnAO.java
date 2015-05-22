package de.uniol.inf.is.odysseus.condition.logicaloperator;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DEVIATIONCURVELEARN", doc = "Learns deviation of (each point of a) curve.", category = { LogicalOperatorCategory.PROCESSING })
public class DeviationCurveLearnAO extends UnaryLogicalOp {
	
	private static final long serialVersionUID = 4139317638350922577L;

	private String valueAttributeName;
	private String counterAttributeName;
	private int curvesToLearn;

	public DeviationCurveLearnAO() {
		this.valueAttributeName = "value";
		this.counterAttributeName = "counter";
		this.curvesToLearn = 3;
	}
	
	public DeviationCurveLearnAO(DeviationCurveLearnAO ao) {
		this.valueAttributeName = ao.getValueAttributeName();
		this.curvesToLearn = ao.getCurvesToLearn();
	}
	
	@Parameter(type = StringParameter.class, name = "parameterAttribute", optional = true, doc = "Name of the attribute which should be analysed")
	public void setValueAttributeName(String valueAttributeName) {
		this.valueAttributeName = valueAttributeName;
	}

	public String getValueAttributeName() {
		return valueAttributeName;
	}
	
	@Parameter(type = StringParameter.class, name = "counterAttribute", optional = true, doc = "Name of the attribute which holds the counter (of each tuple in the curve)")
	public void setCounterAttributeName(String counterAttributeName) {
		this.counterAttributeName = counterAttributeName;
	}
	
	public String getCounterAttributeName() {
		return counterAttributeName;
	}

	@Parameter(type = IntegerParameter.class, name = "curvesToLearn", optional = true, doc = "THe number of (correct) curves to learn from. The first x curves will define the perfect curve the others are compared to.")
	public void setCurvesToLearn(int curvesToLearn) {
		this.curvesToLearn = curvesToLearn;
	}

	public int getCurvesToLearn() {
		return curvesToLearn;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DeviationCurveLearnAO(this);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {

		// Transfer the mean and the standard deviation to the next operator
		
		// The number of the tuple in the curve
		SDFAttribute number = new SDFAttribute(null, "number", SDFDatatype.INTEGER, null, null, null);
		SDFAttribute meanValue = new SDFAttribute(null, "mean", SDFDatatype.DOUBLE, null, null, null);
		SDFAttribute standardDeviation = new SDFAttribute(null, "standardDeviation", SDFDatatype.DOUBLE, null, null,
				null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.add(number);
		outputAttributes.add(meanValue);
		outputAttributes.add(standardDeviation);

		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, getInputSchema());
		this.setOutputSchema(outputSchema);

		return getOutputSchema();
	}

}
