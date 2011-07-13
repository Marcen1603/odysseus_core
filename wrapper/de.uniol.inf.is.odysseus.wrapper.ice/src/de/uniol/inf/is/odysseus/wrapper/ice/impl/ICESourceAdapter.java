package de.uniol.inf.is.odysseus.wrapper.ice.impl;

import java.util.HashMap;
import java.util.Map;

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
import ReACT.ScooterPos;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public class ICESourceAdapter extends AbstractPushingSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(ICESourceAdapter.class);

    private final Map<Source, Thread> iceThreads = new HashMap<Source, Thread>();

    @Override
    public String getName() {
        return "ICE";
    }

    @Override
    protected void doDestroy(final Source source) {
        this.iceThreads.get(source).interrupt();
        this.iceThreads.remove(source);

    }

    @Override
    protected void doInit(final Source source) {
        final String service = source.getConfiguration().get("service").toString();
        final String host = source.getConfiguration().get("host").toString();
        final int port = Integer.parseInt(source.getConfiguration().get("port").toString());
        final String protocol = source.getConfiguration().get("protocol").toString();
        final String ownService = source.getConfiguration().get("ownService").toString();
        final int ownPort = Integer.parseInt(source.getConfiguration().get("ownPort").toString());
        try {
            // ICEConnection connection = new ICEConnection(source, "ClientConnector",
            // "192.168.1.99",
            // 10000, "", "", "tcp", "ServiceConnector", 20000, this);
            final ICEConnection connection = new ICEConnection(source, service, host, port,
                    protocol, "", "", ownService, ownPort, this);
            final Thread iceThread = new Thread(connection);
            this.iceThreads.put(source, iceThread);
            iceThread.start();
        }
        catch (final Exception e) {
            ICESourceAdapter.LOG.error(e.getMessage(), e);
        }
    }

    private class ICEConnection implements Runnable {
        private final Source source;
        private final ICESourceAdapter adapter;
        private final Communicator communicator;
        private final ObjectAdapter objectAdapter;
        private ClientCommunicatorPrx connectionObject;
        private final int maxMessageSize = 10240;
        private final int id = 0;
        private final String proxy;

        public ICEConnection(final Source source, final String service, final String host,
                final int port, final String protocol, final String username,
                final String password, final String ownService, final int ownPort,
                final ICESourceAdapter adapter) {
            this.source = source;
            this.adapter = adapter;
            final InitializationData initializationData = new InitializationData();
            final Properties props = Util.createProperties();
            props.setProperty("Ice.MessageSizeMax", Integer.toString(this.maxMessageSize));
            initializationData.properties = props;
            this.communicator = Util.initialize(initializationData);
            this.proxy = service + ":" + protocol + " -h " + host + " -p " + port;
            ICESourceAdapter.LOG.info("Try proxy: {}", this.proxy);
            this.objectAdapter = this.communicator.createObjectAdapterWithEndpoints(ownService,
                    protocol + " -h 127.0.0.1 -p " + ownPort);

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

                    // SimulationFacadePrx simulation = SimulationFacadePrxHelper
                    // .checkedCast(connectionObject.ice_twoway());
                    // SimulationControlerPrx controller = SimulationControlerPrxHelper
                    // .checkedCast(simulation.getObjectByUID(SIMULATION_CONTROLLER));
                    // if (controller == null) {
                    // controller = simulation.createNewSimulationControler();
                    // }
                    // ScenarioPrx scenario = controller.getActiveScenario();
                    // if ((scenario != null) && (scenario.isInitialized())) {
                    // SimAgentPrx[] agents = scenario.getAgents();
                    // System.out.println(agents);
                    // for (SimAgentPrx agent : agents) {
                    // System.out.println(agent.getDescription());
                    // }
                    // }
                    while (!Thread.currentThread().isInterrupted()) {
                        final ScooterPos position = this.connectionObject.getScooterPos(this.id);
                        final Double[] values = new Double[] {
                                position.x * 44.0, position.y * 44.0
                        };
                        this.adapter.transfer(this.source.getName(), System.currentTimeMillis(),
                                values);
                    }
                }
            }
            catch (final Exception e) {
                ICESourceAdapter.LOG.error(e.getMessage(), e);
            }
            finally {
                try {
                    if (this.objectAdapter != null) {
                        this.objectAdapter.deactivate();
                    }
                }
                catch (final Exception e) {
                    ICESourceAdapter.LOG.error(e.getMessage(), e);
                }
                try {
                    if (this.communicator != null) {
                        this.communicator.destroy();
                    }
                }
                catch (final Exception e) {
                    ICESourceAdapter.LOG.error(e.getMessage(), e);
                }
            }
        }
    }
}
