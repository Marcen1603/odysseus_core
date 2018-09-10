/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.database.derby;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.derby.jdbc.ClientDataSource;
import org.apache.derby.jdbc.ClientXADataSource;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.derby.jdbc.EmbeddedXADataSource;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DerbyJDBCSource implements DataSourceFactory {

    public DerbyJDBCSource() {
    }

    @Override
    public DataSource createDataSource(Properties props) throws SQLException {
        DataSource source;
        if ((props.containsKey(JDBC_SERVER_NAME)) && (!"".equals(props.getProperty(JDBC_SERVER_NAME)))) {
            source = new ClientDataSource();
            setup((EmbeddedConnectionPoolDataSource) source, props);
        }
        else {
            source = new EmbeddedDataSource();
            setup((EmbeddedConnectionPoolDataSource) source, props);
        }
        return source;
    }

    @Override
    public ConnectionPoolDataSource createConnectionPoolDataSource(Properties props) throws SQLException {
        ConnectionPoolDataSource source;
        if ((props.containsKey(JDBC_SERVER_NAME)) && (!"".equals(props.getProperty(JDBC_SERVER_NAME)))) {
            source = new ClientConnectionPoolDataSource();
            setup((ClientConnectionPoolDataSource) source, props);
        }
        else {
            source = new EmbeddedConnectionPoolDataSource();
            setup((EmbeddedConnectionPoolDataSource) source, props);
        }

        return source;
    }

    @Override
    public XADataSource createXADataSource(Properties props) throws SQLException {
        XADataSource source;
        if ((props.containsKey(JDBC_SERVER_NAME)) && (!"".equals(props.getProperty(JDBC_SERVER_NAME)))) {
            source = new ClientXADataSource();
            setupXSource((ClientXADataSource) source, props);
        }
        else {
            source = new EmbeddedXADataSource();
            setupXSource((EmbeddedXADataSource) source, props);
        }
        return source;
    }

    @Override
    public Driver createDriver(Properties props) throws SQLException {
        if ((props.containsKey(JDBC_SERVER_NAME)) && (!"".equals(props.getProperty(JDBC_SERVER_NAME)))) {
            return new org.apache.derby.jdbc.ClientDriver();
        }
        return new org.apache.derby.jdbc.EmbeddedDriver();
    }

    /**
     * Setups the basic properties for {@link DataSource}s
     */
    private void setup(EmbeddedConnectionPoolDataSource source, Properties props) {
        if (props == null) {
            return;
        }
        if (props.containsKey(JDBC_DATABASE_NAME)) {
            source.setDatabaseName(props.getProperty(JDBC_DATABASE_NAME));
        }
        if (props.containsKey(JDBC_DATASOURCE_NAME)) {
            source.setDataSourceName(props.getProperty(JDBC_DATASOURCE_NAME));
        }
        if (props.containsKey(JDBC_DESCRIPTION)) {
            source.setDescription(props.getProperty(JDBC_DESCRIPTION));
        }
        if (props.containsKey(JDBC_NETWORK_PROTOCOL)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_PASSWORD)) {
            source.setPassword(props.getProperty(JDBC_PASSWORD));
        }
        if (props.containsKey(JDBC_PORT_NUMBER)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_ROLE_NAME)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_SERVER_NAME)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_URL)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_USER)) {
            source.setUser(props.getProperty(JDBC_USER));
        }
    }

    /**
     * Setups the basic properties for embedded {@link DataSource}s
     */
    private void setup(EmbeddedDataSource source, Properties props) {
        if (props == null) {
            return;
        }
        if (props.containsKey(JDBC_DATABASE_NAME)) {
            source.setDatabaseName(props.getProperty(JDBC_DATABASE_NAME));
        }
        if (props.containsKey(JDBC_DATASOURCE_NAME)) {
            source.setDataSourceName(props.getProperty(JDBC_DATASOURCE_NAME));
        }
        if (props.containsKey(JDBC_DESCRIPTION)) {
            source.setDescription(props.getProperty(JDBC_DESCRIPTION));
        }
        if (props.containsKey(JDBC_NETWORK_PROTOCOL)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_PASSWORD)) {
            source.setPassword(props.getProperty(JDBC_PASSWORD));
        }
        if (props.containsKey(JDBC_PORT_NUMBER)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_ROLE_NAME)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_SERVER_NAME)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_URL)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_USER)) {
            source.setUser(props.getProperty(JDBC_USER));
        }
    }

    /**
     * Setup the basic and extended properties for {@link XADataSource}s and
     * {@link ConnectionPoolDataSource}s
     */
    private void setupXSource(EmbeddedXADataSource source, Properties props) {
        if (props == null) {
            return;
        }
        setup(source, props);
        if (props.containsKey(JDBC_INITIAL_POOL_SIZE)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MAX_IDLE_TIME)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MAX_STATEMENTS)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MAX_POOL_SIZE)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MIN_POOL_SIZE)) {
            // FIXME ckuka
        }
    }

    /**
     * Setups the basic properties for {@link DataSource}s
     */
    private void setup(ClientConnectionPoolDataSource source, Properties props) {
        if (props == null) {
            return;
        }
        if (props.containsKey(JDBC_DATABASE_NAME)) {
            source.setDatabaseName(props.getProperty(JDBC_DATABASE_NAME));
        }
        if (props.containsKey(JDBC_DATASOURCE_NAME)) {
            source.setDataSourceName(props.getProperty(JDBC_DATASOURCE_NAME));
        }
        if (props.containsKey(JDBC_DESCRIPTION)) {
            source.setDescription(props.getProperty(JDBC_DESCRIPTION));
        }
        if (props.containsKey(JDBC_NETWORK_PROTOCOL)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_PASSWORD)) {
            source.setPassword(props.getProperty(JDBC_PASSWORD));
        }
        if (props.containsKey(JDBC_PORT_NUMBER)) {
            source.setPortNumber(Integer.parseInt(props.getProperty(JDBC_PORT_NUMBER)));
        }
        if (props.containsKey(JDBC_ROLE_NAME)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_SERVER_NAME)) {
            source.setServerName(props.getProperty(JDBC_SERVER_NAME));
        }
        if (props.containsKey(JDBC_URL)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_USER)) {
            source.setUser(props.getProperty(JDBC_USER));
        }
    }

    /**
     * Setups the basic properties for embedded {@link DataSource}s
     */
    private void setup(ClientDataSource source, Properties props) {
        if (props == null) {
            return;
        }
        if (props.containsKey(JDBC_DATABASE_NAME)) {
            source.setDatabaseName(props.getProperty(JDBC_DATABASE_NAME));
        }
        if (props.containsKey(JDBC_DATASOURCE_NAME)) {
            source.setDataSourceName(props.getProperty(JDBC_DATASOURCE_NAME));
        }
        if (props.containsKey(JDBC_DESCRIPTION)) {
            source.setDescription(props.getProperty(JDBC_DESCRIPTION));
        }
        if (props.containsKey(JDBC_NETWORK_PROTOCOL)) {
            // Embedded DerbyDB
        }
        if (props.containsKey(JDBC_PASSWORD)) {
            source.setPassword(props.getProperty(JDBC_PASSWORD));
        }
        if (props.containsKey(JDBC_PORT_NUMBER)) {
            source.setPortNumber(Integer.parseInt(props.getProperty(JDBC_PORT_NUMBER)));
        }
        if (props.containsKey(JDBC_ROLE_NAME)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_SERVER_NAME)) {
            source.setServerName(props.getProperty(JDBC_SERVER_NAME));
        }
        if (props.containsKey(JDBC_URL)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_USER)) {
            source.setUser(props.getProperty(JDBC_USER));
        }
    }

    /**
     * Setup the basic and extended properties for {@link XADataSource}s and
     * {@link ConnectionPoolDataSource}s
     */
    private void setupXSource(ClientXADataSource source, Properties props) {
        if (props == null) {
            return;
        }
        setup(source, props);
        if (props.containsKey(JDBC_INITIAL_POOL_SIZE)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MAX_IDLE_TIME)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MAX_STATEMENTS)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MAX_POOL_SIZE)) {
            // FIXME ckuka
        }
        if (props.containsKey(JDBC_MIN_POOL_SIZE)) {
            // FIXME ckuka
        }
    }

}
