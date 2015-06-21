package de.uniol.inf.is.odysseus.query.transformation.operator.registry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.transformation.operator.java.ITransformationOperator;

public class QueryTransformationOperatorRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformationOperatorRegistry.class);
	
	static Map<String, Map<String,ITransformationOperator>> transformationOperator = new HashMap<String, Map<String,ITransformationOperator>>();
	

	public static ITransformationOperator getOperatorTransformation(String programmLanguage, String operatorName){
	
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
	
	
	public static void registerOperator(ITransformationOperator operator){
		LOG.debug("Operator" + operator.getName() +" f�r "+operator.getProgramLanguage() +" wird hinzugef�gt");
		
		//Programmiersprache noch nicht vorhanden
		if(!transformationOperator.containsKey(operator.getProgramLanguage().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(transformationOperator.get(operator.getProgramLanguage().toLowerCase())== null || !transformationOperator.get(operator.getProgramLanguage().toLowerCase()).containsKey(operator.getName().toLowerCase())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,ITransformationOperator> tempMap = new HashMap<String,ITransformationOperator>();
				tempMap.put(operator.getName().toLowerCase(), operator);
				
				//verschachtelte Map abspeichern
				transformationOperator.put(operator.getProgramLanguage().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+operator.getName()+" -> "+operator.getProgramLanguage().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzuf�gen			
			if(!transformationOperator.get(operator.getProgramLanguage().toLowerCase()).containsKey(operator.getName())){
				Map<String,ITransformationOperator> tempMap = transformationOperator.get(operator.getProgramLanguage().toLowerCase());
				tempMap.put(operator.getName().toLowerCase(), operator);
			}else{
				LOG.debug("Operator "+operator.getName()+" -> "+operator.getProgramLanguage().toLowerCase()+" already added" );
			}
	
		}
		
		
	}

	public static void unregisterOperator(ITransformationOperator operator){
		if(transformationOperator.containsKey(operator.getProgramLanguage().toLowerCase())){
			if(transformationOperator.get(operator.getProgramLanguage().toLowerCase()).containsKey(operator.getName())){
				transformationOperator.get(operator.getProgramLanguage().toLowerCase()).remove(operator.getName());
			}
			
			if(transformationOperator.get(operator.getProgramLanguage().toLowerCase()).isEmpty()){
				transformationOperator.remove(operator.getProgramLanguage().toLowerCase());
			}
		}
	}
	
}
