package de.uniol.inf.is.odysseus.datamining.classification.builder;

import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.ClassifyAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This class is the builder class to define parameters used to create the
 * logical classification operator
 * 
 * @author Sven Vorlauf
 * 
 */
public class ClassifyAOBuilder extends AbstractOperatorBuilder {
	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 816020377240844656L;

	/**
	 * the name of the parameter defining the attributes to use for
	 * classification
	 */
	private static final String ATTRIBUTES = "ATTRIBUTES";

	/**
	 * the parameter defining the attributes to use for classification
	 */
	protected ListParameter<SDFAttribute> attributes;

	/**
	 * the name of the parameter defining the attribute for the class
	 */
	private static final String LABEL_ATTRIBUTE = "LABELATTRIBUTE";

	/**
	 * the parameter defining the attribute for the class
	 */
	protected ResolvedSDFAttributeParameter labelAttribute;

	/**
	 * create a new builder
	 */
	public ClassifyAOBuilder() {

		super(2, 2);
		attributes = new ListParameter<SDFAttribute>(ATTRIBUTES,
				REQUIREMENT.MANDATORY, new ResolvedSDFAttributeParameter(
						"data mining attribute", REQUIREMENT.MANDATORY));
		labelAttribute = new ResolvedSDFAttributeParameter(LABEL_ATTRIBUTE,
				REQUIREMENT.OPTIONAL);
		setParameters(attributes, labelAttribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder
	 * #internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder
	 * #createOperatorInternal()
	 */
	@Override
	protected ILogicalOperator createOperatorInternal() {
		// create and configure the logical operator
		ClassifyAO classifyAO = new ClassifyAO();
		classifyAO.setAttributes(new SDFAttributeList(attributes.getValue()));
		classifyAO.setLabelAttribute(labelAttribute.getValue());
		return classifyAO;
	}

}
