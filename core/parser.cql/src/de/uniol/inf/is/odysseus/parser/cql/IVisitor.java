package de.uniol.inf.is.odysseus.parser.cql;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface IVisitor {

	public Object visit(SimpleNode node, Object data, Object baseObject); 
	public void setUser(User user);
	public void setDataDictionary(IDataDictionary dd);
}
