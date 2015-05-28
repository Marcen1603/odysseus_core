package de.uniol.inf.is.odysseus.server.nosql.base.util.connection;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

/**
 *  The NoSQLConnectionWrapper holds a specified connection and counts the usages.
 *  If their are more usages, the connection will be closed
 *
 * @param <E> The class of the NoSQL connection
 */
public abstract class NoSQLConnectionWrapper<E> {

    protected E connection;
    protected int userCount = 0;

    public NoSQLConnectionWrapper(String host, int port, String user, String password, String database) throws OpenFailedException{
        this.connection = establishConnection(host, port, user, password, database);
    }

    /**
     *  The implementation will establish the connection to the database in this method.
     *
     * @return the connection to the database
     * @throws OpenFailedException
     */
    protected abstract E establishConnection(String host, int port, String user, String password, String database) throws OpenFailedException;

    public E getConnection() {
        userCount++;
        return connection;
    }

    public int unregisterUser() {

        if(--userCount <= 0){
            closeConnection(connection);
        }
        return userCount;
    }

    /**
     *  The implementation will close the connection to the database in this method.
     *
     * @param connection to the database
     */
    protected abstract void closeConnection(E connection);
}
