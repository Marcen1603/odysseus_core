package de.uniol.inf.is.odysseus.relational_interval.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name = "FastMedian", minInputPorts = 1, maxInputPorts = 1, doc = "Calculate the median for one attribute in the input tuples", category = { LogicalOperatorCategory.ADVANCED })
public class RelationalFastMedianAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -9112124571817836462L;

	private SDFAttribute medianAttribute;
	private boolean numericalMedian = false; 
	private List<SDFAttribute> groupingAttributes;

	public RelationalFastMedianAO(RelationalFastMedianAO op) {
		super(op);
		if (op.groupingAttributes != null){
			this.groupingAttributes = new LinkedList<>();
			this.groupingAttributes.addAll(op.groupingAttributes);
		}
		this.medianAttribute = op.medianAttribute;
		this.numericalMedian = op.numericalMedian;
	}

	public RelationalFastMedianAO() {
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RelationalFastMedianAO(this);
	}

	@Parameter(name = "GROUP_BY", optional = true, type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setGroupingAttributes(List<SDFAttribute> attributes) {
		this.groupingAttributes = attributes;
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Parameter(name = "Attribute", optional = false, type = ResolvedSDFAttributeParameter.class)
	public void setMedianAttribute(SDFAttribute attribute) {
		this.medianAttribute = attribute;
	}

	public SDFAttribute getMedianAttribute() {
		return medianAttribute;
	}

	@Parameter(name = "numerical", optional = true, type = BooleanParameter.class)
	public void setIsNumericalMedian(boolean numericalMedian){
		this.numericalMedian = numericalMedian;
	}
	
	public boolean isNumericalMedian() {
		return numericalMedian;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> outattr = new LinkedList<>();
		if (groupingAttributes != null){
			outattr.addAll(groupingAttributes);
		}
		outattr.add(medianAttribute);
		SDFSchema output = new SDFSchema(getInputSchema(0), outattr);
		return output;
	}

}
