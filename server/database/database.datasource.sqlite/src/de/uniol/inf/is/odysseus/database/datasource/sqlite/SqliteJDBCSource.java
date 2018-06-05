/**
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.database.datasource.sqlite;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.osgi.service.jdbc.DataSourceFactory;
import org.sqlite.JDBC;
import org.sqlite.SQLiteDataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

/**
 * @author Cornelius Ludmann
 *
 */
public class SqliteJDBCSource implements DataSourceFactory {

	public void start() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// Load driver if not already done...
		Class<?> clazz = Class.forName("org.sqlite.JDBC");
		clazz.newInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.jdbc.DataSourceFactory#createDataSource(java.util.
	 * Properties)
	 */
	@Override
	public DataSource createDataSource(Properties props) throws SQLException {
		SQLiteDataSource source = new SQLiteDataSource();
		setup(source, props);
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.jdbc.DataSourceFactory#createConnectionPoolDataSource(
	 * java.util.Properties)
	 */
	@Override
	public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException {
		SQLiteConnectionPoolDataSource source = new SQLiteConnectionPoolDataSource();
		setup(source, props);
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.jdbc.DataSourceFactory#createXADataSource(java.util.
	 * Properties)
	 */
	@Override
	public XADataSource createXADataSource(Properties props) throws SQLException {
		// not supported
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.jdbc.DataSourceFactory#createDriver(java.util.
	 * Properties)
	 */
	@Override
	public Driver createDriver(Properties props) throws SQLException {
		return new JDBC();
		//return DriverManager.getDriver("jdbc:sqlite:");
	}

	/**
	 * @param source
	 * @param props
	 */
	private void setup(SQLiteDataSource source, Properties props) {
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
			// not supported?
		}
		if (props.containsKey(JDBC_PORT_NUMBER)) {
			// not supported?
		}
		if (props.containsKey(JDBC_ROLE_NAME)) {
			// not supported?
		}
		if (props.containsKey(JDBC_SERVER_NAME)) {
			// not supported?
		}
		if (props.containsKey(JDBC_URL)) {
			source.setUrl(props.getProperty(JDBC_URL));
		}
		if (props.containsKey(JDBC_USER)) {
			// not supported?
		}
	}

}
