package de.uniol.inf.is.odysseus.markov.operator.logical;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModelDictionary;

public class MarkovAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = 2490089798515939999L;

	private ListParameter<SDFAttribute> groupBy = new ListParameter<SDFAttribute>("GROUP_BY", REQUIREMENT.OPTIONAL, new ResolvedSDFAttributeParameter("group_by attribute",
			REQUIREMENT.OPTIONAL));

	private ListParameter<AggregateItem> aggregations = new ListParameter<AggregateItem>("AGGREGATIONS", REQUIREMENT.MANDATORY, new AggregateItemParameter("aggregation entry",
			REQUIREMENT.MANDATORY));	

	private StringParameter hmm = new StringParameter("HMM", REQUIREMENT.MANDATORY);

	public MarkovAOBuilder() {
		super(1, 1);
		super.addParameters(groupBy, aggregations, hmm);
	}

	@Override
	public IOperatorBuilder cleanCopy() {
		return new MarkovAOBuilder();
	}

	@Override
	protected boolean internalValidation() {
		boolean hasErrors = false;
		if (aggregations.hasValue()) {
			List<AggregateItem> items = aggregations.getValue();
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();

			for (AggregateItem item : items) {
				SDFAttribute attribute = item.outAttribute;
				if (attributes.contains(attribute)) {
					addError(new IllegalParameterException("dublicate attribute name:" + attribute.getAttributeName()));
					hasErrors = true;
				} else {
					attributes.add(attribute);
				}

			}
		}
		if(HiddenMarkovModelDictionary.getInstance().getHMM(this.hmm.getValue())==null){
			addError(new IllegalParameterException("Hidden markov model \""+this.hmm.getValue()+"\" does not exist!"));
			hasErrors = true;
		}
		return !hasErrors;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		HiddenMarkovModel hmm = HiddenMarkovModelDictionary.getInstance().getHMM(this.hmm.getValue());
		MarkovAO ao = new MarkovAO(hmm);
		if(groupBy.hasValue()){
			SDFSchema groupList = new SDFSchema("",groupBy.getValue());
			ao.addGroupingAttributes(groupList);
		}
		for(AggregateItem item : this.aggregations.getValue()){
			ao.addAggregation(item.inAttribute, item.aggregateFunction, item.outAttribute);
		}
		return ao;
	}

}
