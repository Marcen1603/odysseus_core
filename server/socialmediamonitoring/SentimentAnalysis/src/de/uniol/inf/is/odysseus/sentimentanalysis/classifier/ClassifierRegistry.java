package de.uniol.inf.is.odysseus.sentimentanalysis.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * manage the classifier 
 */
public class ClassifierRegistry{

	static Logger logger = LoggerFactory.getLogger(ClassifierRegistry.class);

	static Map<String, IClassifier> classifierAlgoTypes = new HashMap<String, IClassifier>();

	static Map<String, IClassifier> classifierDomains = new HashMap<String, IClassifier>();

	
	/**
	 * return a classifier instance by classifier type and domain
	 * @param classifierType
	 * @param domain
	 * @return
	 */
	public static IClassifier getClassifierByTypeAndDomain(String classifierType, String domain) {

		if (!classifierAlgoTypes.containsKey(classifierType.toLowerCase())) {
			logger.info("Classifier: " + classifierType + " is not valid.");
				
			return null;
		}

		if (classifierDomains.containsKey(domain.toLowerCase())) {
			IClassifier classifier = classifierDomains.get(domain
					.toLowerCase());

			// Classifier with domain and classifierType exist
			if (classifier.getType().toLowerCase()
					.equals(classifierType.toLowerCase())) {
				return classifier;
			} else {
				/*
				 *  Classifier with domain exist, but have a different 
				 *  classifierType
				 */ 
				logger.info("Classifier with: '" + domain.toLowerCase()
						+ "' already exists with a different classifierType.");
				return null;
			}
		} else {
			/*
			 *  Classifier with domain and classifierType does not exist, create new
			 *  instance
			 */
			IClassifier classifier = classifierAlgoTypes.get(classifierType.toLowerCase());
			
			IClassifier newClassifier = classifier.getInstance(domain.toLowerCase());
			
			classifierDomains.put(domain.toLowerCase(), newClassifier);

			return newClassifier;
		}
	}
	
	/**
	 * remove a classifier by domain
	 * @param domain
	 */
	public static void unregisterDomain(String domain){
		IClassifier classifier = classifierDomains.get(domain
				.toLowerCase());
		if(classifier != null){
			classifierDomains.remove(domain.toLowerCase());
		}
		
	}
	
	/**
	 * return a list of available classifier
	 * @return
	 */
	public static List<String> getValidClassifier(){
		return new ArrayList<String>(classifierAlgoTypes.keySet());
	}
	
	
	/**
	 * register the classifier by starting odysseus
	 * @param classifierAlgo
	 */
	public static void registerClassifierAlgo(IClassifier classifierAlgo) {

		if (!classifierAlgoTypes.containsKey(classifierAlgo.getType()
				.toLowerCase())) {
			classifierAlgoTypes.put(classifierAlgo.getType().toLowerCase(),
					classifierAlgo);
		} else {
			logger.debug("Classifier " + classifierAlgo.getType().toLowerCase()
					+ " already added");
		}

	}

	/**
	 * unregister the classifier by closing odysseus
	 * @param classifierAlgo
	 */
	public static void unregisterClassifierAlgo(IClassifier classifierAlgo) {
		if (classifierAlgoTypes.containsKey(classifierAlgo.getType()
				.toLowerCase())) {
			classifierAlgoTypes.remove(classifierAlgo.getType().toLowerCase());
		}

	}

}
