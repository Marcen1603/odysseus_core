package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.List;


import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;


public interface IClassifier {

	IClassifier getInstance(String domain);
	
	void setDomain(String domain);
	
	void setRemoveStopWords(boolean removeStopWords);
	
	void setNgram(int ngram);
	
	void trainClassifier(List<TrainSetEntry> trainingset, boolean isTrained);
	
	int startDetect(String text);
	
	String getType();
	
	String getDomain();
	
	boolean getRemoveStopWords();
		
}
