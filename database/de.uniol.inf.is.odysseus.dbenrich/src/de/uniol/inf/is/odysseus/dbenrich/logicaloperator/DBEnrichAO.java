package de.uniol.inf.is.odysseus.dbenrich.logicaloperator;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="DBENRICH")
public class DBEnrichAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7829900765149450355L;
	
	static Logger logger = LoggerFactory.getLogger(DBEnrichAO.class);

	/* Directly passed through attributes */
	private String connectionName;
	private String query;
	private List<String> attributes; // for pstmt parameters
	private boolean multiTupleOutput = false; // n output tuples for n db results
	private boolean noCache = false;
	private int cacheSize = 20;
	private long expirationTime = 1000 * 60 * 5; // 5 Minuten
	private String removalStrategy = "fifo";

	public DBEnrichAO() {
		super();
	}

	public DBEnrichAO(DBEnrichAO dBEnrichAO) {
		super(dBEnrichAO);
		connectionName = dBEnrichAO.connectionName;
		query = dBEnrichAO.query;
		attributes = dBEnrichAO.attributes;
		multiTupleOutput = dBEnrichAO.multiTupleOutput;
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
		// System.out.println("isValid() call; " + getDebugString());
		boolean valid = true;

		if (cacheSize < 1) {
			addError(new IllegalParameterException(
					"CacheSize must be at least 1. You may " +
					"use the parameter \"nocache='True'\" instead."));
			valid = false;
		}

		if (expirationTime < 0) {
			addError(new IllegalParameterException(
					"ExpirationTime may not be negative."));
			valid = false;
		}

		return valid;
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

			logger.debug(resultSetMetaDataToString(resultSetMetaData));
			logger.debug(sdfSchemaToString(getInputSchema(), 
					"Input stream: "+getInputSchema().getURI()));
			logger.debug(sdfSchemaToString(getOutputSchema(), 
					"Output stream: "+getInputSchema().getURI()));
		} catch (SQLException e) {
			throw new RuntimeException("Error while analysing SQL query", e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (Exception e) { }
			}
		}
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return super.getOutputSchemaIntern(pos);
	}

	private static String sdfSchemaToString(SDFSchema schema, String identifier) {
		StringBuilder sb = new StringBuilder(140);
		sb.append(identifier + ", Schema=[");
		boolean addComma = false;
		for (SDFAttribute attribute : schema) {
			if(addComma) sb.append(",");
			sb.append("['" + attribute.getURI() + "','" + attribute.getDatatype() + "']");
			addComma = true;
		}
		sb.append("]");
		return sb.toString();
	}

	private static String resultSetMetaDataToString(ResultSetMetaData resultSetMetaData) {
		StringBuilder sb = new StringBuilder(80);
		try {
			sb.append("SQL-ResultSetMetaData: [");
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				if(i>1) sb.append(",");
				sb.append("[" + resultSetMetaData.getColumnLabel(i) + ","
						+ resultSetMetaData.getColumnType(i) + "]");
			}
			sb.append("]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// private String getDebugString() {
	// 	String variablesStr = "{";
	// 	for (String var : attributes) {
	// 		variablesStr += "'" + var + "' ";
	// 	}
	// 	variablesStr = variablesStr.trim() + "}";
	// 	return String.format("connectionName:%s, query:%s, variables:%s, multiTupleOutput:%s, noCache:%s, cacheSize:%s, expirationTime:%s, removalStrategy:%s", connectionName, query, variablesStr, multiTupleOutput, noCache, cacheSize, expirationTime, removalStrategy);
	// }

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
	
	public boolean isMultiTupleOutput() {
		return multiTupleOutput;
	}

	@Parameter(type = BooleanParameter.class, optional=true, name = "multiTupleOutput")
	public void setMultiTupleOutput(boolean multiTupleOutput) {
		this.multiTupleOutput = multiTupleOutput;
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