package de.uniol.inf.is.odysseus.wrapper.haggis.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.Current;
import Ice.Identity;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Properties;
import Ice.Util;
import TelemetriePublishSubscribe.Pose;
import TelemetriePublishSubscribe.TelemetriePublisherPrx;
import TelemetriePublishSubscribe.TelemetriePublisherPrxHelper;
import TelemetriePublishSubscribe.TelemetrieSubscriberPrx;
import TelemetriePublishSubscribe.TelemetrieSubscriberPrxHelper;
import TelemetriePublishSubscribe._TelemetrieSubscriberDisp;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Transport handler for the Haggis simulation platform.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class HaggisTransportHandler extends AbstractTransportHandler {
    /** Logger */
    private final Logger        LOG              = LoggerFactory.getLogger(HaggisTransportHandler.class);
    // private static final Charset charset = Charset.forName("UTF-8");
    // private static final CharsetEncoder encoder = charset.newEncoder();
    private static final String OWN_SERVICE      = "OdysseusSubscriberAdapter";
    private static final String PROTOCOL         = "default";
    private static final String DEFAULT_SERVICE  = "scm.eci.simulation.SimulationFacade";
    private static final String DEFAULT_HOST     = "127.0.0.1";
    private static final int    DEFAULT_PORT     = 10001;
    private static final String DEFAULT_AGENT    = "LiftSupervisor";
    private static final String DEFAULT_USERNAME = "";
    private static final String DEFAULT_PASSWORD = "";
    /** Input stream for data transfer */
    private InputStream         input;
    private String              host;
    private int                 port;
    private String              service;
    private String              username;
    private String              password;
    private String              agent;
    private ObjectAdapter       objectAdapter;
    private Communicator        communicator;

    /**
     * 
     */
    public HaggisTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public HaggisTransportHandler(final IProtocolHandler<?> protocolHandler) {
        super(protocolHandler);
    }

    @Override
    public void send(final byte[] message) throws IOException {

    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        final HaggisTransportHandler handler = new HaggisTransportHandler(protocolHandler);
        handler.init(options);
        return handler;
    }

    protected void init(Map<String, String> options) {
        if (options.get("host") != null) {
            setHost(options.get("host"));
        }
        else {
            setHost(DEFAULT_HOST);
        }
        if (options.get("port") != null) {
            setPort(Integer.parseInt(options.get("port").toString()));
        }
        else {
            setPort(DEFAULT_PORT);
        }
        if (options.get("service") != null) {
            setService(options.get("service"));
        }
        else {
            setService(DEFAULT_SERVICE);
        }
        if (options.get("agent") != null) {
            setAgent(options.get("agent"));
        }
        else {
            setAgent(DEFAULT_AGENT);
        }

        if (options.get("username") != null) {
            setUsername(options.get("username"));
        }
        else {
            setUsername(DEFAULT_USERNAME);
        }
        if (options.get("password") != null) {
            setPassword(options.get("password"));
        }
        else {
            setPassword(DEFAULT_PASSWORD);
        }
    }

    @Override
    public InputStream getInputStream() {
        return this.input;
    }

    @Override
    public String getName() {
        return "Haggis";
    }

    @Override
    public OutputStream getOutputStream() {
        throw new IllegalArgumentException("In-Only transport handler");
    }

    @Override
    public void processInOpen() throws UnknownHostException, IOException {
        final Properties props = Util.createProperties();
        final InitializationData initializationData = new InitializationData();
        initializationData.properties = props;
        communicator = Util.initialize(initializationData);
        objectAdapter = communicator.createObjectAdapterWithEndpoints(OWN_SERVICE, PROTOCOL + " -h 127.0.0.1 -p " + (1024 + (int) (Math.random() * 1000)));
        LOG.debug(String.format("Connecting to ICE endpoint %s", Arrays.toString(objectAdapter.getEndpoints())));
        String proxy = getService() + ":" + PROTOCOL + " -h " + getHost() + " -p " + getPort();

        objectAdapter.activate();
        final ObjectPrx base = communicator.stringToProxy(proxy);
        try {
            if (base != null) {
                base.ice_ping();
                LOG.debug(base.ice_id());
                TelemetrieSubscriber telemetrieSubscriber = new TelemetrieSubscriber(this);
                ObjectPrx subscriber = objectAdapter.add(telemetrieSubscriber, new Identity("Foo", "Fara"));
                telemetrieSubscriber.open(base, subscriber);
            }
            else {
                throw new IllegalArgumentException(String.format("Invalid proxy %s", proxy));
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        throw new IllegalArgumentException("In-Only transport handler");
    }

    @Override
    public void processInClose() throws IOException {
        try {
            objectAdapter.deactivate();
            objectAdapter.destroy();
            communicator.shutdown();
            communicator.destroy();
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
        finally {
            this.input = null;
            this.fireOnDisconnect();
        }
    }

    @Override
    public void processOutClose() throws IOException {
        this.fireOnDisconnect();
        throw new IllegalArgumentException("In-Only transport handler");
    }

    void process(Object[] object) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Object obj : object) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(obj);
            }
            fireProcess(ByteBuffer.wrap(sb.toString().getBytes()));
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    private void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    private void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgent() {
        return agent;
    }

    private void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }

    class TelemetrieSubscriber extends _TelemetrieSubscriberDisp {
        private final HaggisTransportHandler handler;
        private TelemetriePublisherPrx       publisher;
        /**
		 * 
		 */
        private static final long            serialVersionUID = -5341086082699034964L;

        /**
         * @param handler
         */
        public TelemetrieSubscriber(HaggisTransportHandler handler) {
            this.handler = handler;
        }

        @Override
        public void _notify(Pose p, Current __current) {
            // System.out.println(p.X + "; " + p.Y + "; "+ p.Z + "; " +
            // p.orientation);
            try {
                handler.process(new Object[] { p.timestamp, p.X, p.Y, p.Z, p.orientation });
            }
            catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw e;
            }
        }

        /**
         * @param base
         * @param subscriber
         * 
         */
        public void open(ObjectPrx base, ObjectPrx subscriber) {
            try {
                publisher = TelemetriePublisherPrxHelper.checkedCast(base);
                TelemetrieSubscriberPrx subscriberPrx = TelemetrieSubscriberPrxHelper.checkedCast(subscriber);
                publisher.subscribe(subscriberPrx);
            }
            catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw e;
            }
        }

        public void close() {
            if (this.publisher != null) {
                this.publisher = null;
            }
        }
    }
}
