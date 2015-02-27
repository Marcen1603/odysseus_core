package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SDFExpressionParameter;

@LogicalOperator(name="TOPK", maxInputPorts=1,minInputPorts=1,category={ LogicalOperatorCategory.ADVANCED }, doc = "Calculate the top k elements of the input")
public class TopKAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 8852471127806000337L;

	private NamedExpression scoringFunction;
	private int k;
	private boolean descending = true;
	private boolean suppressDuplicates = true;
	private List<SDFAttribute> groupingAttributes;
	private boolean fastGrouping = false;
	
	public TopKAO() {
	}

	public TopKAO(TopKAO other) {
		super(other);
		this.scoringFunction = other.scoringFunction;
		this.k = other.k;
		this.descending = other.descending;
		this.setSuppressDuplicates(other.isSuppressDuplicates());
		if (other.groupingAttributes != null){
			this.groupingAttributes = new ArrayList<SDFAttribute>(other.groupingAttributes);
		}
		this.fastGrouping = other.fastGrouping;
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

	
	/**
	 * @return the suppressDuplicates
	 */
	public boolean isSuppressDuplicates() {
		return suppressDuplicates;
	}

	/**
	 * @param suppressDuplicates the suppressDuplicates to set
	 */
	@Parameter(name="suppressDuplicates", optional = true, type = BooleanParameter.class, doc ="If set to true (defaul), output is only generated when a new top k set is available")
	public void setSuppressDuplicates(boolean suppressDuplicates) {
		this.suppressDuplicates = suppressDuplicates;
	}
	
	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}
	
	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	/**
	 * @return the fastGrouping
	 */
	public boolean isFastGrouping() {
		return fastGrouping;
	}

	/**
	 * @param fastGrouping the fastGrouping to set
	 */
	@Parameter(name = "fastGrouping", type = BooleanParameter.class, optional = true, doc = "Use hash code instead of tuple compare to create group. Potentially unsafe!")
	public void setFastGrouping(boolean fastGrouping) {
		this.fastGrouping = fastGrouping;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema subSchema = getInputSchema(0);
		List<SDFAttribute> attributes = new LinkedList<SDFAttribute>();
		attributes.add(new SDFAttribute(subSchema.getURI(), "topk", SDFDatatype.LIST_TUPLE, subSchema));
		attributes.add(new SDFAttribute(subSchema.getURI(), "trigger", SDFDatatype.TUPLE, subSchema));
		if (groupingAttributes != null){
			attributes.addAll(groupingAttributes);
		}
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, subSchema);
		return outputSchema;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TopKAO(this);
	}

}
