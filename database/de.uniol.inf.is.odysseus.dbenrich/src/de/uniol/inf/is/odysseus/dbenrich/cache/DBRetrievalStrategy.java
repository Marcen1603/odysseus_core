package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.IRetrievalStrategy;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

/**
 * A retrieval strategy, that uses a jdbc connection / sql database as its
 * data source.
 */
public class DBRetrievalStrategy implements
		IRetrievalStrategy<ComplexParameterKey, List<IStreamObject<?>>> {

	private String connectionName;
	private String query;
	private boolean multiTupleOutput;

	// Initialized in open()
	private SDFSchema dbFetchSchema;
	private PreparedStatement preparedStatement;

	public DBRetrievalStrategy(String connectionName, String query, boolean multiTupleOutput) {
		this.connectionName = connectionName;
		this.query = query;
		this.multiTupleOutput = multiTupleOutput;
	}

	public DBRetrievalStrategy(DBRetrievalStrategy dbRetrievalStrategy) {
		this.connectionName = dbRetrievalStrategy.connectionName;
		this.query = dbRetrievalStrategy.query;
		this.multiTupleOutput = dbRetrievalStrategy.multiTupleOutput;
	}

	@Override
	public List<IStreamObject<?>> get(ComplexParameterKey key) {
		return getTuplesFromDB(key);
	}

	@Override
	public void open() {
		try {
			if (preparedStatement == null || preparedStatement.isClosed()
					|| dbFetchSchema == null) {
				// Get Connection
				IDatabaseConnection iDatabaseConnection = DatabaseConnectionDictionary
						.getInstance().getDatabaseConnection(connectionName);
				if (iDatabaseConnection == null) {
					throw new RuntimeException(
							"Could not find the connection " +
							"'" + connectionName + "'");
				}

				Connection connection = iDatabaseConnection.getConnection();

				// Prepare Statement
				preparedStatement = connection.prepareStatement(query);

				// Retrieve DB Schema
				dbFetchSchema = Conversions.createSDFSchemaByResultSetMetaData(
						preparedStatement.getMetaData());
			}
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
		dbFetchSchema = null;
	}

	private List<IStreamObject<?>> getTuplesFromDB(ComplexParameterKey complexParameterKey) {

		ResultSet rs = null;
		/* Will be converted to an array at the end; replace it with a 
		 * more efficient data structure, if you find one for this use */
		List<IStreamObject<?>> dbTuples = new ArrayList<>();

		try {
			// Insert parameters corresponding to variables in prepared statement...
			Object[] queryParameters = complexParameterKey.getQueryParameters();
			for (int i = 0; i < queryParameters.length; i++) {
				preparedStatement.setObject(i + 1, queryParameters[i]); // automapping
			}
			// ... and execute
			// System.out.println("PreparedStatement: " + preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				Tuple<?> dbTuple = new Tuple<>(dbFetchSchema.size(), false);
				for (int i = 0; i < dbFetchSchema.size(); i++) {
					Object newAttribute = getWithType(rs, dbFetchSchema.get(i),
							i + 1);
					dbTuple.setAttribute(i, newAttribute);
				}
				
					dbTuples.add(dbTuple);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Could not retrieve entry from Database", e);
		}

		return dbTuples;
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
	
	@Override
	public DBRetrievalStrategy clone() {
		return new DBRetrievalStrategy(this);
	}
}
