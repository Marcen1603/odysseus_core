package de.uniol.inf.is.odysseus.codegenerator.scheduler.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.codegenerator.scheduler.ICScheduler;

/**
 * Registry for scheduler for the codegenerator 
 * 
 * @author MarcPreuschaft
 *
 */
public class CSchedulerRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(CSchedulerRegistry.class);
	
	//list of avaliable scheduler
	static Map<String, Map<String,ICScheduler>> schedulerList= new HashMap<String, Map<String,ICScheduler>>();
	
	/**
	 * return a scheduler 
	 * 
	 * @param targetPlatform
	 * @param schedulerName
	 * @return
	 */
	public static ICScheduler getScheduler(String targetPlatform, String schedulerName){
	
	if(!schedulerList.isEmpty()){
		if(schedulerList.containsKey(targetPlatform.toLowerCase())){
			if(schedulerList.get(targetPlatform.toLowerCase()).containsKey(schedulerName.toLowerCase())){
				return schedulerList.get(targetPlatform.toLowerCase()).get(schedulerName.toLowerCase());
			}else{
				LOG.debug("No support for the "+ schedulerName.toLowerCase() +" scheduler");
			}
		}else{
			LOG.debug("Transformation operator registry found no programmlanguage:"+targetPlatform.toLowerCase());
		}
	}else{
		LOG.debug("Transformation operator registry is empty!");
	}
		
		return null;
	}
	
	/**
	 *  registers a new scheduler (OSGI method)
	 *  				   
	 * @param scheduler
	 */
	public static void registerScheduler(ICScheduler scheduler){
		LOG.debug("Operator" + scheduler.getName() +" für "+scheduler.getTargetPlatform() +" wird hinzugefügt");
		
		//Programmiersprache noch nicht vorhanden
		if(!schedulerList.containsKey(scheduler.getTargetPlatform().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(schedulerList.get(scheduler.getTargetPlatform().toLowerCase())== null || !schedulerList.get(scheduler.getTargetPlatform().toLowerCase()).containsKey(scheduler.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,ICScheduler> tempMap = new HashMap<String,ICScheduler>();
				tempMap.put(scheduler.getName().toLowerCase(), scheduler);
				
				//verschachtelte Map abspeichern
				schedulerList.put(scheduler.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+scheduler.getName()+" -> "+scheduler.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!schedulerList.get(scheduler.getTargetPlatform().toLowerCase()).containsKey(scheduler.getName())){
				Map<String,ICScheduler> tempMap = schedulerList.get(scheduler.getTargetPlatform().toLowerCase());
				tempMap.put(scheduler.getName().toLowerCase(), scheduler);
			}else{
				LOG.debug("Operator "+scheduler.getName()+" -> "+scheduler.getTargetPlatform().toLowerCase()+" already added" );
			}
	
		}
		
		
	}

	/**
	 * unregisters a scheduler (OSGI method)
	 * @param scheduler
	 */
	public static void unregisterScheduler(ICScheduler scheduler){
		if(schedulerList.containsKey(scheduler.getTargetPlatform().toLowerCase())){
			if(schedulerList.get(scheduler.getTargetPlatform().toLowerCase()).containsKey(scheduler.getName())){
				schedulerList.get(scheduler.getTargetPlatform().toLowerCase()).remove(scheduler.getName());
			}
			
			if(schedulerList.get(scheduler.getTargetPlatform().toLowerCase()).isEmpty()){
				schedulerList.remove(scheduler.getTargetPlatform().toLowerCase());
			}
		}
	}
	
	public static Set<String> getAllScheduler(String targetPlatform){
		Map<String, ICScheduler> schedulerMap = schedulerList.get(targetPlatform.toLowerCase());
		if(schedulerMap != null){
			return schedulerMap.keySet();
		}else{
			return null;
		}
		
	}
	
	

	
	public static Map<String, Map<String,ICScheduler>> getAllScheduler(){
		return schedulerList;
	}
	
	
	public static boolean existScheduler(String targetPlatform, String scheduler){
		
		if(schedulerList.containsKey(targetPlatform.toLowerCase()) && schedulerList.get(targetPlatform.toLowerCase()).containsKey(scheduler.toLowerCase())){
			return true;
		}else{
			return false;
		}
		
	}

}
