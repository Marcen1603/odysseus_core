package de.uniol.inf.is.odysseus.database.datasource.oracle;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.xa.client.OracleXADataSource;

import org.osgi.service.jdbc.DataSourceFactory;

public class OracleJDBCSource implements DataSourceFactory {

	public void start() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		// Load driver if not already done...
		Class<?> clazz = Class.forName("oracle.jdbc.OracleDriver");
		// The newInstance() call is a work around for some
		// broken Java implementations, see MySQL Connector/J documentation
		clazz.newInstance();
	}

	@Override
	public DataSource createDataSource(Properties props) throws SQLException {
		OracleDataSource source = new OracleDataSource();
		setup(source,props);
		return source;
	}

	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(
			Properties props) throws SQLException {
		OracleConnectionPoolDataSource source = new OracleConnectionPoolDataSource();
		setup(source, props);
		return source;
	}

	@Override
	public XADataSource createXADataSource(Properties props)
			throws SQLException {
		OracleXADataSource source = new OracleXADataSource();
		setupXSource(source,props);
		return source;
	}

	@Override
	public Driver createDriver(Properties props) throws SQLException {
		oracle.jdbc.OracleDriver driver = new oracle.jdbc.OracleDriver();
		return driver;
	}
	
	/**
	 * Setups the basic properties for {@link DataSource}s
	 */
	private void setup(OracleDataSource source, Properties props) {
		if (props == null) {
			return;
		}
		if (props.containsKey(JDBC_DATABASE_NAME)) {
			source.setDatabaseName(props.getProperty(JDBC_DATABASE_NAME));
		}
		if (props.containsKey(JDBC_DATASOURCE_NAME)) {
			// not supported?
		}
		if (props.containsKey(JDBC_DESCRIPTION)) {
			// not suported?
		}
		if (props.containsKey(JDBC_NETWORK_PROTOCOL)) {
			// not supported?
		}
		if (props.containsKey(JDBC_PASSWORD)) {
			source.setPassword(props.getProperty(JDBC_PASSWORD));
		}
		if (props.containsKey(JDBC_PORT_NUMBER)) {
			source.setPortNumber(Integer.parseInt(props
					.getProperty(JDBC_PORT_NUMBER)));
		}
		if (props.containsKey(JDBC_ROLE_NAME)) {
			// not supported?
		}
		if (props.containsKey(JDBC_SERVER_NAME)) {
			source.setServerName(props.getProperty(JDBC_SERVER_NAME));
		}
		if (props.containsKey(JDBC_URL)) {
			source.setURL(props.getProperty(JDBC_URL));
		}
		if (props.containsKey(JDBC_USER)) {
			source.setUser(props.getProperty(JDBC_USER));
		}
	}

	/**
	 * Setup the basic and extended properties for {@link XADataSource}s and
	 * {@link ConnectionPoolDataSource}s
	 */
	private void setupXSource(OracleXADataSource source, Properties props) {
		if (props == null) {
			return;
		}
		setup(source, props);
		if (props.containsKey(JDBC_INITIAL_POOL_SIZE)) {
			// not supported?
		}
		if (props.containsKey(JDBC_MAX_IDLE_TIME)) {
			// not supported?
		}
		if (props.containsKey(JDBC_MAX_STATEMENTS)) {
			// not supported?
		}
		if (props.containsKey(JDBC_MAX_POOL_SIZE)) {
			// not supported?
		}
		if (props.containsKey(JDBC_MIN_POOL_SIZE)) {
			// not supported?
		}
	}

}
