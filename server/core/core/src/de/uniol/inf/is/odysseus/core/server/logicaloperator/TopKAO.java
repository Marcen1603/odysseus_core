package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;

@LogicalOperator(name="TOPK", maxInputPorts=1,minInputPorts=1,category={ LogicalOperatorCategory.ADVANCED }, doc = "Calculate the top k elements of the input")
public class TopKAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 8852471127806000337L;

	private NamedExpression scoringFunction;
	private int k;
	private boolean descending;

	public TopKAO() {
	}

	public TopKAO(TopKAO other) {
		super(other);
		this.scoringFunction = other.scoringFunction;
		this.k = other.k;
		this.descending = other.descending;
	}

	public NamedExpression getScoringFunction() {
		return scoringFunction;
	}

	@Parameter(name="scoringFunction", optional = false, type = SDFExpressionParameter.class, doc ="The scoring function for ordering")
	public void setScoringFunction(NamedExpression scoringFunction) {
		this.scoringFunction = scoringFunction;
	}

	public int getK() {
		return k;
	}

	@Parameter(name="k", optional = false, type = IntegerParameter.class, doc ="The number of elements to sort")
	public void setK(int k) {
		this.k = k;
	}

	public boolean isDescending() {
		return descending;
	}

	@Parameter(name="descending", optional = true, type = BooleanParameter.class, doc ="Sort descending (default is true)")
	public void setDescending(boolean ascending) {
		this.descending = ascending;
	}

	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema subSchema = getInputSchema(0);
		List<SDFAttribute> attributes = new LinkedList<SDFAttribute>();
		attributes.add(new SDFAttribute(subSchema.getURI(), "topk", SDFDatatype.LIST_TUPLE, subSchema));
		attributes.add(new SDFAttribute(subSchema.getURI(), "trigger", SDFDatatype.TUPLE, subSchema));		
		SDFSchema outputSchema = new SDFSchema(subSchema, attributes);
		return outputSchema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TopKAO(this);
	}

}
