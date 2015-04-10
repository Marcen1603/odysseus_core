package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.connectionwrapper;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by RoBeaT on 23.02.2015.
 */
public class ElasticSearchConnectionWrapper extends NoSQLConnectionWrapper<TransportClient> {

    public ElasticSearchConnectionWrapper(String host, int port, String user, String password) throws OpenFailedException {
        super(host, port, user, password);
    }

    @Override
    protected TransportClient establishConnection(String host, int port, String user, String password) throws OpenFailedException {
        return new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }

    @Override
    protected void closeConnection(TransportClient connection) {
        connection.close();
    }
}