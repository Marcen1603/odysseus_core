package de.uniol.inf.is.odysseus.datamining.builder;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;


public abstract class AbstractDataMiningAOBuilder extends
		AbstractOperatorBuilder {
	private static final String ATTRIBUTES = "ATTRIBUTES";
	
	protected ListParameter<SDFAttribute> attributes;
	
	public AbstractDataMiningAOBuilder() {
		
		super(1, 1);
		attributes =  new ListParameter<SDFAttribute>(
				ATTRIBUTES, REQUIREMENT.MANDATORY,
				new ResolvedSDFAttributeParameter("data mining attribute",
						REQUIREMENT.MANDATORY));
		setParameters(attributes);
	}

	@Override
	protected boolean internalValidation() {
		Iterator<SDFAttribute> iter = attributes.getValue().iterator();
		while(iter.hasNext()){
			SDFAttribute attribute = iter.next();
			if(!SDFDatatypes.isNumerical(attribute.getDatatype())){
				addError(new NonNumericAttributeException(attribute));
				return false;
			}
		}
		return true;
	}

}
