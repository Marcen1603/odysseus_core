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
		LOG.debug("Operator" + operator.getName() +" für "+operator.getProgrammLanguage() +" wird hinzugefügt");
		
		//Programmiersprache noch nicht vorhanden
		if(!transformationOperator.containsKey(operator.getProgrammLanguage().toLowerCase())){
		
			//Operator noch nicht vorhanden?
			if(transformationOperator.get(operator.getProgrammLanguage().toLowerCase())== null || !transformationOperator.get(operator.getProgrammLanguage().toLowerCase()).containsKey(operator.getName())){
				//Operatorname + ITransformationOperator Map erzeugen
				Map<String,ITransformationOperator> tempMap = new HashMap<String,ITransformationOperator>();
				tempMap.put(operator.getName(), operator);
				
				//verschachtelte Map abspeichern
				transformationOperator.put(operator.getProgrammLanguage().toLowerCase(), tempMap);
			}else{
				LOG.debug("Operator "+operator.getName()+" -> "+operator.getProgrammLanguage().toLowerCase()+" already added" );
			}
			
		}else{
			//Programmiersprache existiert bereits nur Operator hinzufügen			
			if(!transformationOperator.get(operator.getProgrammLanguage().toLowerCase()).containsKey(operator.getName())){
				Map<String,ITransformationOperator> tempMap = transformationOperator.get(operator.getProgrammLanguage().toLowerCase());
				tempMap.put(operator.getName(), operator);
			}else{
				LOG.debug("Operator "+operator.getName()+" -> "+operator.getProgrammLanguage().toLowerCase()+" already added" );
			}
	
		}
		
		
	}

	public static void unregisterOperator(ITransformationOperator operator){
		if(transformationOperator.containsKey(operator.getProgrammLanguage().toLowerCase())){
			if(transformationOperator.get(operator.getProgrammLanguage().toLowerCase()).containsKey(operator.getName())){
				transformationOperator.get(operator.getProgrammLanguage().toLowerCase()).remove(operator.getName());
			}
			
			if(transformationOperator.get(operator.getProgrammLanguage().toLowerCase()).isEmpty()){
				transformationOperator.remove(operator.getProgrammLanguage().toLowerCase());
			}
		}
	}
	
}
