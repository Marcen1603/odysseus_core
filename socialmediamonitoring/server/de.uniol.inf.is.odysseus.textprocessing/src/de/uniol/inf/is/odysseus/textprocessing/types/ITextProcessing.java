package de.uniol.inf.is.odysseus.textprocessing.types;

import java.util.Vector;


public interface ITextProcessing {
	
	ITextProcessing getInstance(String processType);
	
	String getType();
	
	void setTextProcessingTypeName(String processType);
	
	Vector<String> startTextProcessing(Vector<String> incomingText);
	
	void setOptions(String[] options);

}
