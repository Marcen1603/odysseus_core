package de.uniol.inf.is.odysseus.objecttracking.parser;

import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateProjectionVisitor;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class CreateMVProjectionVisitor implements IVisitor{
	
	static boolean registerd = VisitorFactory.getInstance().setVisitor(new CreateMVProjectionVisitor(), "MVProjection");
	
	User user = null;
	
	@Override
	public void setUser(User user) {
		this.user = user;	
	}
	
	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
		CreateProjectionVisitor cpv = (CreateProjectionVisitor)baseObject;
		
		ObjectTrackingProjectAO project = new ObjectTrackingProjectAO();
		project.subscribeTo(cpv.getTop(), cpv.getTop().getOutputSchema());
		project.setOutputSchema(cpv.getOutputSchema());
		return project;
	}

}
