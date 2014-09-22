package de.uniol.inf.is.odysseus.wrapper.haggis.communication;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.Identity;
import Ice.InitializationData;
import Ice.ObjectAdapter;
import Ice.ObjectPrx;
import Ice.Properties;
import Ice.Util;
import de.uniol.inf.is.odysseus.wrapper.haggis.physicaloperator.access.HaggisTransportHandler;

/**
 * ICE consumer object. Realized as runnable to avoid disconnecting problems and scheduling conflicts handling multiple incoming ICE streams.
 * 
 * @author jbmzh <jan.meyer.zu.holte@uni-oldenburg.de>
 *
 */
public class HaggisConsumer implements Runnable {
	private Thread t;
	
	private HaggisTransportHandler HTH;
	
	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(HaggisConsumer.class);
	
	private ObjectAdapter objectAdapter;
    private Communicator communicator;
    private HaggisTelemetrieSubscriber telemetrieSubscriber;
    
    private String proxy;
	
	public HaggisConsumer(HaggisTransportHandler hTH){
		setHTH(hTH);
		final Properties props = Util.createProperties();
        final InitializationData initializationData = new InitializationData();
        initializationData.properties = props;
        setCommunicator(Util.initialize(initializationData));
        setObjectAdapter(communicator.createObjectAdapterWithEndpoints(getHTH().getOwnService(), getHTH().getProtocol() + " -h " + getHTH().getListen() + " -p " + (1024 + (int) (Math.random() * 1000))));
        LOG.debug(String.format("Connecting to ICE endpoint %s", Arrays.toString(objectAdapter.getEndpoints())));
        setProxy(getHTH().getService() + ":" + getHTH().getProtocol() + " -h " + getHTH().getHost() + " -p " + getHTH().getPort());
	}
	
	@Override
	public void run() {
		objectAdapter.activate();
        final ObjectPrx base = communicator.stringToProxy(proxy);
        try {
            if (base != null) {
                base.ice_ping();
                LOG.debug(base.ice_id());
                setTelemetrieSubscriber(new HaggisTelemetrieSubscriber(this));
                ObjectPrx subscriber = objectAdapter.add(telemetrieSubscriber, new Identity(getHTH().getUsername(), getHTH().getPassword()));
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

	public void start(){
		if(t==null){
			t = new Thread(this);
			t.start();
		}
	}
	
	public void close(){
		try {
            objectAdapter.deactivate();
            objectAdapter.destroy();
            communicator.shutdown();
            communicator.destroy();
            setTelemetrieSubscriber(null);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
        	getHTH().setInput(null);
            getHTH().fireOnDisconnect();
        }
		t.interrupt();
		t = null;
	}
	

	public HaggisTransportHandler getHTH() {
		return HTH;
	}

	public void setHTH(HaggisTransportHandler hTH) {
		HTH = hTH;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public ObjectAdapter getObjectAdapter() {
		return objectAdapter;
	}

	public void setObjectAdapter(ObjectAdapter objectAdapter) {
		this.objectAdapter = objectAdapter;
	}

	public Communicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public HaggisTelemetrieSubscriber getTelemetrieSubscriber() {
		return telemetrieSubscriber;
	}

	public void setTelemetrieSubscriber(HaggisTelemetrieSubscriber telemetrieSubscriber) {
		this.telemetrieSubscriber = telemetrieSubscriber;
	}
}
