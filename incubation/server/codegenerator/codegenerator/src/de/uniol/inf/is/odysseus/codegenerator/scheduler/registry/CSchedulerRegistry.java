package de.uniol.inf.is.odysseus.codegenerator.scheduler.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.scheduler.ICScheduler;


public class CSchedulerRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(CSchedulerRegistry.class);
	
	static Map<String, Map<String,ICScheduler>> schedulerList= new HashMap<String, Map<String,ICScheduler>>();
	

	public static ICScheduler getScheduler(String targetPlatform, String schedulerName){
	
	if(!schedulerList.isEmpty()){
		if(schedulerList.containsKey(targetPlatform.toLowerCase())){
			if(schedulerList.get(targetPlatform.toLowerCase()).containsKey(schedulerName.toLowerCase())){
				return schedulerList.get(targetPlatform.toLowerCase()).get(schedulerName.toLowerCase());
			}else{
				LOG.debug("No support for the "+ schedulerName.toLowerCase() +" executor");
			}
		}else{
			LOG.debug("Transformation operator registry found no programmlanguage:"+targetPlatform.toLowerCase());
		}
	}else{
		LOG.debug("Transformation operator registry is empty!");
	}
		
		return null;
	}
	
					   
	public static void registerScheduler(ICScheduler executor){
		LOG.debug("Operator" + executor.getName() +" für "+executor.getTargetPlatform() +" wird hinzugefügt");
		
		//Programmiersprache noch nicht vorhanden
		if(!schedulerList.containsKey(executor.getTargetPlatform().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(schedulerList.get(executor.getTargetPlatform().toLowerCase())== null || !schedulerList.get(executor.getTargetPlatform().toLowerCase()).containsKey(executor.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,ICScheduler> tempMap = new HashMap<String,ICScheduler>();
				tempMap.put(executor.getName().toLowerCase(), executor);
				
				//verschachtelte Map abspeichern
				schedulerList.put(executor.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+executor.getName()+" -> "+executor.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!schedulerList.get(executor.getTargetPlatform().toLowerCase()).containsKey(executor.getName())){
				Map<String,ICScheduler> tempMap = schedulerList.get(executor.getTargetPlatform().toLowerCase());
				tempMap.put(executor.getName().toLowerCase(), executor);
			}else{
				LOG.debug("Operator "+executor.getName()+" -> "+executor.getTargetPlatform().toLowerCase()+" already added" );
			}
	
		}
		
		
	}

	public static void unregisterScheduler(ICScheduler executor){
		if(schedulerList.containsKey(executor.getTargetPlatform().toLowerCase())){
			if(schedulerList.get(executor.getTargetPlatform().toLowerCase()).containsKey(executor.getName())){
				schedulerList.get(executor.getTargetPlatform().toLowerCase()).remove(executor.getName());
			}
			
			if(schedulerList.get(executor.getTargetPlatform().toLowerCase()).isEmpty()){
				schedulerList.remove(executor.getTargetPlatform().toLowerCase());
			}
		}
	}
	
	public static Set<String> getAllScheduler(String programmLanguage){
		Map<String, ICScheduler> schedulerMap = schedulerList.get(programmLanguage.toLowerCase());
		if(schedulerMap != null){
			return schedulerMap.keySet();
		}else{
			return null;
		}
		
	}
	
	

	
	public static Map<String, Map<String,ICScheduler>> getAllScheduler(){
		return schedulerList;
	}
	
	
	public static boolean existScheduler(String targetPlatform, String executor){
		
		if(schedulerList.containsKey(targetPlatform.toLowerCase()) && schedulerList.get(targetPlatform.toLowerCase()).containsKey(executor.toLowerCase())){
			return true;
		}else{
			return false;
		}
		
	}

}
