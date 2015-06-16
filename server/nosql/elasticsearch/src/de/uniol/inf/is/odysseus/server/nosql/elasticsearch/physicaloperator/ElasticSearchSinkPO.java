package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.physicaloperator;

import java.util.List;

import org.elasticsearch.client.transport.TransportClient;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionWrapper;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.connectionwrapper.ElasticSearchConnectionWrapper;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.logicaloperator.ElasticSearchSinkAO;

public class ElasticSearchSinkPO<T extends IStreamObject<?>> extends AbstractNoSQLJsonSinkPO<T> {

    private TransportClient client;
    private String index; // like database
    private String type; // like table

    public ElasticSearchSinkPO(ElasticSearchSinkAO elasticSearchSinkAO) {
        super(elasticSearchSinkAO);

        index = elasticSearchSinkAO.getIndexName();
        type = elasticSearchSinkAO.getTypeName();
    }

    @Override
    protected void process_next_json_to_write(List<String> jsonToWrite) {

        for (String json : jsonToWrite) {

            client.prepareIndex(index, type)
                    .setSource(json)
                    .setOperationThreaded(false)
                    .execute();

        }
    }

    @Override
    public void setupConnection(Object connection) {
        client = (TransportClient) connection;
    }

    @Override
    public Class<? extends NoSQLConnectionWrapper<?>> getNoSQLConnectionWrapperClass() {
        return ElasticSearchConnectionWrapper.class;
    }

}
