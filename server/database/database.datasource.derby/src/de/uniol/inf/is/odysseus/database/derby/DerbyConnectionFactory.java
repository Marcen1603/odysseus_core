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

import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class DerbyConnectionFactory extends AbstractDatabaseConnectionFactory {

    @Override
    public IDatabaseConnection createConnection(String server, int port, String database, String user, String password) {
        Properties connectionProps = getCredentials(user, password);
        final String connString;
        if ((server == null) || ("".equals(server))) {
            // Embedded DB
            connString = "jdbc:derby:" + database + ";create=true";
        }
        else {
            // Network DB
            if (port > 0) {
                connString = "jdbc:derby://" + server + ":" + port + "/" + database + ";create=true";
            }
            else {
                connString = "jdbc:derby://" + server + "/" + database + ";create=true";
            }
        }
        return new DatabaseConnection(connString, connectionProps);
    }

    @Override
    public String getDatabase() {
        return "derby";
    }

}
