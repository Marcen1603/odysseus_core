package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ProjectAOBuilder extends AbstractOperatorBuilder {

	private static final String ATTRIBUTES = "ATTRIBUTES";
	private ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			ATTRIBUTES, REQUIREMENT.MANDATORY,
			new ResolvedSDFAttributeParameter("project attribute",
					REQUIREMENT.MANDATORY));

	public ProjectAOBuilder() {
		super(1, 1);
		setParameters(attributes);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SDFAttributeList outputSchema = new SDFAttributeList(attributes
				.getValue());

		ProjectAO projectAO = new ProjectAO();
		projectAO.setOutputSchema(outputSchema);

		return projectAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
