package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.Map;


public interface IClassifier {

	IClassifier getInstance(String domain);
	
	void setDomain(String domain);
	
	void setNgram(int ngram);
	
	void trainClassifier(Map<String, Integer> trainingset);
	
	int startDetect(String text);
	
	String getType();
	
	String getDomain();
		
}
