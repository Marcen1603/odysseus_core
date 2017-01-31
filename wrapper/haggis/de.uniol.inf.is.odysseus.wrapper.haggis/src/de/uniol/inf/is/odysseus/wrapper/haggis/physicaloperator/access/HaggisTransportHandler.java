package de.uniol.inf.is.odysseus.wrapper.haggis.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.haggis.communication.HaggisConsumer;

/**
 * Transport handler for the Haggis simulation platform. Realized using the ICE framework (See http://www.zeroc.com/ice.html).
 *
 * @author Christian Kuka <christian@kuka.cc> edited by jbmzh <jan.meyer.zu.holte@uni-oldenburg.de>
 *
 */
public class HaggisTransportHandler extends AbstractTransportHandler {
    /** Logger */
    private final Logger LOG = LoggerFactory.getLogger(HaggisTransportHandler.class);
    private final String OWN_SERVICE = "OdysseusSubscriberAdapter";

	private final String PROTOCOL = "default";
    private final String DEFAULT_SERVICE = "scm.eci.simulation.SimulationFacade";
    private final String DEFAULT_HOST = "127.0.0.1";
    private final int DEFAULT_PORT = 10001;
    private final String DEFAULT_AGENT = "LiftSupervisor";
    private final String DEFAULT_USERNAME = "Foo";
    private final String DEFAULT_PASSWORD = "Fara";
    private final String DEFAULT_LISTEN = "localhost";
    /** Input stream for data transfer */
    private InputStream input;

	private String listen;
    private String host;
    private int port;
    private String service;
    private String username;
    private String password;
    private String agent;

    private HaggisConsumer consumer;

    /**
     *
     */
    public HaggisTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public HaggisTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
        init(options);
    }

    @Override
    public void send(final byte[] message) throws IOException {

    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final HaggisTransportHandler handler = new HaggisTransportHandler(protocolHandler, options);
        return handler;
    }

    protected void init(OptionMap options) {
        if (options.get("listen") != null) {
            setListen(options.get("listen"));
        }
        else {
            setListen(DEFAULT_LISTEN);
        }
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
	public void processInOpen() throws IOException {
	}

    @Override
    public void processInStart() {
		if(consumer == null){
			consumer = new HaggisConsumer(this);
			consumer.start();
		}
    }

	@Override
	public void processInClose() throws IOException {
		consumer.close();
		consumer = null;
	}

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        throw new IllegalArgumentException("In-Only transport handler");
    }

    @Override
    public void processOutClose() throws IOException {
        this.fireOnDisconnect();
        throw new IllegalArgumentException("In-Only transport handler");
    }

    public void process(Object[] object) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOnly;
    }

    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof HaggisTransportHandler)) {
    		return false;
    	}
    	HaggisTransportHandler other = (HaggisTransportHandler)o;
    	if(!this.host.equals(other.host)) {
    		return false;
    	} else if(!this.getUsername().equals(other.getUsername())) {
    		return false;
    	} else if(!this.getPassword().equals(other.getPassword())) {
    		return false;
    	} else if(this.port != other.port) {
    		return false;
    	} else if(!this.getAgent().equals(other.getAgent())) {
    		return false;
    	} else if(!this.getService().equals(other.getService())) {
    		return false;
    	} else if(!this.getListen().equals(other.getListen())) {
    		return false;
    	}

    	return true;
    }

    /**
     * @param listen
     *            the listen address to set
     */
    public void setListen(String listen) {
        this.listen = listen;
    }

    /**
     * @return the listen address
     */
    public String getListen() {
        return this.listen;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @param agent
     *            the agent to set
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * @return the agent
     */
    public String getAgent() {
        return this.agent;
    }

    /**
     * @param service
     *            the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the service
     */
    public String getService() {
        return this.service;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    public InputStream getInput() {
		return input;
	}

	public void setInput(InputStream input) {
		this.input = input;
	}

	public Logger getLOG() {
		return LOG;
	}

	public String getOwnService() {
		return OWN_SERVICE;
	}

	public String getProtocol() {
		return PROTOCOL;
	}
}
