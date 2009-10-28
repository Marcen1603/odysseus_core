package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ProjectMVAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ProjectMVAOFactory {
	public ProjectAO createProjectMVAO(ILogicalOperator top, SDFAttributeList inputSchema, SDFAttributeList outputSchema, double[][] projectionMatrix, double[] projectionVector) {
		ProjectMVAO project = new ProjectMVAO();
		project.subscribeTo(top);
		project.setInputSchema(inputSchema);
		project.setOutputSchema(outputSchema);
		// cannot be done if a MapAO is used, so it must be done
		// here
		project.setProjectMatrix(projectionMatrix);
		project.setProjectVector(projectionVector);

		return project;
	}
}
