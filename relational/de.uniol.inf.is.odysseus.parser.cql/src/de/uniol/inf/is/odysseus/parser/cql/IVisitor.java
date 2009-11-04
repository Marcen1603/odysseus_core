package de.uniol.inf.is.odysseus.parser.cql;

import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;

public interface IVisitor {

	public Object visit(SimpleNode node, Object data); 
}
