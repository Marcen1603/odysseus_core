package de.uniol.inf.is.odysseus.storing.cql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateFromDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseCreateOption;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseTableOptions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseTruncateOption;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInsertIntoStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTJdbcIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSaveMetaData;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.storing.DatabaseServiceLoader;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseAccessAO;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class DatabaseVisitor {

	private User caller;
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public void setUser(User user) {
		this.caller = user;
	}

	public Object visit(ASTCreateFromDatabase node, Object data) {
		DatabaseAccessAO access = null;
		if (node.jjtGetNumChildren() > 1) {
			if (node.jjtGetChild(0) instanceof ASTJdbcIdentifier) {
				String jdbc = ((ASTJdbcIdentifier) node.jjtGetChild(0)).getConnection();
				String name = ((ASTIdentifier) node.jjtGetChild(1)).getName();
				boolean sensitiv = false;
				if (node.jjtGetNumChildren() == 3) {
					sensitiv = true;
				}
				access = getAccessAOForJDBC(jdbc, name, sensitiv);
			} else {
				String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
				access = getAccessAOForDefault(name, true);
			}
		} else {
			String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
			access = getAccessAOForDefault(name, false);
		}

		// ***************
		DataDictionary.getInstance().setView(name, access, caller);
		return access;
	}

	private DatabaseAccessAO getAccessAOForDefault(String tableName, boolean isTimeSensitiv) {
		DatabaseAccessAO dba = new DatabaseAccessAO(getSource(name), DatabaseServiceLoader.getConnection(), tableName, isTimeSensitiv);
		return dba;
	}

	private DatabaseAccessAO getAccessAOForJDBC(String jdbcString, String tableName, boolean isTimeSensitiv) {
		try {
			System.err.println("Currently only Derby DB is allowed, because there are no other drivers...");
			Connection con = DriverManager.getConnection(jdbcString);
			DatabaseAccessAO dba = new DatabaseAccessAO(getSource(name), con, tableName, isTimeSensitiv);
			return dba;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private SDFSource getSource(String sourceName) {
		return new SDFSource(sourceName, "databaseReading");
	}

	public Object visit(ASTInsertIntoStatement node, Object data) {		
		boolean create = false;
		boolean truncate = false;
		boolean ifnotexists = false;
		ASTDatabaseTableOptions tableOps = (ASTDatabaseTableOptions) node.jjtGetChild(0);
		if(tableOps.jjtGetChild(0) instanceof ASTDatabaseCreateOption){
			create = true;
			name = ((ASTIdentifier)tableOps.jjtGetChild(0).jjtGetChild(0)).getName();
			if(tableOps.jjtGetChild(0).jjtGetNumChildren()>1){
				ifnotexists = true;
			}
		}else if(tableOps.jjtGetChild(0) instanceof ASTDatabaseTruncateOption){
			truncate = true;
			name = ((ASTIdentifier)tableOps.jjtGetChild(0).jjtGetChild(0)).getName();
		}else if(tableOps.jjtGetChild(0) instanceof ASTIdentifier){
			name = ((ASTIdentifier)tableOps.jjtGetChild(0)).getName();
		}
		
		
		
		
		ASTComplexSelectStatement selectStatement;
		Connection conn = DatabaseServiceLoader.getConnection();
		boolean savemetadata = false;

		if (node.jjtGetNumChildren() == 3) {
			if (node.jjtGetChild(1) instanceof ASTJdbcIdentifier) {
				String jdbcString = ((ASTJdbcIdentifier) node.jjtGetChild(0)).getConnection();
				try {
					conn = DriverManager.getConnection(jdbcString);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (node.jjtGetChild(1) instanceof ASTSaveMetaData) {
				savemetadata = true;
			}
		} else {
			if (node.jjtGetNumChildren() == 4) {
				String jdbcString = ((ASTJdbcIdentifier) node.jjtGetChild(0)).getConnection();
				try {
					conn = DriverManager.getConnection(jdbcString);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				savemetadata = true;
			}
		}

		// evaluate nested select
		int last = node.jjtGetNumChildren() - 1;
		selectStatement = (ASTComplexSelectStatement) node.jjtGetChild(last);
		CQLParser v = new CQLParser();
		v.setUser(caller);
		AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(selectStatement, null);
		DatabaseSinkAO dbSink = new DatabaseSinkAO(conn, name, savemetadata, create, truncate, ifnotexists);
		result.subscribeSink(dbSink, 0, 0, result.getOutputSchema());

		return dbSink;

	}

}
