package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator.AbstractNoSQLJsonSinkPO;
import de.uniol.inf.is.odysseus.server.nosql.elasticsearch.logicaloperator.ElasticSearchSinkAO;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.util.List;

/**
 * Erstellt von RoBeaT
 * Date: 16.12.2014
 */
public class ElasticSearchSinkPO extends AbstractNoSQLJsonSinkPO {

    private TransportClient client;
    private String index; // like database
    private String type; // like table

    public ElasticSearchSinkPO(ElasticSearchSinkAO elasticSearchSinkAO) {
        super(elasticSearchSinkAO);

        index = elasticSearchSinkAO.getIndexName();
        type = elasticSearchSinkAO.getTypeName();
    }

    @Override
    protected void process_open_connection() throws OpenFailedException {

        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("134.106.56.17", 9300));
    }

    @Override
    protected void process_next_tuple_to_write(List<Tuple<ITimeInterval>> tupleToWrite) {

        for (Tuple<ITimeInterval> tuple : tupleToWrite) {

            String json =  toJsonString(tuple);

            client.prepareIndex(index, type)
                    .setSource(json)
                    .setOperationThreaded(false)
                    .execute();
        }
    }

    @Override
    protected void process_close() {
        client.close();
    }
}
