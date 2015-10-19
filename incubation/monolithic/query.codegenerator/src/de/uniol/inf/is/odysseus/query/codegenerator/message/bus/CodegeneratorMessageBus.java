package de.uniol.inf.is.odysseus.query.codegenerator.message.bus;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodegeneratorMessageEvent;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.enums.UpdateMessageStatusType;

public class CodegeneratorMessageBus {
	
	public static boolean warningErrorDetected = false;
	
	private static List<ICodegeneratorMessageConsumer> consumerList = new ArrayList<ICodegeneratorMessageConsumer>();
	
	public static void sendUpdate(CodegeneratorMessageEvent update){
		synchronized (consumerList) {
			for(ICodegeneratorMessageConsumer consumer : consumerList){
				consumer.addMessageEvent(update);
			}
		}
		if(update.getStatusType() == UpdateMessageStatusType.WARNING || update.getStatusType() == UpdateMessageStatusType.ERROR  ){
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

