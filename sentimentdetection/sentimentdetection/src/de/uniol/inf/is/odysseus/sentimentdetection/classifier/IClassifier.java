package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IClassifier<T extends IMetaAttribute> {

	IClassifier<?> getInstance(String domain);
	
	String getType();
	
	String getDomain();
	
	void setDomain(String domain);
	void setNgram(int ngram);
	
	int startDetect(String text);
	
	void trainClassifier(Map<String, Integer> trainingset);
	
	
}
