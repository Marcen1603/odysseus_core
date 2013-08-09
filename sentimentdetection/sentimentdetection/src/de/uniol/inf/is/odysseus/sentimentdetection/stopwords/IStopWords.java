package de.uniol.inf.is.odysseus.sentimentdetection.stopwords;



public interface IStopWords {
	
	IStopWords getInstance();

	String getLanguage();
	
	String removeStopWords(String text);
	
	String stemmRecord(String record);
	
}
