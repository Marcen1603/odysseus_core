package de.uniol.inf.is.odysseus.storing.cql;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDbTable;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class FromTableVisitor {

	private User user;
	
	public void setUser(User user){
		this.user = user;
	}
	
	public Object visit(ASTDbTable node, Object data){
		String streamName = "beispiel"; 
		String tableName = ((ASTIdentifier)node.jjtGetChild(0)).getName();
		AccessAO access = new AccessAO();
		access.setSource(new SDFSource(tableName, "databaseReading"));		
		DataDictionary.getInstance().setView(streamName, access, user);		
		return access;
	}
}
