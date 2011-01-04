package de.uniol.inf.is.odysseus.datamining.classification.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.HoeffdingTreeAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HoeffdingTreeAOBuilder extends
		AbstractClassificationLearnerAOBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7070015888103484706L;

	private static final String PROBABILITY = "PROBABILITY";
	private DirectParameter<Double> probability;
	private static final String TIE = "TIE";
	private DirectParameter<Double> tie;

	public HoeffdingTreeAOBuilder() {

		probability = new DirectParameter<Double>(PROBABILITY,
				REQUIREMENT.MANDATORY);
		tie = new DirectParameter<Double>(TIE, REQUIREMENT.OPTIONAL);

		setParameters(probability, tie);
	}

	@Override
	protected boolean internalValidation() {
		if (probability.getValue() < 0 || probability.getValue() > 1) {
			addError(new AttributeOutOfRangeException(probability.getName(),
					"has to be between 0 and 1"));
			return false;
		}
		return super.internalValidation();
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		HoeffdingTreeAO hoeffdingTreeAO = new HoeffdingTreeAO();
		hoeffdingTreeAO.setAttributes(new SDFAttributeList(attributes
				.getValue()));
		hoeffdingTreeAO.setLabelAttribute(labelAttribute.getValue());
		hoeffdingTreeAO.setProbability(probability.getValue());
		hoeffdingTreeAO.setTie(tie.getValue());
		return hoeffdingTreeAO;
	}

}
