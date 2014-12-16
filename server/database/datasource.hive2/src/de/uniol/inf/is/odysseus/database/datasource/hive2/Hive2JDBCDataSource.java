package de.uniol.inf.is.odysseus.database.datasource.hive2;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.osgi.service.jdbc.DataSourceFactory;

public class Hive2JDBCDataSource implements DataSourceFactory {

	public void start() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		// Load driver if not already done...
		Class<?> clazz = Class.forName("org.apache.hive.jdbc.HiveDriver");
		// The newInstance() call is a work around for some
		// broken Java implementations, see MySQL Connector/J documentation
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
		org.apache.hive.jdbc.HiveDriver driver = new org.apache.hive.jdbc.HiveDriver();
		return driver;
	}

}
