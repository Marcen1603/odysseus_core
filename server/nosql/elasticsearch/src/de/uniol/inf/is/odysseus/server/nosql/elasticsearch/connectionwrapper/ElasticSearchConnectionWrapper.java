package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.connectionwrapper;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;

/**
 * Created by RoBeaT on 23.02.2015.
 */
@SuppressWarnings("all")
public class ElasticSearchConnectionWrapper extends NoSQLConnectionWrapper<TransportClient> {
	
    public ElasticSearchConnectionWrapper(String host, int port, String user, String password, String database) throws OpenFailedException {
        super(host, port, user, password, database);
    }

    @Override
    protected TransportClient establishConnection(String host, int port, String user, String password,String database) throws OpenFailedException {
        return new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }

    @Override
    protected void closeConnection(TransportClient connection) {
        connection.close();
    }
}
