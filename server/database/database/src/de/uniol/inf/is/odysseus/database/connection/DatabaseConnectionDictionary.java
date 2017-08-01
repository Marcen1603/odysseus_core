/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.database.connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dennis Geesen
 *         Created at: 20.10.2011
 * @author Marco Grawunder, Christian Kuka
 */
public class DatabaseConnectionDictionary {
    private static Logger LOG = LoggerFactory.getLogger(DatabaseConnectionDictionary.class);

    private static Map<String, IDatabaseConnection> connections = new HashMap<>();
    private static HashMap<String, IDatabaseConnectionFactory> factories = new HashMap<>();
    private static List<IDatabaseConnectionDictionaryListener> listeners = new ArrayList<>();

    public static void addConnection(final String name, final IDatabaseConnection connection) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(connection);
        DatabaseConnectionDictionary.connections.put(name.toUpperCase(), connection);
        DatabaseConnectionDictionary.fireChangeEvent();
    }

    public static void addListener(final IDatabaseConnectionDictionaryListener listener) {
        Objects.requireNonNull(listener);
        DatabaseConnectionDictionary.listeners.add(listener);
    }

    public static Set<String> getConnectionFactoryNames() {
        return DatabaseConnectionDictionary.factories.keySet();
    }

    public static Map<String, IDatabaseConnection> getConnections() {
        return Collections.unmodifiableMap(DatabaseConnectionDictionary.connections);
    }

    public static IDatabaseConnection getDatabaseConnection(final String name) {
        if ((name == null) || ("".equals(name))) {
            return null;
        }
        return DatabaseConnectionDictionary.connections.get(name.toUpperCase());
    }

    public static IDatabaseConnectionFactory getFactory(final String dbms) {
        if ((dbms == null) || ("".equals(dbms))) {
            return null;
        }
        return DatabaseConnectionDictionary.factories.get(dbms.toUpperCase());
    }

    public static boolean hasConnection(final String name) {
        if ((name == null) || ("".equals(name))) {
            return false;
        }
        return DatabaseConnectionDictionary.connections.containsKey(name.toUpperCase());
    }

    public static boolean isConnectionExisting(final String name) {
        if ((name == null) || ("".equals(name))) {
            return false;
        }
        return DatabaseConnectionDictionary.connections.containsKey(name.toUpperCase());
    }

    public static void removeAllConnections() {
        synchronized (DatabaseConnectionDictionary.connections) {
            for (final Entry<String, IDatabaseConnection> c : DatabaseConnectionDictionary.connections.entrySet()) {
                if (c.getValue() != null) {
                    try {
                        if (c.getValue().getConnection() != null) {
                            c.getValue().getConnection().close();
                        }
                    }
                    catch (final Throwable e) {
                        DatabaseConnectionDictionary.LOG.error(e.getMessage(), e);
                    }
                }
            }
            DatabaseConnectionDictionary.connections.clear();
        }
        DatabaseConnectionDictionary.fireChangeEvent();
    }

    public static void removeConnection(final String name) {
        if ((name == null) || ("".equals(name))) {
            return;
        }
        synchronized (DatabaseConnectionDictionary.connections) {
            final IDatabaseConnection con = DatabaseConnectionDictionary.connections.remove(name.toUpperCase());
            if (con != null) {
                try {
                    if (con.getConnection() != null) {
                        con.getConnection().close();
                    }
                }
                catch (final Throwable e) {
                    DatabaseConnectionDictionary.LOG.error(e.getMessage(), e);
                }
            }
        }
        DatabaseConnectionDictionary.fireChangeEvent();
    }

    public static void removeListener(final IDatabaseConnectionDictionaryListener listener) {
        Objects.requireNonNull(listener);
        DatabaseConnectionDictionary.listeners.remove(listener);
    }

    private static void fireChangeEvent() {
        for (final IDatabaseConnectionDictionaryListener listener : DatabaseConnectionDictionary.listeners) {
            listener.databaseConnectionDictionaryChanged();
        }
    }

    public DatabaseConnectionDictionary() {
    }

    public void add(final IDatabaseConnectionFactory factory) {
        Objects.requireNonNull(factory);
        DatabaseConnectionDictionary.factories.put(factory.getDatabase().toUpperCase(), factory);
        DatabaseConnectionDictionary.fireChangeEvent();
    }

    public void removeFactory(final IDatabaseConnectionFactory factory) {
        Objects.requireNonNull(factory);
        DatabaseConnectionDictionary.factories.remove(factory.getDatabase().toUpperCase());
        DatabaseConnectionDictionary.fireChangeEvent();
    }

}
