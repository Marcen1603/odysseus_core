package de.uniol.inf.is.odysseus.wrapper.snet.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.iis.kom.wsn.gateway.client.WSNGatewayClient;
import de.fraunhofer.iis.kom.wsn.gateway.client.WSNGatewayClientEvent;
import de.fraunhofer.iis.kom.wsn.gateway.client.WSNGatewayClientListener;
import de.fraunhofer.iis.kom.wsn.messages.WSNMessage;
import de.fraunhofer.iis.kom.wsn.messages.WSNMessageDefault;


public class SNetCommunicationHandler implements Runnable {

	protected final Logger LOG = LoggerFactory.getLogger(SNetCommunicationHandler.class);
	
	protected Thread t;
	
	private String appName = "";
	private String host = "127.0.0.1";
	private short port = 5001;

	private final WSNGatewayClient client = new WSNGatewayClient();
	
	private ArrayList<WSNMessageDefault> inputMessages = new ArrayList<WSNMessageDefault>();

	public SNetCommunicationHandler(String applicationName, String host, short port) {
		this.appName = applicationName;
		this.host = host;
		this.port = port;
	}
	
	public void connect(){
		try {
            // connect to the server
            client.connect(this.host, this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void initializeListener(){

        // receive events
        client.addWSNGatewayClientListener(new WSNGatewayClientListener() {

            @Override
            public void clientEvent(WSNGatewayClientEvent event) {

                if (event.getEvent() == WSNGatewayClientEvent.EVENT_DISCONNECT) {
                    System.out.println("disconnected");
                } else if (event.getEvent() == WSNGatewayClientEvent.EVENT_MESSAGE) {
                    final WSNMessage message = event.getWSNMessage();

                    if (message instanceof WSNMessageDefault) {
                        final WSNMessageDefault msg = (WSNMessageDefault) message;
                        inputMessages.add(msg);
//                        System.out.println(msg.toString());
                    }
                }
            }
        });
	}
	

	public void subscribeForServices(List<Integer> services) {
		if(services.size() > 0){
			for(Integer service: services){
				subscribeService(service);
			}
		} else {
			// subscribe for all services
	        subscribeService(0xFF);
		}
	}

	public void register(){
		// register this application to the server
        try {
			client.sendRegisterApplication(this.appName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deregister(){
		// deregister this application to the server
        try {
			client.sendDeregisterApplication();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void subscribeService(int service){
		//subscribe service
		try {
			client.sendSubscribeServiceType(service);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unscubscribeService(int service){
		// unsubscribe service
        try {
			client.sendUnsubscribeServiceType(service);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		if(t == null){
			t = new Thread(this);
			t.setName("SnetConnector");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start WSN-Communicator - but it was already running");
		}
	}
	
	public void close(){
		try {
			client.sendDeregisterApplication();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(t != null){
			t.interrupt();
		}
		t = null;
		LOG.debug(this.getClass().toString() + " closed.");
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void run() {
		
	}

	public ArrayList<WSNMessageDefault> getMessages() {
		return this.inputMessages;
	}
}
