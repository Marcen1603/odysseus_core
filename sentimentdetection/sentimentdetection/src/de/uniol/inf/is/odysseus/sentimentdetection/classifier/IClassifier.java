package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.List;


import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;


/**
 * @author Marc Preuschaft
 *
 */
public interface IClassifier {

	IClassifier getInstance(String domain);
	
	void trainClassifier(List<TrainSetEntry> trainingset, boolean isTrained);
	
	void setDomain(String domain);
	
	void setRemoveStopWords(boolean removeStopWords);
	
	void setNgram(int ngram);
	
	void setStemmWords(boolean stemmWords);
	
	void setNgramUpTo(int ngramUpTo);
	
	int startDetect(String text);
	
	boolean getRemoveStopWords();
	
	boolean getStemmWords();
	
	String getType();
	
	String getDomain();
	
}
