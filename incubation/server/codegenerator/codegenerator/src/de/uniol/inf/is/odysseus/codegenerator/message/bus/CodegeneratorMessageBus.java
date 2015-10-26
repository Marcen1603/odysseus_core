package de.uniol.inf.is.odysseus.codegenerator.message.bus;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.codegenerator.modell.enums.UpdateMessageEventType;

public class CodegeneratorMessageBus {
	
	public static boolean warningErrorDetected = false;
	
	private static List<ICodegeneratorMessageConsumer> consumerList = new ArrayList<ICodegeneratorMessageConsumer>();
	
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
	                   
	public static void registerConsumer(ICodegeneratorMessageConsumer consumer){
		synchronized (consumerList) {
			consumerList.add(consumer);
		}
	}
	

	public static void unregisterConsumer(ICodegeneratorMessageConsumer consumer){
		synchronized (consumerList) {
			consumerList.remove(consumer);
		}
	}
	
	public static boolean warningErrorDetected(){
		return warningErrorDetected;
	}
	
}

