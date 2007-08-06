package mg.dynaquest.monitor.responseprediction.preprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mg.dynaquest.queryexecution.event.POEventType;

/**
 * Class for filtering out trailing '\0' characters getting input from a Reader.
 * This is needed as the database containing the event history stores plans in
 * clobs, which are filled with '\0'. Includes a workaround for old versions of
 * DynaQuest, which produced wrong xml for plans.
 * 
 * @author Jonas Jacobi
 */
class ClobFilter extends InputStream {
	Reader reader;

	int c;

	boolean isSpace = false;

	boolean first = false;

	public ClobFilter(Reader r) {
		this.reader = r;
	}

	@Override
	public int read() throws IOException {
		// workaround for old version of dynaquest.
		// it produced plans with a missing ' ' after a closing '\''
		// like <po ... name='...'xxx>
		if (isSpace) {
			isSpace = false;
			return ' ';
		}
		c = this.reader.read();
		if (c == 0)
			return -1;

		if (c == '\'') {
			first = !first;
			if (!first) {
				isSpace = true;
			}
			return c;
		}

		return c;
	}
}

/**
 * A event history with a database backend
 * 
 * @author Jonas Jacobi
 */
public class DatabasePOMetadataRepository implements IPOMetadatarepository {
	private Connection dbConnection;

	/**
	 * Stores for each statement wether it is currently in use or not
	 */
	private Map<Statement, Boolean> statements;

	/**
	 * Number of statements for parallel executions
	 */
	private static int statementCount = 10;

	/**
	 * Create a new DatabasePOMetadataRepository and connect to the database
	 * 
	 * @param user
	 *            database user
	 * @param password
	 *            database password
	 * @param jdbcString
	 *            jdbc connection string
	 * @param driverClass
	 *            class of the jdbc driver to be used
	 * @throws ClassNotFoundException
	 *             gets thrown if the driver class can't be loaded
	 * @throws SQLException
	 *             gets thrown if the connection to the database can't be
	 *             established
	 */
	public DatabasePOMetadataRepository(String user, String password,
			String jdbcString, String driverClass)
			throws ClassNotFoundException, SQLException {
		Class.forName(driverClass);
		this.dbConnection = DriverManager.getConnection(jdbcString, user,
				password);
		this.dbConnection.setReadOnly(true);
		this.statements = new HashMap<Statement, Boolean>();
		for (int i = 0; i < statementCount; ++i) {
			statements.put(dbConnection.createStatement(), true);
		}
	}

	public List<POEventData> getEvents(String poGuid) throws Exception {
		String query = "SELECT Time, EventType FROM  DQ_PO_EVENTS WHERE POGUID='"
				+ poGuid + "'" + " ORDER BY Time ASC";
		ResultSet result = null;
		Statement statement2 = getFreeStatement();
		List<POEventData> events = new ArrayList<POEventData>();
		statement2.setFetchSize(1000);
		statement2.setMaxRows(8000);
		result = statement2.executeQuery(query);
		while (result.next()) {
			POEventData pod = createPOEventData(poGuid, result);
			events.add(pod);
		}
		synchronized (this.statements) {
			this.statements.put(statement2, true);
			this.statements.notify();
		}
		return events;
	}

	public List<PlanOperator> getPlans() {
		return getPlans(0);
	}

	public List<PlanOperator> getPlans(long afterTimestamp) {
		return new PlanList(dbConnection, afterTimestamp);
	}

	/**
	 * Get a currently unused statement. Blocks until a statement is available.
	 * @return
	 */
	private Statement getFreeStatement() {
		synchronized (this.statements) {
			while (true) {
				for (Map.Entry<Statement, Boolean> curStatement : this.statements
						.entrySet()) {
					if (curStatement.getValue()) {
						curStatement.setValue(false);
						return curStatement.getKey();
					}
				}
				try {
					this.statements.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Generate a {@link POEventData} object out of query results
	 * @param poGuid unique identifier of the planoperator
	 * @param result ResultSet with the cursor on a valid entry
	 * @return POEventData for the one event the cursor in result points to
	 * @throws SQLException gets thrown if data could not be retrieved from result
	 */
	protected POEventData createPOEventData(String poGuid, ResultSet result)
			throws SQLException {
		return new POEventData(result.getLong("Time"), POEventType
				.valueOf(result.getString("EventType")), poGuid);
	}
}
