package de.uniol.inf.is.odysseus.sentimentanalysis.classifier;

import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;


/**
 * the interface for the classifier
 * 
 * @author Marc Preuschaft
 *
 */
public interface IClassifier {

	/**
	 * return a classifier instance by domain
	 * @param domain
	 * @return
	 */
	IClassifier getInstance(String domain);
	
	/**
	 * train the classifier with a TrainSetEntry
	 * @param trainentry
	 * @param isTrained
	 */
	void trainClassifier(TrainSetEntry trainentry, boolean isTrained);
	
	/**
	 * set the domain of the classifier
	 * @param domain
	 */
	void setDomain(String domain);
	
	/**
	 * set the option RemoveStopWords to true/false
	 * @param removeStopWords
	 */
	void setRemoveStopWords(boolean removeStopWords);
	
	/**
	 * set which ngram is to use
	 * @param ngram
	 */
	void setNgram(int ngram);
	
	/**
	 * set the option StemmtWords to ture/false
	 * @param stemmWords
	 */
	void setStemmWords(boolean stemmWords);
	
	/**
	 * set the N-Gram up to x
	 * @param ngramUpTo
	 */
	void setNgramUpTo(int ngramUpTo);
	
	/**
	 * set the debugClassifier modus
	 * @param debugModus
	 */
	void setDebugModus(boolean debugModus);

	/**
	 * return the classification of a sentence (1 positive), (-1 negative)
	 * @param text
	 * @return
	 */
	int startDetect(String text);
	
	/**
	 * return the option removeStopWords
	 * @return
	 */
	boolean getRemoveStopWords();

	/**
	 * return the option StemmWords
	 * @return
	 */
	boolean getStemmWords();
	
	/**
	 * return the name of the classifier
	 * @return
	 */
	String getType();
	
	/**
	 * return the domain of the classifier 
	 * @return
	 */
	String getDomain();
	
	
	/**
	 * return the option debugClassifier true/false
	 * @return
	 */
	boolean getDebugModus();
	
	
}
