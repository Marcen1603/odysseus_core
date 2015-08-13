package de.uniol.inf.is.odysseus.query.transformation.executor.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.transformation.executor.IExecutor;


public class ExecutorRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(ExecutorRegistry.class);
	
	static Map<String, Map<String,IExecutor>> executorList= new HashMap<String, Map<String,IExecutor>>();
	

	public static IExecutor getExecutor(String programmLanguage, String executorName){
	
	if(!executorList.isEmpty()){
		if(executorList.containsKey(programmLanguage.toLowerCase())){
			if(executorList.get(programmLanguage.toLowerCase()).containsKey(executorName.toLowerCase())){
				return executorList.get(programmLanguage.toLowerCase()).get(executorName.toLowerCase());
			}else{
				LOG.debug("No support for the "+ executorName.toLowerCase() +" executor");
			}
		}else{
			LOG.debug("Transformation operator registry found no programmlanguage:"+programmLanguage.toLowerCase());
		}
	}else{
		LOG.debug("Transformation operator registry is empty!");
	}
		
		return null;
	}
	
					   
	public static void registerExecutor(IExecutor executor){
		LOG.debug("Operator" + executor.getName() +" für "+executor.getTargetPlatform() +" wird hinzugefügt");
		
		//Programmiersprache noch nicht vorhanden
		if(!executorList.containsKey(executor.getTargetPlatform().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(executorList.get(executor.getTargetPlatform().toLowerCase())== null || !executorList.get(executor.getTargetPlatform().toLowerCase()).containsKey(executor.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,IExecutor> tempMap = new HashMap<String,IExecutor>();
				tempMap.put(executor.getName().toLowerCase(), executor);
				
				//verschachtelte Map abspeichern
				executorList.put(executor.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+executor.getName()+" -> "+executor.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!executorList.get(executor.getTargetPlatform().toLowerCase()).containsKey(executor.getName())){
				Map<String,IExecutor> tempMap = executorList.get(executor.getTargetPlatform().toLowerCase());
				tempMap.put(executor.getName().toLowerCase(), executor);
			}else{
				LOG.debug("Operator "+executor.getName()+" -> "+executor.getTargetPlatform().toLowerCase()+" already added" );
			}
	
		}
		
		
	}

	public static void unregisterExecutor(IExecutor executor){
		if(executorList.containsKey(executor.getTargetPlatform().toLowerCase())){
			if(executorList.get(executor.getTargetPlatform().toLowerCase()).containsKey(executor.getName())){
				executorList.get(executor.getTargetPlatform().toLowerCase()).remove(executor.getName());
			}
			
			if(executorList.get(executor.getTargetPlatform().toLowerCase()).isEmpty()){
				executorList.remove(executor.getTargetPlatform().toLowerCase());
			}
		}
	}
	
	public static Set<String> getAllExecutor(String programmLanguage){
		return executorList.get(programmLanguage.toLowerCase()).keySet();
}

}
