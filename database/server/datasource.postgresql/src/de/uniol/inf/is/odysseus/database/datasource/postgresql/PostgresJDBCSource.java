package de.uniol.inf.is.odysseus.database.datasource.postgresql;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;


import org.osgi.service.jdbc.DataSourceFactory;

public class PostgresJDBCSource implements DataSourceFactory {

	public void start() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		// Load driver if not already done...
		Class<?> clazz = Class.forName("org.postgresql.Driver");
		clazz.newInstance();
	}

	@Override
	public DataSource createDataSource(Properties props) throws SQLException {
		return null;
	}

	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(
			Properties props) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XADataSource createXADataSource(Properties props)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Driver createDriver(Properties props) throws SQLException {
		org.postgresql.Driver driver = new org.postgresql.Driver();
		return driver;
	}

	
}
