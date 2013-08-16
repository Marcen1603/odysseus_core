package de.uniol.inf.is.odysseus.sentimentdetection.stopwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class StopWordsRegistry {
	
	static Logger logger = LoggerFactory.getLogger(StopWordsRegistry.class);
	
	static Map<String, IStopWords> stopWordsSet = new HashMap<String, IStopWords>();
	
	
	public static IStopWords getStopWordsByLanguage(String language){

		IStopWords stopwords = stopWordsSet.get(language.toLowerCase());
		IStopWords newStopWords = stopwords.getInstance();
		
		return newStopWords;
		
	}
	
	public static void registerStopWordsSet(IStopWords stopWords) {

		if (!stopWordsSet.containsKey(stopWords.getLanguage().toLowerCase())) {
			stopWordsSet.put(stopWords.getLanguage().toLowerCase(),stopWords);
		} else {
			logger.debug("StopWords for:  " + stopWords.getLanguage().toLowerCase()
					+ " already added");
		}

	}

	public static void unregisterStopWordsSet(IStopWords stopWords) {
		if (stopWordsSet.containsKey(stopWords.getLanguage().toLowerCase())) {
			stopWordsSet.remove(stopWords.getLanguage().toLowerCase());
		}

	}
	
	public static List<String> getValidLanguage(){
		return new ArrayList<String>(stopWordsSet.keySet());
	}
			

}
