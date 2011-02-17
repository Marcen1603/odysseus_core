package de.uniol.inf.is.odysseus.datamining.state.builder;

import de.uniol.inf.is.odysseus.datamining.state.logicaloperator.RecallAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RecallAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -8826462596349794374L;
	
	private static final String ATTRIBUTES = "ATTRIBUTES";
	private ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			ATTRIBUTES, REQUIREMENT.MANDATORY,
			new ResolvedSDFAttributeParameter("check attribute",
					REQUIREMENT.MANDATORY));

	public RecallAOBuilder() {
		super(1,1);
		setParameters(attributes);
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SDFAttributeList outputSchema = new SDFAttributeList(attributes
				.getValue());

		RecallAO recallAO = new RecallAO();
		recallAO.setSchemaToCheck(outputSchema);
		return recallAO;
	}

}
