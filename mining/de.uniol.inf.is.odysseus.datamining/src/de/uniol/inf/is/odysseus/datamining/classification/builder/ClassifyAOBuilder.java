package de.uniol.inf.is.odysseus.datamining.classification.builder;

import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.ClassifyAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public class ClassifyAOBuilder extends
		AbstractOperatorBuilder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 816020377240844656L;

	private static final String ATTRIBUTES = "ATTRIBUTES";
	
	protected ListParameter<SDFAttribute> attributes;
	
	private static final String LABELATTRIBUTE = "LABELATTRIBUTE";
	protected ResolvedSDFAttributeParameter labelAttribute;
	
	public ClassifyAOBuilder() {
		
		super(2, 2);
		attributes =  new ListParameter<SDFAttribute>(
				ATTRIBUTES, REQUIREMENT.MANDATORY,
				new ResolvedSDFAttributeParameter("data mining attribute",
						REQUIREMENT.MANDATORY));
		labelAttribute = new ResolvedSDFAttributeParameter(LABELATTRIBUTE,
				REQUIREMENT.OPTIONAL);
		setParameters(attributes,labelAttribute);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}
	
	@Override
	protected ILogicalOperator createOperatorInternal() {
		ClassifyAO classifyAO = new ClassifyAO();
		classifyAO.setAttributes(new SDFAttributeList(attributes
				.getValue()));
		classifyAO.setLabelAttribute(labelAttribute.getValue());
		return classifyAO;
	}

}
