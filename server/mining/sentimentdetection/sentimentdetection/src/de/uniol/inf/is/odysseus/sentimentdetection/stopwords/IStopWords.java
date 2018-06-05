package de.uniol.inf.is.odysseus.sentimentdetection.stopwords;

/**
 * the interface for StopWords
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
	 * remove the stopwords from the sentence
	 * @param text
	 * @return
	 */
	String removeStopWords(String text);
	
	/**
	 * stemm a sentence
	 * @param record
	 * @return
	 */
	String stemmSentence(String sentence);
	
}
