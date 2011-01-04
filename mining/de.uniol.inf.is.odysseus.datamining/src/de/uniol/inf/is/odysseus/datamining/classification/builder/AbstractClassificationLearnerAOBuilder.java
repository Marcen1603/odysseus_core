package de.uniol.inf.is.odysseus.datamining.classification.builder;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;


public abstract class AbstractClassificationLearnerAOBuilder extends
		AbstractOperatorBuilder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 816020377240844656L;

	private static final String ATTRIBUTES = "ATTRIBUTES";
	
	protected ListParameter<SDFAttribute> attributes;
	
	private static final String LABELATTRIBUTE = "LABELATTRIBUTE";
	protected ResolvedSDFAttributeParameter labelAttribute;
	
	public AbstractClassificationLearnerAOBuilder() {
		
		super(1, 1);
		attributes =  new ListParameter<SDFAttribute>(
				ATTRIBUTES, REQUIREMENT.MANDATORY,
				new ResolvedSDFAttributeParameter("data mining attribute",
						REQUIREMENT.MANDATORY));
		labelAttribute = new ResolvedSDFAttributeParameter(LABELATTRIBUTE,
				REQUIREMENT.MANDATORY);
		setParameters(attributes,labelAttribute);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}
}
