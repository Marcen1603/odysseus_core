package de.uniol.inf.is.odysseus.datamining.clustering.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AbstractDataMiningAOBuilder;
import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.LeaderAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class LeaderAOBuilder extends AbstractDataMiningAOBuilder{

	
	
	private static final String THRESHOLD = "THRESHOLD";
	private DirectParameter<Double> threshold;
	
	public LeaderAOBuilder() {
		
		
		threshold = new DirectParameter<Double>(THRESHOLD, REQUIREMENT.MANDATORY);
		setParameters(threshold);
	}

	@Override
	protected boolean internalValidation() {
		if(threshold.getValue() <= 0){
			addError(new AttributeOutOfRangeException(threshold.getName(),"has to be greater then zero"));
			return false;
		}
		return super.internalValidation();
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		LeaderAO leaderAO = new LeaderAO();
		leaderAO.setAttributes(new SDFAttributeList(attributes.getValue()));
		leaderAO.setThreshold(threshold.getValue());
		
		return leaderAO;
	}

}
