package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class ClassifierRegistry <T extends Tuple<?>>{
	
	static Logger logger = LoggerFactory.getLogger(ClassifierRegistry.class);
	
	static Map<String, IClassifier<?>> classifierAlgoTypes = new HashMap<String, IClassifier<?>>();
	
	
	public static void registerClassifierAlgo(IClassifier<?> classifierAlgo){
		
		if(!classifierAlgoTypes.containsKey(classifierAlgo.getType().toLowerCase())){
			classifierAlgoTypes.put(classifierAlgo.getType().toLowerCase(), classifierAlgo);
		}else{
			logger.debug("Classifier "+ classifierAlgo.getType().toLowerCase() + " already added");
		}
		
	}
	
	
	public static void unregisterClassifierAlgo(IClassifier<?> classifierAlgo){
		
		if(classifierAlgoTypes.containsKey(classifierAlgo.getType().toLowerCase())){
			classifierAlgoTypes.remove(classifierAlgo.getType().toLowerCase());
		}
	
	}
	
	
	public static IClassifier<?> getClassifierByName(String name){
	
		if(classifierAlgoTypes.containsKey(name.toLowerCase())){
			IClassifier<?> algo = classifierAlgoTypes.get(name.toLowerCase());
			
			return algo;
			
		}else{
			logger.debug("The classifier: " + name +" is not available");
			return null;
		}
			
	}

}
