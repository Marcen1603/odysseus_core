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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
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
	private boolean noCache = false;
	private int cacheSize = 20;
	private long expirationTime = 1000 * 60 * 5; // 5 Minuten
	private String removalStrategy = "fifo";

	///* Available after initialize() */
	///* The ordered attributes, that are expected after an db fetch */
	//private SDFSchema dbFetchSchema;

	private static final long serialVersionUID = -3850263953852415445L;

	public DBEnrichAO() {
		super();
	}

	public DBEnrichAO(DBEnrichAO dBEnrichAO) {
		super(dBEnrichAO);
		connectionName = dBEnrichAO.connectionName;
		query = dBEnrichAO.query;
		attributes = dBEnrichAO.attributes;
		noCache = dBEnrichAO.noCache;
		cacheSize = dBEnrichAO.cacheSize;
		expirationTime = dBEnrichAO.expirationTime;
		removalStrategy = dBEnrichAO.removalStrategy;
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
		return String.format("connectionName:%s, query:%s, variables:%s, noCache:%s, cacheSize:%s, expirationTime:%s, removalStrategy:%s", connectionName, query, variablesStr, noCache, cacheSize, expirationTime, removalStrategy);
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

	public String getRemovalStrategy() {
		return removalStrategy;
	}

	@Parameter(type = StringParameter.class, optional=true, name = "removalStrategy")
	public void setRemovalStrategy(String removalStrategy) {
		this.removalStrategy = removalStrategy;
	}

	public boolean isNoCache() {
		return noCache;
	}

	@Parameter(type = BooleanParameter.class, optional=true, name = "noCache")
	public void setNoCache(boolean noCache) {
		this.noCache = noCache;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	@Parameter(type = LongParameter.class, optional=true, name = "expirationTime")
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}
}