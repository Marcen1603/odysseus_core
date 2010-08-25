package de.uniol.inf.is.odysseus.cep.sase;

import java.util.List;

public class AssignExpression extends AttributeExpression{
	PathAttribute attributToAssign;
	
	public AssignExpression(PathAttribute attributToAssign, List<PathAttribute> attributes,
			String fullExpression) {
		super(attributes, fullExpression);
		this.attributToAssign = attributToAssign;
	}
	
	public PathAttribute getAttributToAssign() {
		return attributToAssign;
	}

}
