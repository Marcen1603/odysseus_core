package de.uniol.inf.is.odysseus.datamining.clustering.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.LeaderAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This class is the builder class for the {@link LeaderAO}. It specifies a
 * parameter for the leader algorithms threshold value and checks that the value is
 * greater then zero.
 * 
 * @author Kolja Blohm
 * 
 */
public class LeaderAOBuilder extends AbstractClusteringAOBuilder {

	private static final long serialVersionUID = 7536703160376357750L;
	private static final String THRESHOLD = "THRESHOLD";
	private DirectParameter<Double> threshold;

	/**
	 * Creates a new LeaderAOBuilder.
	 */
	public LeaderAOBuilder() {

		threshold = new DirectParameter<Double>(THRESHOLD,
				REQUIREMENT.MANDATORY);
		setParameters(threshold);
	}

	/**
	 * Validates that the threshold is greater then zero and initiates an error
	 * otherwise.
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.builder.AbstractClusteringAOBuilder#internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		if (threshold.getValue() <= 0) {
			addError(new AttributeOutOfRangeException(threshold.getName(),
					"has to be greater then zero"));
			return false;
		}
		return super.internalValidation();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder#createOperatorInternal()
	 */
	@Override
	protected ILogicalOperator createOperatorInternal() {
		
		//create and initialize the LeaderAO
		LeaderAO leaderAO = new LeaderAO();
		leaderAO.setAttributes(new SDFAttributeList(attributes.getValue()));
		leaderAO.setThreshold(threshold.getValue());

		return leaderAO;
	}

}
