package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IntegerHandler;
import de.uniol.inf.is.odysseus.core.datahandler.LongHandler;
import de.uniol.inf.is.odysseus.core.datahandler.StringHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;

/**
 * Global registry for all event receivers.
 * Also starts them all as soon as the first login occurred.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class EventReceiverRegistry extends Thread {

	/**
	 * Singleton implementation.
	 */
	public static EventReceiverRegistry getInstance() {
		if(instance == null)
			instance = new EventReceiverRegistry();
		return instance;
	}
	private static EventReceiverRegistry instance = null;
	private EventReceiverRegistry() {}
	

	/**
	 * Keeps track of all event receivers
	 */
	ArrayList<EventReceiver> registeredData = new ArrayList<EventReceiver>();
	
	/**
	 * Registers an event receiver at this registry
	 * @param eventReceiver
	 * 			the event receiver to register
	 */
	public void register(EventReceiver eventReceiver) {
		synchronized(registeredData) {
			registeredData.add(eventReceiver);
		}
	}
	
	/**
	 * Registers data handlers globally to be used in any event receiver.
	 * Data handlers are necessary for the ReceiverPO to run.
	 */
	private void registerDataHandlers() {
		DataHandlerRegistry.registerDataHandler(new LongHandler());
		DataHandlerRegistry.registerDataHandler(new IntegerHandler());
		DataHandlerRegistry.registerDataHandler(new StringHandler());
		DataHandlerRegistry.registerDataHandler(new TupleDataHandler());
	}
	
	/**
	 * Registers the transport handler to be used in any event receiver.
	 * Transport handlers are necessary for the ReceiverPO to run.
	 */
	private void registerTransportHandler() {
		TransportHandlerRegistry.register(new NonBlockingTcpHandler());
	}
	
	/**
	 * Registers the protocol handler to be used in any event receiver.
	 * Protocol handlers are necessary for the ReceiverPO to run.
	 */
	@SuppressWarnings("rawtypes")
	private void registerProtocolHandler() {
		ProtocolHandlerRegistry.register(new SizeByteBufferHandler());
	}
	
	/**
	 * Registers all necessary handlers and starts all the receivers
	 * time delayed, to provide enough time for odysseus' sinks to start
	 */
	public void startAll() {
		if(!isStarted) {
			registerDataHandlers();
			registerProtocolHandler();
			registerTransportHandler();
			start();
			isStarted = true;
		}
	}
	
	private boolean isStarted = false;

	/**
	 * Starts all the event receivers time delayed
	 */
	@Override
	public void run() {
		synchronized(registeredData) {
			try {
				for(EventReceiver sender : registeredData)
					sender.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.out.println("Receiver started!");
		}
	}
	
}
