package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

public class DBRetrievalStrategy implements
		IRetrievalStrategy<ComplexParameterKey, Tuple> {

	private String connectionName;
	private String query;

	// Initialized in open()
	private SDFSchema dbFetchSchema;
	private PreparedStatement preparedStatement;

	public DBRetrievalStrategy(String connectionName, String query) {
		this.connectionName = connectionName;
		this.query = query;
	}

	@Override
	public Tuple get(ComplexParameterKey key) {
		return getTupleFromDB(key);
	}

	@Override
	public void open() {
		try {
			// Get Connection
			IDatabaseConnection iDatabaseConnection = DatabaseConnectionDictionary
					.getInstance().getDatabaseConnection(connectionName);
			if (iDatabaseConnection == null) {
				throw new RuntimeException("Could not find the connection '"
						+ connectionName + "'");
			}

			Connection connection = iDatabaseConnection.getConnection();

			// Prepare Statement
			preparedStatement = connection.prepareStatement(query);

			// Retrieve DB Schema
			dbFetchSchema = Conversions.createSDFSchemaByResultSetMetaData(
					preparedStatement.getMetaData());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenFailedException(e);
		}
	}

	@Override
	public void close() {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			preparedStatement = null;
		}
	}

	private Tuple getTupleFromDB(ComplexParameterKey complexParameterKey) {

		ResultSet rs = null;
		Tuple dbTuple = null;

		try {
			// Insert parameters corr. to variables in prepared statement...
			Object[] queryParameters = complexParameterKey.getQueryParameters();
			for (int i = 0; i < queryParameters.length; i++) {
				preparedStatement.setObject(i + 1, queryParameters[i]); // automapping
			}
			// ... and execute
			System.out.println("PreparedStatement: "
					+ preparedStatement.toString());
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				dbTuple = new Tuple(dbFetchSchema.size(), false);
				for (int i = 0; i < dbFetchSchema.size(); i++) {
					Object newAttribute = getWithType(rs, dbFetchSchema.get(i),
							i + 1);
					dbTuple.setAttribute(i, newAttribute);
				}
			} else {
				// do nothing, return dbTuple = null
				// (old:) throw new RuntimeException("No results for query");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Could not retrieve entry from Database", e);
		}

		return dbTuple; // null, if nothing was found
	}

	private Object getWithType(ResultSet rs, SDFAttribute sdfAttributeTarget,
			int position) throws SQLException {

		/*
		 * getObject(pos) should be sufficient for all use cases, since JDBC
		 * already uses a fitting java typemap for this method and the schema
		 * which is used for later casting is already known to the DSMS (plus
		 * tuples use an Object-Array anyway).
		 */
		return rs.getObject(position);
	}
}
