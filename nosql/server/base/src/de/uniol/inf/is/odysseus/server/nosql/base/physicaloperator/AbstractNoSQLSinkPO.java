package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.BatchSizeTimer;
import de.uniol.inf.is.odysseus.server.nosql.base.util.BatchSizeTimerTask;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionManager;

/**
 *  The AbstractNoSQLSinkPO ist the superclass for all NoSQL sinks. It helps the concrete implementation with the
 *  connection-handling and collecting elements to write them all into the NoSQL database instead of accessing the
 *  database for each element.
 */
public abstract class AbstractNoSQLSinkPO<E extends IStreamObject<?>> extends AbstractSink<E> implements IPhysicalNoSQLOperator {

	private static NoSQLConnectionManager connectionManager = NoSQLConnectionManager.getInstance();
    BatchSizeTimer<E> batchSizeTimer;

    private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    
    public AbstractNoSQLSinkPO(AbstractNoSQLSinkAO abstractNoSQLSinkAO){
        super();
        
        host = abstractNoSQLSinkAO.getHost();
        port = abstractNoSQLSinkAO.getPort();
        user = abstractNoSQLSinkAO.getUser();
        password = abstractNoSQLSinkAO.getPassword();
        database = abstractNoSQLSinkAO.getDatabase();

        int batchSize = abstractNoSQLSinkAO.getBatchSize();
        int batchTimeout = abstractNoSQLSinkAO.getBatchTimeout();

        BatchSizeTimerTask<E> batchSizeTimerTask = new BatchSizeTimerTaskImpl();
        batchSizeTimer = new BatchSizeTimer<>(batchSizeTimerTask, batchSize, batchTimeout);
    }

    @Override
    protected final void process_next(E element, int port) {
        batchSizeTimer.add(element);
    }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
       // nothing
    }
    
    @Override
    protected void process_open() throws OpenFailedException {
        Object noSQLConnection = connectionManager.getConnection(host, port, user, password, database, getNoSQLConnectionWrapperClass());
        setupConnection(noSQLConnection);
    }


    @Override
    protected void process_close() {
        batchSizeTimer.ring();
    }

    @Override
    protected void process_done(int port) {
    	batchSizeTimer.ring();
    }
    
    /**
     *  process_next_tuple_to_write will be implemented in the concrete NoSQLSinkPO.
     *  In this method all elements of elementsToWrite will be written into the NoSQL database.
     *
     * @param elementsToWrite list of elements which will be written into the database
     */
    protected abstract void process_next_tuple_to_write(List<E> elementsToWrite);

    private class BatchSizeTimerTaskImpl implements BatchSizeTimerTask<E> {
    	
        @Override
        public void onTimerRings(List<E> elements) {
            process_next_tuple_to_write(elements);
        }
    }
}
