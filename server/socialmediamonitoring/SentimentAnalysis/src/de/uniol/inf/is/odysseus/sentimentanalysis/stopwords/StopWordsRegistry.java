package de.uniol.inf.is.odysseus.sentimentanalysis.stopwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * class to mange the stopwords
 * @author Marc Preuschaft
 *
 */
public class StopWordsRegistry {
	
	static Logger logger = LoggerFactory.getLogger(StopWordsRegistry.class);
	
	static Map<String, IStopWords> stopWordsSet = new HashMap<String, IStopWords>();
	
	
	/**
	 * return a StopWord instance by language
	 * @param language
	 * @return
	 */
	public static IStopWords getStopWordsByLanguage(String language){

		IStopWords stopwords = stopWordsSet.get(language.toLowerCase());
		IStopWords newStopWords = stopwords.getInstance();
		
		return newStopWords;
		
	}
	
	/**
	 * @param stopWords
	 */
	public static void registerStopWordsSet(IStopWords stopWords) {

		if (!stopWordsSet.containsKey(stopWords.getLanguage().toLowerCase())) {
			stopWordsSet.put(stopWords.getLanguage().toLowerCase(),stopWords);
		} else {
			logger.debug("StopWords for:  " + stopWords.getLanguage().toLowerCase()
					+ " already added");
		}

	}

	/**
	 * @param stopWords
	 */
	public static void unregisterStopWordsSet(IStopWords stopWords) {
		if (stopWordsSet.containsKey(stopWords.getLanguage().toLowerCase())) {
			stopWordsSet.remove(stopWords.getLanguage().toLowerCase());
		}

	}
	
	/**
	 * return a list of available language
	 * @return
	 */
	public static List<String> getValidLanguage(){
		return new ArrayList<String>(stopWordsSet.keySet());
	}
			

}
