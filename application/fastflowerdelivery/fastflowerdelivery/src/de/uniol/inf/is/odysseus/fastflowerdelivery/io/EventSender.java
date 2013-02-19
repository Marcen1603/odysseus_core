package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * Sends events as data tuples to Odysseus.
 * Internally the odysseus event generator is used
 * to send events to odysseus.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class EventSender extends StreamClientHandler {

	/**
	 * A Queue to hold events to be send to odysseus
	 */
	private List<DataTuple> eventQueue = new ArrayList<DataTuple>();
	
	/**
	 * The port to open the server on
	 */
	private int port;
	
	/**
	 * Handles event to tuple conversion
	 */
	private ISourceEventHandler sourceEventHandler;
	
	/**
	 * Create an event sender
	 * @param port
	 * 			the port to start the server on
	 * @param sourceEventHandler
	 * 			the source event handler to handle event to tuple conversion
	 */
	public EventSender(int port, ISourceEventHandler sourceEventHandler) {
		this.port = port;
		this.sourceEventHandler = sourceEventHandler;
	}
	
	/**
	 * Private constructor for clone method
	 * @param port
	 * 			the port to start the server on
	 * @param sourceEventHandler
	 * 			the source event handler to handle event to tuple conversion
	 * @param eventQueue
	 * 			the current event queue
	 */
	private EventSender(int port, ISourceEventHandler sourceEventHandler, List<DataTuple> eventQueue) {
		this.port = port;
		this.sourceEventHandler = sourceEventHandler;
		this.eventQueue = eventQueue;
	}
	
	/**
	 * Starts the server on the specified port
	 */
	public void startServer() {
		try {
			StreamServer server = new StreamServer(port, this);
			server.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Accepts events to send to odysseus.
	 * Once an event has been accepted, the sender
	 * thread will wake up and send the event.
	 * If the server did not start or nobody did connect, yet,
	 * the accepted events will be held in qeue and be sent once
	 * the server is started, someone has connected and another event is accepted.
	 * @param event
	 * 			the event to send to odysseus
	 */
	public void accept(ISourceEventHandler event) {
		synchronized(sourceEventHandler) {
			eventQueue.add(event.getTuple());
			sourceEventHandler.notifyAll();
		}
	}
	
	@Override
	public void init() {}

	@Override
	public void close() {}

	/**
	 * Sends the accepted events.
	 * Will be called by the event generator.
	 * This method will wait for events to be accepted to send.
	 */
	@Override
	public List<DataTuple> next() throws InterruptedException {
		List<DataTuple> result = new ArrayList<DataTuple>();
		synchronized(sourceEventHandler) {
			sourceEventHandler.wait();
			if(eventQueue.size() > 0) {
				for(DataTuple dt : eventQueue)
					result.add(dt);
				eventQueue.clear();
			}
		}
		return result;
	}

	/**
	 * Required by the event generator
	 */
	@Override
	public StreamClientHandler clone() {
		return new EventSender(port, sourceEventHandler, eventQueue);
	}

	/**
	 * Retrieves the instance of the source event handler
	 * @return the source event handler
	 */
	public ISourceEventHandler getSourceEventHandler() {
		return sourceEventHandler;
	}
}
