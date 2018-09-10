package de.uniol.inf.is.odysseus.server.mongodb.connectionwrapper;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;

public class MongoDBConnectionWrapper extends NoSQLConnectionWrapper<MongoClient> {

    public MongoDBConnectionWrapper(String host, int port, String user, String password, String database) throws OpenFailedException {
        super(host, port, user, password, database);
    }

    @Override
    protected MongoClient establishConnection(String host, int port, String user, String password, String database) throws OpenFailedException {

        try {
        	if (user != null){
            	MongoCredential credential = MongoCredential.createPlainCredential(user, database, password.toCharArray());
            	return new MongoClient(new ServerAddress(), Arrays.asList(credential));
        	}else{
        		return new MongoClient(host, port);
        	}
        } catch (UnknownHostException e) {
            throw new OpenFailedException(e);
        }
    }

    @Override
    protected void closeConnection(MongoClient connection) {
        connection.close();
    }
}
