package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

public class DBRetrievalStrategy implements IRetrievalStrategy<ComplexParameterKey, Tuple> {
	
	private String connectionName;
	private String query;
	private List<String> variables;
	
	/** @deprecated */
	private SDFSchema opOutputSchema;
	
	// Initialized in open()
	private SDFSchema dbFetchSchema;
	private PreparedStatement preparedStatement;

	public DBRetrievalStrategy(String connectionName, String query, List<String> variables, SDFSchema opOutputSchema) {
		this.connectionName = connectionName;
		this.query = query;
		this.variables = variables;
		this.opOutputSchema = opOutputSchema;
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
			if(iDatabaseConnection == null) {
				throw new RuntimeException("Could not find the connection '"+connectionName+"'");
			}

			Connection connection = iDatabaseConnection.getConnection();

			// Prepare Statement
			preparedStatement = connection.prepareStatement(query);

			// Retrieve DB Schema
			dbFetchSchema = Conversions.createSDFSchemaByResultSetMetaData(preparedStatement.getMetaData());
		} catch(Exception e) {
			e.printStackTrace();
			throw new OpenFailedException(e);
		}
		System.out.println("open dbret");
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
		System.out.println("close dbret");
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO vielleicht statt close (und open in konstruktor)
		super.finalize();
	}
	
	
	
	
	
	
	private Tuple getTupleFromDB(ComplexParameterKey complexParameterKey) { // FIXME (Tuple<T> inputTuple) {

		ResultSet rs = null;
		Tuple dbTuple = new Tuple(dbFetchSchema.size(), false); // false ok?

		try {
			// Insert parameters corr. to variables in prepared statement...
			Object[] queryParameters = complexParameterKey.getQueryParameters();
			for(int i=0; i<queryParameters.length; i++) {
				preparedStatement.setObject(i+1, queryParameters[i]); // automapping, works better with postgre
			}
			// ... and execute
			System.out.println("PreparedStatement: " + preparedStatement.toString());
			rs = preparedStatement.executeQuery();


			if(rs.next()) {
				for(int i=0; i<dbFetchSchema.size(); i++) {
					Object newAttribute = getWithType(rs, dbFetchSchema.get(i), i+1);
					dbTuple.setAttribute(i, newAttribute);
				}
			} else {
				throw new RuntimeException("No results for query");
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not retrieve entry from Database", e);
		}

		return dbTuple;
	}
	
	
//	private Tuple getTupleFromDB(Tuple inputTuple) { // FIXME (Tuple<T> inputTuple) {
//
//		ResultSet rs = null;
//		Tuple dbTuple = new Tuple(dbFetchSchema.size(), false); // false ok?
//
//		try {
//			// Insert parameters corr. to variables in prepared statement...
//			for(int i=0; i<variables.size(); i++) {
//				String variable  = variables.get(i);
//
//				// Get desired parameter from input tuple
//				// TODO remember the positions
//				SDFAttribute attribute = opOutputSchema.findAttribute(variable);
//				if(attribute==null) {
//					throw new RuntimeException("Could not find attribute '" + variable +"' in input tuple.");
//				}
//				int parameterPosition = opOutputSchema.indexOf(attribute);
//				// String parameter = inputTuple.getAttribute(parameterPosition).toString();
//				Object parameter = inputTuple.getAttribute(parameterPosition);
//
//				// System.out.printf("i:%s, Variable:%s, Position:%s, Parameter:%s\n", i, variable, parameterPosition, parameter);
//
//				// preparedStatement.setString(i+1, parameter);
//				preparedStatement.setObject(i+1, parameter); // automapping, works better with postgre
//			}
//			// ... and execute
//			System.out.println("PreparedStatement: " + preparedStatement.toString());
//			rs = preparedStatement.executeQuery();
//
//
//			if(rs.next()) {
//				for(int i=0; i<dbFetchSchema.size(); i++) {
//					Object newAttribute = getWithType(rs, dbFetchSchema.get(i), i+1);
//					dbTuple.setAttribute(i, newAttribute);
//				}
//			} else {
//				throw new RuntimeException("No results for query");
//			}
//		} catch(SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException("Could not retrieve entry from Database", e);
//		}
//
//		return dbTuple;
//	}

	private Object getWithType(ResultSet rs, SDFAttribute sdfAttributeTarget, int position) throws SQLException {

		/*
		 * getObject(pos) should be sufficient for all use cases, since JDBC
		 * already uses a fitting java typemap for this method and the schema
		 * which is used for later casting is already known to the DSMS (plus
		 * tuples use an Object-Array anyway).
		 */
		return rs.getObject(position);
	}
}
