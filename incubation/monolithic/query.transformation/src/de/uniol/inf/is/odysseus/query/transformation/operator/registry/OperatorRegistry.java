package de.uniol.inf.is.odysseus.query.transformation.operator.registry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;

public class OperatorRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(OperatorRegistry.class);
	
	static Map<String, Map<String,IOperator>> transformationOperator = new HashMap<String, Map<String,IOperator>>();
	

	public static IOperator getOperatorTransformation(String programmLanguage, String operatorName){
	
	if(!transformationOperator.isEmpty()){
		if(transformationOperator.containsKey(programmLanguage.toLowerCase())){
			if(transformationOperator.get(programmLanguage.toLowerCase()).containsKey(operatorName.toLowerCase())){
				return transformationOperator.get(programmLanguage.toLowerCase()).get(operatorName.toLowerCase());
			}else{
				LOG.debug("No support for the "+ operatorName.toLowerCase() +" operator");
			}
		}else{
			LOG.debug("Transformation operator registry found no programmlanguage:"+programmLanguage.toLowerCase());
		}
	}else{
		LOG.debug("Transformation operator registry is empty!");
	}
		
		return null;
	}
	
	
	public static void registerOperator(IOperator operator){
		LOG.debug("Operator" + operator.getName() +" für "+operator.getTargetPlatform() +" wird hinzugefügt");
		
		//Programmiersprache noch nicht vorhanden
		if(!transformationOperator.containsKey(operator.getTargetPlatform().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(transformationOperator.get(operator.getTargetPlatform().toLowerCase())== null || !transformationOperator.get(operator.getTargetPlatform().toLowerCase()).containsKey(operator.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,IOperator> tempMap = new HashMap<String,IOperator>();
				tempMap.put(operator.getName().toLowerCase(), operator);
				
				//verschachtelte Map abspeichern
				transformationOperator.put(operator.getTargetPlatform().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+operator.getName()+" -> "+operator.getTargetPlatform().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!transformationOperator.get(operator.getTargetPlatform().toLowerCase()).containsKey(operator.getName())){
				Map<String,IOperator> tempMap = transformationOperator.get(operator.getTargetPlatform().toLowerCase());
				tempMap.put(operator.getName().toLowerCase(), operator);
			}else{
				LOG.debug("Operator "+operator.getName()+" -> "+operator.getTargetPlatform().toLowerCase()+" already added" );
			}
	
		}
		
		
	}

	public static void unregisterOperator(IOperator operator){
		if(transformationOperator.containsKey(operator.getTargetPlatform().toLowerCase())){
			if(transformationOperator.get(operator.getTargetPlatform().toLowerCase()).containsKey(operator.getName())){
				transformationOperator.get(operator.getTargetPlatform().toLowerCase()).remove(operator.getName());
			}
			
			if(transformationOperator.get(operator.getTargetPlatform().toLowerCase()).isEmpty()){
				transformationOperator.remove(operator.getTargetPlatform().toLowerCase());
			}
		}
	}
	
}
