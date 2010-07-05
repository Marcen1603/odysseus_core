package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ProjectAOBuilder extends AbstractOperatorBuilder {

	private static final String ATTRIBUTES = "ATTRIBUTES";
	private ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			ATTRIBUTES, REQUIREMENT.MANDATORY, new SDFAttributeParameter("",
					REQUIREMENT.MANDATORY));

	public ProjectAOBuilder() {
		setParameters(attributes);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 1);

		SDFAttributeList outputSchema = new SDFAttributeList(attributes
				.getValue());

		ProjectAO projectAO = new ProjectAO();
		projectAO.setOutputSchema(outputSchema);

		return projectAO;
	}

}
