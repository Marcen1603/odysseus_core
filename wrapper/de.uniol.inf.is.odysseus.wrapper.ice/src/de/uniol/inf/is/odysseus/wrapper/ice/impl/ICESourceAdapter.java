package de.uniol.inf.is.odysseus.wrapper.ice.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scm.eci.simulation.ScenarioPrx;
import scm.eci.simulation.SimAgentPrx;
import scm.eci.simulation.SimulationControlerPrx;
import scm.eci.simulation.SimulationControlerPrxHelper;
import scm.eci.simulation.SimulationFacadePrx;
import scm.eci.simulation.SimulationFacadePrxHelper;
import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Properties;
import Ice.Util;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public class ICESourceAdapter extends AbstractPushingSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(ICESourceAdapter.class);

    private static final String SIMULATION_CONTROLLER = "scm.simulation.SimulationControler";
    private Map<Source, Thread> iceThreads = new HashMap<Source, Thread>();

    @Override
    public String getName() {
        return "ICE";
    }

    @Override
    protected void doDestroy(Source source) {
        this.iceThreads.get(source).interrupt();
        this.iceThreads.remove(source);

    }

    @Override
    protected void doInit(Source source) {
        System.out.println("Init call");
        try {
            ICEConnection connection = new ICEConnection("ClientConnector", "192.168.1.99", 10000,
                    "tcp", "ServiceConnector", 20000);
            System.out.println("Connection created");
            final Thread iceThread = new Thread(connection);
            System.out.println("Thread created");
            iceThreads.put(source, iceThread);
            iceThread.start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ICEConnection implements Runnable {
        private String service;
        private String host;
        private int port;
        private String protocol;
        private String ownService;
        private int ownPort;
        private Communicator communicator;
        private ObjectAdapter objectAdapter;
        private ObjectPrx connectionObject;
        private int maxMessageSize = 10240;

        public ICEConnection(String service, String host, int port, String protocol,
                String ownService, int ownPort) {
            this.service = service;
            this.host = host;
            this.port = port;
            this.protocol = protocol;
            this.ownService = ownService;
            this.ownPort = ownPort;
            InitializationData initializationData = new InitializationData();
            Properties props = Util.createProperties();
            props.setProperty("Ice.MessageSizeMax", Integer.toString(maxMessageSize));
            initializationData.properties = props;
            this.communicator = Util.initialize(initializationData);
        }

        @Override
        public void run() {
            String proxy = service + ":" + protocol + " -h " + host + " -p " + port;
            this.objectAdapter = communicator.createObjectAdapterWithEndpoints(ownService, protocol
                    + " -h 127.0.0.1 -p " + ownPort);
            LOG.info("Try proxy: {}", proxy);
            System.out.println("Try proxy: " + proxy);
            connectionObject = communicator.stringToProxy(proxy);
            System.out.println("Create proxy ");
            try {

                connectionObject.ice_ping();
                objectAdapter.activate();
                SimulationFacadePrx simulation = SimulationFacadePrxHelper
                        .checkedCast(connectionObject.ice_twoway());
                SimulationControlerPrx controller = SimulationControlerPrxHelper
                        .checkedCast(simulation.getObjectByUID(SIMULATION_CONTROLLER));
                if (controller == null) {
                    controller = simulation.createNewSimulationControler();
                }
                ScenarioPrx scenario = controller.getActiveScenario();
                if ((scenario != null) && (scenario.isInitialized())) {
                    SimAgentPrx[] agents = scenario.getAgents();
                    System.out.println(agents);
                    for (SimAgentPrx agent : agents) {
                        System.out.println(agent.getDescription());
                    }
                }
                while (!Thread.currentThread().isInterrupted()) {

                }

            }
            catch (Exception e) {
                e.printStackTrace();
                LOG.error(e.getMessage(), e);
            }
            finally {
                try {
                    if (objectAdapter != null) {
                        objectAdapter.deactivate();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    LOG.error(e.getMessage(), e);
                }
                try {
                    if (communicator != null) {
                        communicator.destroy();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }
}
