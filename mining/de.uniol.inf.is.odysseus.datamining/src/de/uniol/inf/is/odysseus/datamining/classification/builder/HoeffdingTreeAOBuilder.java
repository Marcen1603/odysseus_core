package de.uniol.inf.is.odysseus.datamining.classification.builder;

import de.uniol.inf.is.odysseus.datamining.builder.AbstractDataMiningAOBuilder;
import de.uniol.inf.is.odysseus.datamining.builder.AttributeOutOfRangeException;
import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.HoeffdingTreeAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.DirectParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HoeffdingTreeAOBuilder extends AbstractDataMiningAOBuilder {

	private static final String LABELATTRIBUTE = "LABELATTRIBUTE";
	private ResolvedSDFAttributeParameter labelAttribute;

	private static final String PROBABILITY = "PROBABILITY";
	private DirectParameter<Double> probability;

	public HoeffdingTreeAOBuilder() {
		labelAttribute = new ResolvedSDFAttributeParameter(LABELATTRIBUTE,
				REQUIREMENT.MANDATORY);
		probability = new DirectParameter<Double>(PROBABILITY,
				REQUIREMENT.MANDATORY);
		setParameters(labelAttribute, probability);
	}
	
	@Override
	protected boolean internalValidation() {
		if(probability.getValue() < 0 || probability.getValue() > 1){
			addError(new AttributeOutOfRangeException(probability.getName(),"has to be between 0 and 1"));
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
		return hoeffdingTreeAO;
	}

}
