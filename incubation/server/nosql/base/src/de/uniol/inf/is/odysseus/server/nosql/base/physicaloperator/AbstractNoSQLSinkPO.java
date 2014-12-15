package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.BatchSizeTimer;
import de.uniol.inf.is.odysseus.server.nosql.base.util.BatchSizeTimerTask;

import java.util.List;

/**
 * Erstellt von RoBeaT
 * Date: 11.12.2014
 */
public abstract class AbstractNoSQLSinkPO extends AbstractSink<Tuple<ITimeInterval>> {

    private final String connectionName;
    BatchSizeTimer<Tuple<ITimeInterval>> batchSizeTimer;

    public AbstractNoSQLSinkPO(AbstractNoSQLSinkAO abstractNoSQLSinkAO){

        connectionName = abstractNoSQLSinkAO.getConnectionName();
        int batchSize = abstractNoSQLSinkAO.getBatchSize();
        int batchTimeout = abstractNoSQLSinkAO.getBatchTimeout();

        BatchSizeTimerTask<Tuple<ITimeInterval>> batchSizeTimerTask = new BatchSizeTimerTaskImpl();
        batchSizeTimer = new BatchSizeTimer<>(batchSizeTimerTask, batchSize, batchTimeout);
    }

    @Override
    protected void process_open() throws OpenFailedException {
        process_open_connection();
    }

    @Override
    protected final void process_next(Tuple<ITimeInterval> tuple, int port) {
        batchSizeTimer.add(tuple);
    }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
       // nothing
    }

    protected abstract void process_open_connection() throws OpenFailedException;

    protected abstract void process_next_tuple_to_write(List<Tuple<ITimeInterval>> tupleToWrite);



    private class BatchSizeTimerTaskImpl implements BatchSizeTimerTask<Tuple<ITimeInterval>> {

        @Override
        public void onTimerRings(List<Tuple<ITimeInterval>> elements) {
            process_next_tuple_to_write(elements);
        }
    }
}
