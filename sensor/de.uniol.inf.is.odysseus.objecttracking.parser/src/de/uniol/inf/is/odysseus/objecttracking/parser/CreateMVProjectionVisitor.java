package de.uniol.inf.is.odysseus.objecttracking.parser;

import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ProjectMVAO;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateProjectionVisitor;

public class CreateMVProjectionVisitor implements IVisitor{

	static boolean registerd = VisitorFactory.getInstance().setVisitor(new CreateMVProjectionVisitor(), "MVProjection");
	
	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
		CreateProjectionVisitor cpv = (CreateProjectionVisitor)baseObject;
		
		ProjectMVAO project = new ProjectMVAO();
		project.subscribeTo(cpv.getTop(), cpv.getTop().getOutputSchema());
		project.setOutputSchema(cpv.getOutputSchema());
		// cannot be done if a MapAO is used, so it must be done
		// here
		project.setProjectMatrix(cpv.getProjectionMatrix());
		project.setProjectVector(cpv.getProjectionVector());

		return project;
	}

}
