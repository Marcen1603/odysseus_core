package de.uniol.inf.is.odysseus.dbenrich.logicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractEnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DBENRICH", doc="Enrich stream objects with information from a database.", category={LogicalOperatorCategory.ENRICH, LogicalOperatorCategory.DATABASE})
public class DBEnrichAO extends AbstractEnrichAO {

	private static final long serialVersionUID = 7829900765149450355L;

	static Logger logger = LoggerFactory.getLogger(DBEnrichAO.class);

	/* Directly passed through attributes */
	private String connectionName;
	private String query;
	private List<String> attributes; // for pstmt parameters

	public DBEnrichAO() {
		super();
	}

	public DBEnrichAO(DBEnrichAO dBEnrichAO) {
		super(dBEnrichAO);
		connectionName = dBEnrichAO.connectionName;
		query = dBEnrichAO.query;
		attributes = dBEnrichAO.attributes;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DBEnrichAO(this);
	}

	@Override
	public boolean isValid() {
		// System.out.println("isValid() call; " + getDebugString());
		boolean valid = true;

		return valid;
	}

	@Override
	public void initialize() {
		/*
		 * Create the preparedStatement and calculate the outputSchema
		 * accordingly
		 */

		IDatabaseConnection iDatabaseConnection = DatabaseConnectionDictionary
				.getDatabaseConnection(connectionName);
		if (iDatabaseConnection == null) {
			throw new RuntimeException("Could not find the connection '"
					+ connectionName + "'");
		}

		PreparedStatement preparedStatement = null;
		try {
			Connection connection = iDatabaseConnection.getConnection(); // jdbc
			preparedStatement = connection.prepareStatement(query);

			ResultSetMetaData resultSetMetaData = preparedStatement
					.getMetaData();

			SDFSchema dbFetchSchema = Conversions
					.createSDFSchemaByResultSetMetaData(resultSetMetaData);

			SDFSchema outputSchema = SDFSchema.union(getInputSchema(),
					dbFetchSchema);

			setOutputSchema(outputSchema);

			logger.debug(resultSetMetaDataToString(resultSetMetaData));
			logger.debug(sdfSchemaToString(getInputSchema(), "Input stream: "
					+ getInputSchema().getURI()));
			logger.debug(sdfSchemaToString(getOutputSchema(), "Output stream: "
					+ getInputSchema().getURI()));
		} catch (SQLException e) {
			throw new RuntimeException("Error while analysing SQL query "+e.getMessage(), e);
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int port) {
		if (recalcOutputSchemata || getOutputSchema() == null) {
			initialize();
		}
		return getOutputSchema();
	}


	private static String sdfSchemaToString(SDFSchema schema, String identifier) {
		StringBuilder sb = new StringBuilder(140);
		sb.append(identifier + ", Schema=[");
		boolean addComma = false;
		for (SDFAttribute attribute : schema) {
			if (addComma)
				sb.append(",");
			sb.append("['" + attribute.getURI() + "','"
					+ attribute.getDatatype() + "']");
			addComma = true;
		}
		sb.append("]");
		return sb.toString();
	}

	private static String resultSetMetaDataToString(
			ResultSetMetaData resultSetMetaData) {
		StringBuilder sb = new StringBuilder(80);
		try {
			sb.append("SQL-ResultSetMetaData: [");
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				if (i > 1)
					sb.append(",");
				sb.append("[" + resultSetMetaData.getColumnLabel(i) + ","
						+ resultSetMetaData.getColumnType(i) + "]");
			}
			sb.append("]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
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

}