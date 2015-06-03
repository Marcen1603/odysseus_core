package de.uniol.inf.is.odysseus.server.nosql.base.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSourceAO;
import de.uniol.inf.is.odysseus.server.nosql.base.util.connection.NoSQLConnectionManager;

/**
 *  The AbstractNoSQLSourcePO ist the superclass for all NoSQL sources. It helps the concrete implementation with the
 *  connection-handling and transfer of elements of the database as data stream with the transferThread.
 */
public abstract class AbstractNoSQLSourcePO<E extends IStreamObject<?>> extends AbstractSource<E> implements IPhysicalNoSQLOperator {

    private static NoSQLConnectionManager connectionManager = NoSQLConnectionManager.getInstance();

    private Thread transferThread;

    private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    
    private final int maxTupleCount;
    private final int delayBetweenTuple;
    private final int reloadDataInterval;
    private final boolean reloadData;

    public AbstractNoSQLSourcePO(AbstractNoSQLSourceAO abstractNoSQLSourceAO) {
        super();

        host = abstractNoSQLSourceAO.getHost();
        port = abstractNoSQLSourceAO.getPort();
        user = abstractNoSQLSourceAO.getUser();
        password = abstractNoSQLSourceAO.getPassword();
        database = abstractNoSQLSourceAO.getDatabase();

        maxTupleCount = abstractNoSQLSourceAO.getMaxTupleCount();
        delayBetweenTuple = abstractNoSQLSourceAO.getDelayBetweenTuple();
        reloadDataInterval = abstractNoSQLSourceAO.getReloadDataInterval();
        reloadData = abstractNoSQLSourceAO.isReloadData();
    }

    @Override
    protected void process_open() throws OpenFailedException {

        Object noSQLConnection = connectionManager.getConnection(host, port, user, password, database, getNoSQLConnectionWrapperClass());
        setupConnection(noSQLConnection);

        transferThread = new TransferThread();
        transferThread.start();
    }
    /**
     *  process_transfer_tuples will be implemented in the concrete NoSQLSourcePO.
     *  In this method will the concrete NoSQLSourcePO read data from the NoSQL database.
     *
     * @param maxElementCount the max count of elements. The concrete class should respect the max count
     * @return a list of Es
     */
    protected abstract List<E> process_transfer_tuples(int maxElementCount);

    @Override
    protected void process_close() {
        super.process_close();
        connectionManager.unregisterConnection(host, port);
        transferThread.interrupt();
        propagateDone();
    }

    class TransferThread extends Thread{

        @Override
        public void run() {

            do {

                List<E> tuples = process_transfer_tuples(maxTupleCount);

                for (E tuple : tuples) {
                    transfer(tuple);
                    waitForDelayBetweenTuple();
                }

                waitForReloadDataInterval();

            }while (reloadData);

            propagateDone();
        }

        private void waitForDelayBetweenTuple() {
            sleepDelay(delayBetweenTuple);
        }

        private void waitForReloadDataInterval() {
            sleepDelay(reloadDataInterval);
        }

        private void sleepDelay(long delay) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
