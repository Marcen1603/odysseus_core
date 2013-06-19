package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IClassifier<T extends IMetaAttribute> {

	IClassifier<?> getInstance(String name);
	
	String getType();
	
	String startDetect(String text);
	
}
