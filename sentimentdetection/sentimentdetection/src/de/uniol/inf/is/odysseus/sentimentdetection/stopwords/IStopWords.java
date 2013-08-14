package de.uniol.inf.is.odysseus.sentimentdetection.stopwords;

/**
 * @author Marc Preuschaft
 *
 */
public interface IStopWords {
	
	/**
	 * return a instance of StopWords
	 * @return
	 */
	IStopWords getInstance();
	
	/**
	 * return the language of StopWordset
	 * @return
	 */
	String getLanguage();
	
	/**
	 * removes the stopwords from the text
	 * @param text
	 * @return
	 */
	String removeStopWords(String text);
	
	/**
	 * @param record
	 * @return
	 */
	String stemmRecord(String record);
	
}
