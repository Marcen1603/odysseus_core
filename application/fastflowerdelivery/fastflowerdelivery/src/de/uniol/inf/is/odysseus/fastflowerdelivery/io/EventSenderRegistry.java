package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import java.util.ArrayList;

/**
 * Global registry for all event senders.
 * Also starts the event senders.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class EventSenderRegistry {

	/**
	 * Singleton implementation.
	 */
	public static EventSenderRegistry getInstance() {
		if(instance == null)
			instance = new EventSenderRegistry();
		return instance;
	}
	private static EventSenderRegistry instance = null;
	private EventSenderRegistry() {}
	
	/**
	 * Keeps track of all registered event senders
	 */
	ArrayList<EventSender> registeredData = new ArrayList<EventSender>();
	
	/**
	 * Registers event senders at this registry
	 * @param eventSender
	 */
	public void register(EventSender eventSender) {
		registeredData.add(eventSender);
	}
	
	/**
	 * Retrieve (the first) instance of an event receiver determined by the 
	 * source event handler class. If no event receiver is found, null will be returned
	 * @param sourceEventHandlerClass
	 * 			the class of the source event handler to determine which instance to deliver
	 * @return the instance of the event receiver that receives events of the same class, 
	 * 			or null if no event receiver is found
	 */
	public EventSender get(Class<? extends ISourceEventHandler> sourceEventHandlerClass) {
		for(EventSender sender : registeredData)
			if(sender.getSourceEventHandler().getClass().getSimpleName().equals(sourceEventHandlerClass.getSimpleName()))
				return sender;
		return null;
	}
	
	/**
	 * Starts all the event receivers
	 */
	public void startAll() {
		for(EventSender sender : registeredData)
			sender.startServer();
	}
}
