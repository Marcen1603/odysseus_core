package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;


/**
 * @author Marc Preuschaft
 *
 */
public interface IClassifier {

	IClassifier getInstance(String domain);
	
	void trainClassifier(TrainSetEntry trainentry, boolean isTrained);
	
	void setDomain(String domain);
	
	void setRemoveStopWords(boolean removeStopWords);
	
	void setNgram(int ngram);
	
	void setStemmWords(boolean stemmWords);
	
	void setNgramUpTo(int ngramUpTo);

	int startDetect(String text);
	
	boolean getRemoveStopWords();

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
	
	
}
