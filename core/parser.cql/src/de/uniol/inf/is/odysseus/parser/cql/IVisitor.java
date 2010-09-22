package de.uniol.inf.is.odysseus.parser.cql;

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;

public interface IVisitor {

	public Object visit(SimpleNode node, Object data, Object baseObject); 
	public void setUser(User user);
}
