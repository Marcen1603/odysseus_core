package de.uniol.inf.is.odysseus.server.mongodb.connectionwrapper;

import com.mongodb.MongoClient;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;

import java.net.UnknownHostException;

public class MongoDBConnectionWrapper extends NoSQLConnectionWrapper<MongoClient> {

    public MongoDBConnectionWrapper(String host, int port, String user, String password) throws OpenFailedException {
        super(host, port, user, password);
    }

    @Override
    protected MongoClient establishConnection(String host, int port, String user, String password) throws OpenFailedException {

        try {
            return new MongoClient(host, port);
        } catch (UnknownHostException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    protected void closeConnection(MongoClient connection) {
        connection.close();
    }
}
