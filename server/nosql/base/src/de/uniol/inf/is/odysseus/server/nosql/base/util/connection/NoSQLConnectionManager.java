package de.uniol.inf.is.odysseus.server.nosql.base.util.connection;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

import java.lang.reflect.Constructor;

/**
 *  The NoSQLConnectionManager helps using the connections to the databases efficient by sharing connections und closing
 *  them when they are not needed.
 */
@SuppressWarnings("rawtypes")
public class NoSQLConnectionManager {

    private static final NoSQLConnectionManager INSTANCE = new NoSQLConnectionManager();

    private NoSQLConnectionManager() {
        // nothing
    }

    public static NoSQLConnectionManager getInstance(){
        return INSTANCE;
    }

    /**
     * Static Table of all connections is needed to prevent the creation of unnecessary connections
     * All physical operator instances of the same NoSQL DB (eg. MongoDB) can use the same connection.
     */
    private Table<String, Integer, NoSQLConnectionWrapper> connectionMap = HashBasedTable.create();

    /**
     *  getConnection returns the connection to the database. If the connection is available, the connection will be shared.
     *  Otherwise a new connection will be established
     *
     * @param noSQLConnectionWrapperClass the class of the concrete NoSQLConnectionWrapper
     * @return the connection to the database
     * @throws OpenFailedException
     */
    public Object getConnection(String host,
                                int port,
                                String user,
                                String password,
                                String database,
                                Class<? extends NoSQLConnectionWrapper> noSQLConnectionWrapperClass)
            throws OpenFailedException{

        NoSQLConnectionWrapper noSQLConnectionWrapper = connectionMap.get(host, port);

        if(noSQLConnectionWrapper == null){
            noSQLConnectionWrapper = createNoSQLConnectionWrapper(host, port, user, password, database, noSQLConnectionWrapperClass);
            connectionMap.put(host, port, noSQLConnectionWrapper);
        }
        return noSQLConnectionWrapper.getConnection();
    }

    /**
     * This method will be usually called just one time for all physical operators of one NoSQL DB to establish the connection
     */
    private NoSQLConnectionWrapper createNoSQLConnectionWrapper(String host,
                                                                int port,
                                                                String user,
                                                                String password,
                                                                String database,
                                                                Class<? extends NoSQLConnectionWrapper> noSQLConnectionWrapperClass)
            throws OpenFailedException {

        try {
            Constructor<? extends NoSQLConnectionWrapper> declaredConstructor =
                    noSQLConnectionWrapperClass.getDeclaredConstructor(String.class, int.class, String.class, String.class, String.class);
//                                                                     Host        , Port     , User        , Password.     Database

            return declaredConstructor.newInstance(host, port, user, password, database);

        }catch (Exception e){
            throw new OpenFailedException(e);
        }
    }

    /**
     *  unregisterConnection will be called, if a connection is no longer needed. If the connection is not longer used
     *  by any physical operator, the connection will be closed.
     */
    public void unregisterConnection(String host, int port){
        NoSQLConnectionWrapper noSQLConnectionWrapper = connectionMap.get(host, port);
        int registeredUserCount = noSQLConnectionWrapper.unregisterUser();
        if(registeredUserCount == 0){
            connectionMap.remove(host, port);
        }
    }
}
