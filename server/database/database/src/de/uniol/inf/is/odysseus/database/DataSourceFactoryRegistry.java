package de.uniol.inf.is.odysseus.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.osgi.service.jdbc.DataSourceFactory;

public class DataSourceFactoryRegistry {

	static final List<DataSourceFactory> factories = new LinkedList<>();

	public void bindDatasourceFactory(DataSourceFactory factory) {
		factories.add(factory);
	}

	public void unbindDatasourceFactory(DataSourceFactory factory) {
		factories.remove(factory);
	}

	public static Connection getConnection(String connString,
			Properties connectionProps) throws SQLException {
		
		for (DataSourceFactory f : factories) {
			Driver driver = f.createDriver(connectionProps);
			Connection conn = driver.connect(connString, connectionProps);
			if (conn != null) {
				return conn;
			}
		}

		// Fall back to old driver manager
		return DriverManager.getConnection(connString, connectionProps);

	}

}
