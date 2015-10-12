package de.uniol.inf.is.odysseus.query.codegenerator.message.bus;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.ProgressBarUpdate;

public class CodegeneratorMessageBus {
	
	private static List<ICodegeneratorMessageConsumer> consumerList = new ArrayList<ICodegeneratorMessageConsumer>();
	
	public static void sendUpdate(ProgressBarUpdate update){
		for(ICodegeneratorMessageConsumer consumer : consumerList){
			consumer.addMessage(update);
		}
	}
	                   
	public static void registerConsumer(ICodegeneratorMessageConsumer consumer){
		consumerList.add(consumer);
	}
	

	public static void unregisterConsumer(ICodegeneratorMessageConsumer consumer){
		consumerList.remove(consumer);
	}
	
}

