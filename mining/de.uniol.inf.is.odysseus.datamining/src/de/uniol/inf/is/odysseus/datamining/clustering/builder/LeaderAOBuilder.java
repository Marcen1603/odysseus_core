package de.uniol.inf.is.odysseus.datamining.clustering.builder;

import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.LeaderAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class LeaderAOBuilder extends AbstractOperatorBuilder{

	
	private static final String ATTRIBUTES = "ATTRIBUTES";
	private static final String THRESHOLD = "THRESHOLD";
	private ListParameter<SDFAttribute> attributes;
	private DirectParameter<Double> threshold;
	
	public LeaderAOBuilder() {
		super(1, 1);
		attributes =  new ListParameter<SDFAttribute>(
				ATTRIBUTES, REQUIREMENT.MANDATORY,
				new ResolvedSDFAttributeParameter("leader attribute",
						REQUIREMENT.MANDATORY));
		threshold = new DirectParameter<Double>(THRESHOLD, REQUIREMENT.MANDATORY);
		setParameters(attributes,threshold);
	}

	@Override
	protected boolean internalValidation() {
		
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		LeaderAO leaderAO = new LeaderAO();
		leaderAO.setAttributes(new SDFAttributeList(attributes.getValue()));
		leaderAO.setThreshold(threshold.getValue());
		
		return leaderAO;
	}

}
