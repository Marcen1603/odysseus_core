package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IClassifier<T extends IMetaAttribute> {

	IClassifier<?> getInstance(String domain);
	
	void setDomain(String domain);
	
	void setNgram(int ngram);
	
	void trainClassifier(Map<String, Integer> trainingset);
	
	int startDetect(String text);
	
	String getType();
	
	String getDomain();
		
}
