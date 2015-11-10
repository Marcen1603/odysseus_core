package de.uniol.inf.is.odysseus.codegenerator.message.bus;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.codegenerator.modell.enums.UpdateMessageEventType;

/**
 * provided a central class for eventMessages in the codegenerator.feature
 * 
 * @author MarcPreuschaft
 *
 */
public class CodegeneratorMessageBus {
	
	//any warnings detected?
	public static boolean warningErrorDetected = false;
	
	//list of the consumerList for the eventMessages
	private static List<ICodegeneratorMessageConsumer> consumerList = new ArrayList<ICodegeneratorMessageConsumer>();
	
	/**
	 * send the received message to all consumers
	 * 
	 * @param update
	 */
	public static void sendUpdate(CodegeneratorMessageEvent update){
		synchronized (consumerList) {
			for(ICodegeneratorMessageConsumer consumer : consumerList){
				consumer.addMessageEvent(update);
			}
		}
		if(update.getStatusType() == UpdateMessageEventType.WARNING || update.getStatusType() == UpdateMessageEventType.ERROR  ){
			warningErrorDetected = true;
		}
	}
	  


	/**
	 * registers an new consumer (OSGI method)
	 * @param consumer
	 */
	public static void registerConsumer(ICodegeneratorMessageConsumer consumer){
		synchronized (consumerList) {
			consumerList.add(consumer);
		}
	}
	
	/**
	 *  unregisters a consumer (OSGI method)
	 *  
	 * @param consumer
	 */
	public static void unregisterConsumer(ICodegeneratorMessageConsumer consumer){
		synchronized (consumerList) {
			consumerList.remove(consumer);
		}
	}
	
	/**
	 * return the warningErrorDetected value
	 * @return
	 */
	public static boolean warningErrorDetected(){
		return warningErrorDetected;
	}
	
}

