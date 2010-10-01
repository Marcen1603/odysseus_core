package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class AggregateAOBuilder extends AbstractOperatorBuilder {

	private ListParameter<SDFAttribute> groupBy = new ListParameter<SDFAttribute>(
			"group_by", REQUIREMENT.OPTIONAL,
			new ResolvedSDFAttributeParameter("group_by attribute",
					REQUIREMENT.MANDATORY));

	private ListParameter<AggregateItem> aggregations = new ListParameter<AggregateItem>(
			"AGGREGATIONS", REQUIREMENT.MANDATORY, new AggregateItemParameter(
					"aggregation entry", REQUIREMENT.MANDATORY));

	public AggregateAOBuilder() {
		super(1, 1);
		setParameters(groupBy, aggregations);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		AggregateAO ao = new AggregateAO();

		if (groupBy.hasValue()) {
			SDFAttributeList groupList = new SDFAttributeList(groupBy
					.getValue());
			ao.addGroupingAttributes(groupList);
		}

		for (AggregateItem item : aggregations.getValue()) {
			ao.addAggregation(item.inAttribute, item.aggregateFunction,
					item.outAttribute);
		}

		return ao;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
