package de.uniol.inf.is.odysseus.dbenrich.physicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.dbenrich.util.Conversions;

public class DBEnrichPO<T extends ITimeInterval> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	// Initialized in the constructor
	private final String connectionName;
	private final String query;
	private final List<String> variables;
	private final int cacheSize;
	private final String cachingStrategy;

	// Available after process_open()
	private SDFSchema dbFetchSchema;
	private PreparedStatement preparedStatement;
	private Object cacheManager;

	public DBEnrichPO(String connectionName, String query,
			List<String> variables, int cacheSize, String cachingStrategy) {
		super();
		this.connectionName = connectionName;
		this.query = query;
		this.variables = variables;
		this.cacheSize = cacheSize;
		this.cachingStrategy = cachingStrategy;
	}

	public DBEnrichPO(DBEnrichPO<T> dBEnrichPO) {
		super(dBEnrichPO);
		this.connectionName = dBEnrichPO.connectionName;
		this.query = dBEnrichPO.query;
		this.variables = dBEnrichPO.variables;
		this.cacheSize = dBEnrichPO.cacheSize;
		this.cachingStrategy = dBEnrichPO.cachingStrategy;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT; // TODO eventuell modified
	}

	@Override
	protected void process_next(Tuple<T> inputTuple, int port) {

		System.out.println("(-----------------------------------");

		/*
		// Nur zum Testen
		System.out.println("Hallo");
		try {
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				String s = rs.getString(1);
				System.out.println("DB-Ergebnis(1): " + s);
			}

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			System.out.println("Anzahl Spalten: " + columnCount);
			for(int i=1; i<=columnCount; i++) { // columns count from 1 upwards
				System.out.printf("Spalte %s: %s vom Typ %s\n", i, rsmd.getColumnName(i), rsmd.getColumnType(i));
			    switch(rsmd.getColumnType(i)) {
			    	case java.sql.Types.INTEGER:
			    		System.out.println("INTEGER"); break;
			    	case java.sql.Types.VARCHAR:
			    		System.out.println("VARCHAR"); break;
			    	default:
			    		System.out.println("WAS ANDERES");
			    }
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */


		System.out.println("Tuple(before): "+inputTuple);

		// Temporary: Just append, keep Metadata
		// Later: Maybe use Strategy, (clone metadata?)
		// cache.get == null { get, put} oder cache.get, selbst put?
		Tuple dbTupel = getTupleFromDB(inputTuple);
		for(Object attribute : dbTupel.getAttributes()) {
			inputTuple.append(attribute, false);
		}

		System.out.println("Tuple(after):  "+inputTuple);

		transfer(inputTuple, port);

		System.out.println("-----------------------------------)");
	}

	private Tuple getTupleFromDB(Tuple<T> inputTuple) {

		ResultSet rs = null;
		Tuple dbTuple = new Tuple(dbFetchSchema.size(), false); // false ok?

		try {
			// Insert parameters corr. to variables in prepared statement...
			for(int i=0; i<variables.size(); i++) {
				String variable  = variables.get(i);

				// Get desired parameter from input tuple
				// TODO a simple way
				SDFAttribute attribute = getOutputSchema().findAttribute(variable);
				if(attribute==null) {
					throw new RuntimeException("Could not find attribute '" + variable +"' in input tuple.");
				}
				int parameterPosition = getOutputSchema().indexOf(attribute);
				// String parameter = inputTuple.getAttribute(parameterPosition).toString();
				Object parameter = inputTuple.getAttribute(parameterPosition);

				// System.out.printf("i:%s, Variable:%s, Position:%s, Parameter:%s\n", i, variable, parameterPosition, parameter);

				// preparedStatement.setString(i+1, parameter);
				preparedStatement.setObject(i+1, parameter); // automapping, works better with postgre
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

	private Object getWithType(ResultSet rs, SDFAttribute sdfAttributeTarget, int position) throws SQLException {

		/*
		 * getObject(pos) should be sufficient for all use cases, since JDBC
		 * already uses a fitting java typemap for this method and the schema
		 * which is used for later casting is already known to the DSMS (plus
		 * tuples use an Object-Array anyway).
		 */
		return rs.getObject(position);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO create Cache
		cacheManager = new Object();

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

		System.out.println("executed process_open()");
	}

	@Override
	protected void process_close() {
		// TODO Close all (future) ressources

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("executed process_close()");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp); // TODO ggf. aendern
	}

	@Override
	public AbstractPipe<Tuple<T>, Tuple<T>> clone() {
		return new DBEnrichPO<T>(this);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		// TODO Auto-generated method stub
		return false;
	}
}
