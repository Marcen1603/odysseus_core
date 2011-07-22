package de.uniol.inf.is.odysseus.wrapper.ice.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Properties;
import Ice.Util;
import ReACT.ClientCommunicatorPrx;
import ReACT.ClientCommunicatorPrxHelper;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractSinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SinkAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SinkSpec;

public class ICESinkAdapter extends AbstractSinkAdapter implements SinkAdapter {
    private static Logger LOG = LoggerFactory.getLogger(ICESinkAdapter.class);

    private final Map<SinkSpec, Thread> iceThreads = new HashMap<SinkSpec, Thread>();
    private final BlockingQueue<Float> messageQueue = new LinkedBlockingQueue<Float>();

    @Override
    public String getName() {
        return "ICE";
    }

    @Override
    public void transfer(final SinkSpec sink, final long timestamp, final Object[] data) {
        // Und bist du nicht willig so brauch ich Gewalt.
        this.messageQueue.offer((Float) data[0]);
    }

    @Override
    protected void destroy(final SinkSpec sink) {
        this.iceThreads.get(sink).interrupt();
        this.iceThreads.remove(sink);
    }

    @Override
    protected void init(final SinkSpec sink) {
        final String service = sink.getConfiguration().get("service").toString();
        final String host = sink.getConfiguration().get("host").toString();
        final int port = Integer.parseInt(sink.getConfiguration().get("port").toString());
        final String protocol = sink.getConfiguration().get("protocol").toString();
        final String ownService = sink.getConfiguration().get("ownService").toString();
        final int ownPort = Integer.parseInt(sink.getConfiguration().get("ownPort").toString());
        try {
            final ICEConnection connection = new ICEConnection(sink, service, host, port, protocol,
                    "", "", ownService, ownPort, this);
            final Thread iceThread = new Thread(connection);
            this.iceThreads.put(sink, iceThread);
            iceThread.start();
        }
        catch (final Exception e) {
            ICESinkAdapter.LOG.error(e.getMessage(), e);
        }

    }

    private class ICEConnection implements Runnable {
        private Communicator communicator;
        private ObjectAdapter objectAdapter;
        private ClientCommunicatorPrx connectionObject;
        private final int maxMessageSize = 10240;
        private final int id = 0;
        private final String proxy;

        public ICEConnection(final SinkSpec sink, final String service, final String host,
                final int port, final String protocol, final String username,
                final String password, final String ownService, final int ownPort,
                final ICESinkAdapter adapter) {
            this.proxy = service + ":" + protocol + " -h " + host + " -p " + port;
            try {
                ICESinkAdapter.LOG.info("Try proxy: {}", this.proxy);
                final Properties props = Util.createProperties();
                props.setProperty("Ice.MessageSizeMax", Integer.toString(this.maxMessageSize));
                final InitializationData initializationData = new InitializationData();

                initializationData.properties = props;
                this.communicator = Util.initialize(initializationData);

                this.objectAdapter = this.communicator.createObjectAdapterWithEndpoints(ownService,
                        protocol + " -h 127.0.0.1 -p " + ownPort);
            }
            catch (final Exception e) {
                ICESinkAdapter.LOG.info(e.getMessage(), e);
                ICESinkAdapter.LOG.error(e.getMessage(), e);
            }
        }

        @Override
        public void run() {

            try {
                this.objectAdapter.activate();
                final ObjectPrx base = this.communicator.stringToProxy(this.proxy);
                if (base != null) {
                    this.connectionObject = ClientCommunicatorPrxHelper.checkedCast(base);
                    System.out.println("Create proxy ");
                    this.connectionObject.ice_ping();
                    while (!Thread.currentThread().isInterrupted()) {
                        final Float speed = ICESinkAdapter.this.messageQueue.take();
                        this.connectionObject.setMaxSpeed(this.id, speed);
                    }
                }
            }
            catch (final Exception e) {
                ICESinkAdapter.LOG.error(e.getMessage(), e);
            }
            finally {
                try {
                    if (this.objectAdapter != null) {
                        this.objectAdapter.deactivate();
                    }
                }
                catch (final Exception e) {
                    ICESinkAdapter.LOG.error(e.getMessage(), e);
                }
                try {
                    if (this.communicator != null) {
                        this.communicator.destroy();
                    }
                }
                catch (final Exception e) {
                    ICESinkAdapter.LOG.error(e.getMessage(), e);
                }
            }
        }
    }
}
