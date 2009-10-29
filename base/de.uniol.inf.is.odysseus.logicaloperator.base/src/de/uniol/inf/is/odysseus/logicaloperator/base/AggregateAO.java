package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AggregateAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 2539966167342852544L;

	private Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations = new HashMap<SDFAttribute, Map<AggregateFunction, SDFAttribute>>();

	private List<SDFAttribute> groupingAttributes = new ArrayList<SDFAttribute>();

	private SDFAttributeList outputSchema = null;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((aggregations == null) ? 0 : aggregations.hashCode());
		result = prime
				* result
				+ ((groupingAttributes == null) ? 0 : groupingAttributes
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AggregateAO other = (AggregateAO) obj;
		if (aggregations == null) {
			if (other.aggregations != null)
				return false;
		} else if (!aggregations.equals(other.aggregations))
			return false;
		if (groupingAttributes == null) {
			if (other.groupingAttributes != null)
				return false;
		} else if (!groupingAttributes.equals(other.groupingAttributes))
			return false;
		return true;
	}
	
	public AggregateAO() {
		super();
		aggregations = new HashMap<SDFAttribute, Map<AggregateFunction, SDFAttribute>>();
		groupingAttributes = new ArrayList<SDFAttribute>();
		outputSchema = new SDFAttributeList();
	}

	public AggregateAO(AggregateAO op) {
		super(op);
		aggregations = new HashMap<SDFAttribute, Map<AggregateFunction, SDFAttribute>>(
				op.aggregations);
		groupingAttributes = new ArrayList<SDFAttribute>(op.groupingAttributes);
		outputSchema = op.outputSchema.clone();
	}

	public void addAggregation(SDFAttribute attribute,
			AggregateFunction function, SDFAttribute outAttribute) {
		if (getOutputSchema().contains(outAttribute)) {
			throw new IllegalArgumentException(
					"multiple definitions of element " + outAttribute);
		}

		getOutputSchema().add(outAttribute);
		Map<AggregateFunction, SDFAttribute> af = aggregations.get(attribute);
		if (af == null) {
			af = new HashMap<AggregateFunction, SDFAttribute>();
			aggregations.put(attribute, af);
		}
		af.put(function, outAttribute);
	}

	public Map<AggregateFunction, SDFAttribute> getAggregationFunctions(
			SDFAttribute attribute) {
		return aggregations.get(attribute);
	}

	public Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> getAggregations() {
		return this.aggregations;
	}

	public boolean hasAggregations() {
		return !this.aggregations.isEmpty();
	}

	public void addGroupingAttribute(SDFAttribute attribute) {
		if (groupingAttributes.contains(attribute)) {
			return;
		}
		groupingAttributes.add(attribute);
		getOutputSchema().add(attribute);
	}

	public List<SDFAttribute> getGroupingAttributes() {
		return groupingAttributes;
	}

	@Override
	public void setInputSchema(int pos, SDFAttributeList schema) {
		if (pos != 0) {
			throw new IllegalArgumentException("illegal input port: " + pos);
		}
		super.setInputSchema(pos, schema);
	}

	@Override
	public AggregateAO clone() {
		return new AggregateAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	
}
