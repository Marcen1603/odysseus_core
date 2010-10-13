package de.uniol.inf.is.odysseus.storing.logicaloperator;

import java.sql.Connection;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;

public abstract class AbstractDatabaseAO extends AbstractLogicalOperator{

	private static final long serialVersionUID = 2180309648867607439L;
	private Connection connection;
	private String table = "";
	
	public AbstractDatabaseAO(Connection connection, String tableName){
		this.connection = connection;
		this.table = tableName;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public String getTable() {
		return table;
	}	
}
