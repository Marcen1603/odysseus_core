package de.uniol.inf.is.odysseus.dbenrich.logicaloperator;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="DBENRICH")
public class DBEnrichAO extends UnaryLogicalOp {

	/* Directly passed through attributes */
	private String connectionName;
	private String query;
	private List<String> attributes; // for pstmt parameters
	private int cacheSize = 20;
	private String replacementStrategy = "myStrategy";

	///* Available after initialize() */
	///* The ordered attributes, that are expected after an db fetch */
	//private SDFSchema dbFetchSchema;

	// TODO projekt-imports reduzieren

	private static final long serialVersionUID = -3850263953852415445L;

	public DBEnrichAO() {
		super();
	}

	public DBEnrichAO(DBEnrichAO dBEnrichAO) {
		super(dBEnrichAO);
		connectionName = dBEnrichAO.connectionName;
		query = dBEnrichAO.query;
		attributes = dBEnrichAO.attributes;
		cacheSize = dBEnrichAO.cacheSize;
		replacementStrategy = dBEnrichAO.replacementStrategy;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DBEnrichAO(this);
	}

	@Override
	public boolean isValid() {
		System.out.println("isValid() call; " + getDebugString());
		// TODO Parameter überprüfen
		// prüfen, ob variables in inputSchema
		// Existenz von PrepStmt prüfen

		return true;
	}

	@Override
	public void initialize() {
		/* Create the preparedStatement and calculate the outputSchema
		 * accordingly */

		IDatabaseConnection iDatabaseConnection = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(connectionName);
		if(iDatabaseConnection == null) {
			throw new RuntimeException("Could not find the connection '"+connectionName+"'");
		}

		PreparedStatement preparedStatement = null;
		try {
			Connection connection = iDatabaseConnection.getConnection(); // jdbc
			preparedStatement = connection.prepareStatement(query);

			ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();

			SDFSchema dbFetchSchema = Conversions.createSDFSchemaByResultSetMetaData(resultSetMetaData);

			SDFSchema outputSchema = SDFSchema.union(getInputSchema(), dbFetchSchema);

			setOutputSchema(outputSchema);

			printMetaData(resultSetMetaData);
			printSchema(getInputSchema(), "Input_"+getInputSchema().getURI());
			printSchema(getOutputSchema(), "Output_"+getInputSchema().getURI());
		} catch (SQLException e) {
			throw new RuntimeException("Error while analysing SQL query", e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (Exception e) { }
			}
		}

		System.out.println("executed initialize()");
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return super.getOutputSchemaIntern(pos);
	}

	private static void printSchema(SDFSchema schema, String identifier) { // TODO DELETE
		System.out.println(identifier + " {");
		for(SDFAttribute attribute : schema) {
			System.out.println("\t" + attribute.getURI() + ": " + attribute.getDatatype());
		}
		System.out.println("}");
	}

	private static void printMetaData(ResultSetMetaData resultSetMetaData) { // TODO DELETE
		try {
			System.out.print("SQL-ResultSetMetaData: ");
			for (int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
				System.out.printf("[%s,%s] ",
						resultSetMetaData.getColumnLabel(i),
						resultSetMetaData.getColumnType(i));
			}
			System.out.println();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getDebugString() { // TODO DELETE
		String variablesStr = "{";
		for(String var : attributes) {
			variablesStr += "'" + var + "' ";
		}
		variablesStr = variablesStr.trim() + "}";
		return String.format("connectionName:%s, query:%s, variables:%s, cacheSize:%s, replacementStrategy:%s", connectionName, query, variablesStr, cacheSize, replacementStrategy);
	}

	// Getters / Setters below
	public String getConnectionName() {
		return connectionName;
	}

	@Parameter(type = StringParameter.class, name = "connection")
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getQuery() {
		return query;
	}

	@Parameter(type = StringParameter.class, name = "query")
	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	@Parameter(type = StringParameter.class, name = "attributes", isList = true)
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public int getCacheSize() {
		return cacheSize;
	}

	@Parameter(type = IntegerParameter.class, optional=true, name = "cacheSize")
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	public String getReplacementStrategy() {
		return replacementStrategy;
	}

	@Parameter(type = StringParameter.class, optional=true, name = "replacementStrategy")
	public void setReplacementStrategy(String replacementStrategy) {
		this.replacementStrategy = replacementStrategy;
	}

	//	public SDFSchema getDbFetchSchema() {
	//		return dbFetchSchema;
	//	}
}