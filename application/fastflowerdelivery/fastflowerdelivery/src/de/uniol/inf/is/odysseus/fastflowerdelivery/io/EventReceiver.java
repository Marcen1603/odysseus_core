package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;

/**
 * Receives events sent from odysseus.
 * The events will be instantiated and further processed
 * by a certain method in ISinkEvent.
 * This class basically creates a physical operator used internally in odysseus,
 * the ReceiverPO.
 * Odysseus' sinks already need to be established before trying to open the
 * connection to odysseus using this class.
 *
 * @author Weert Stamm
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class EventReceiver extends Thread implements ITransferHandler {
	
	/**
	 * As soon as this thread is started, it will open a the connection
	 * to odysseus. The sinks in odysseus need to be running already
	 */
	@Override
	public void run() {
		openConnection();
	}
	
	/**
	 * The host address of odysseus
	 */
	private String host;
	
	/**
	 * The port to listen on 
	 */
	private int port;
	
	/**
	 * Used to translate and process any incoming data.
	 */
	private ISinkEventHandler sinkEventHandler;
	
	/**
	 * Creates the event receiver
	 * 
	 * @param hostAddress
	 * 			The host address of odysseus
	 * @param port
	 * 			The port to listen on 
	 * @param sinkEventHandler
	 * 			The handler used to translate and process the incoming data
	 */
	public EventReceiver(String hostAddress, int port, ISinkEventHandler sinkEventHandler) {
		this.host = hostAddress;
		this.port = port;
		this.sinkEventHandler = sinkEventHandler;
	}
	
	/**
	 * Starts the event receiver by opening a ReceiverPO
	 */
	@SuppressWarnings("unchecked")
	private void openConnection() {		
		Map<String, String> options = new HashMap<String, String>();
		options.put("host", host);
		options.put("port", "" + port);
		options.put("autoconnect", "false");
		
		IDataHandler dataHandler = DataHandlerRegistry.getDataHandler(
						"tuple", getSchema());
		
		ReceiverPO receiver = new ReceiverPO();
		IProtocolHandler ph = ProtocolHandlerRegistry.getInstance(
				"sizebytebuffer", ITransportDirection.IN, IAccessPattern.PUSH, options,
				dataHandler, (ITransferHandler) this);
		
		ITransportHandler transportHandler = TransportHandlerRegistry.getInstance(
				"nonblockingtcp", ph, options);
		
		ph.setTransportHandler(transportHandler);
		
		receiver.setProtocolHandler(ph);
		
		receiver.open(null, 0, 0, null, null);
	}
	
	/**
	 * Calls the processTuple method on the sink event handler,
	 * passing any tuple received
	 */
	@Override
	public void transfer(Object toTransfer) {
		if(toTransfer instanceof Tuple)
			sinkEventHandler.processTuple((Tuple)toTransfer);
	}
	
	/**
	 * Builds the SDFSchema from the data provided by the sink event handler
	 * @return the SDFSchema to use for this receiver
	 */
	private SDFSchema getSchema() {
		ArrayList<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		for(String dataType : sinkEventHandler.getSchema()) 
			attr.add(new SDFAttribute("", "", new SDFDatatype(dataType)));
		return new SDFSchema("", attr);
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {}
	
}
