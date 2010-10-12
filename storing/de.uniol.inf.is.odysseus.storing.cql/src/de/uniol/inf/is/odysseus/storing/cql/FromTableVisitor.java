package de.uniol.inf.is.odysseus.storing.cql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateFromDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTJdbcIdentifier;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.storing.DatabaseServiceLoader;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseAccessAO;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class FromTableVisitor {

	private User user;
	private String name;
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public Object visit(ASTCreateFromDatabase node, Object data){
		DatabaseAccessAO access = null;
		if(node.jjtGetNumChildren()>1){
			// es wurde eine db angegeben
			String jdbc = ((ASTJdbcIdentifier)node.jjtGetChild(0)).getConnection();
			String name = ((ASTIdentifier)node.jjtGetChild(1)).getName();
			access = getAccessAOForJDBC(jdbc, name, false);			
		}else{
			String name = ((ASTIdentifier)node.jjtGetChild(0)).getName();
			access = getAccessAOForDefault(name, false);
		}
		
		//***************				
		DataDictionary.getInstance().setView(name, access, user);		
		return access;
	}
	
	
	private DatabaseAccessAO getAccessAOForDefault(String tableName, boolean isTimeSensitiv){
		DatabaseAccessAO dba = new DatabaseAccessAO(getSource(name), DatabaseServiceLoader.getConnection(), tableName, isTimeSensitiv);
		return dba;
	}
	
	private DatabaseAccessAO getAccessAOForJDBC(String jdbcString, String tableName, boolean isTimeSensitiv){
		try {
			Connection con = DriverManager.getConnection(jdbcString);
			DatabaseAccessAO dba = new DatabaseAccessAO(getSource(name), con, tableName, isTimeSensitiv);
			return dba;
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return null;		
	}
	
	private SDFSource getSource(String sourceName){
		return new SDFSource(sourceName, "databaseReading");
	}
}
