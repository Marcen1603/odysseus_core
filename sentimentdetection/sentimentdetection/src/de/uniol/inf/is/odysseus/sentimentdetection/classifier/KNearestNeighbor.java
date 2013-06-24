package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class KNearestNeighbor<T extends IMetaAttribute> extends AbstractClassifier<T> {

	@Override
	public IClassifier<?> getInstance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int startDetect(String text) {
		// TODO Auto-generated method stub
		
		/*
		 * 1. Input sentence
		 * 2. split it up
		 * 3. clean up, remove stop words, punctuation
		 * 4. stemming 
		 * 
		 * 
		 * use: Porter Stemming Algorithm
		 * http://ccl.pku.edu.cn/doubtfire/NLP/Lexical_Analysis/Word_Lemmatization/Porter/Porter%20Stemming%20Algorithm.htm
		 * 
		 */
		return 0;
	}


	@Override
	public void trainClassifier(Map<String, Integer> trainingset) {
		// TODO Auto-generated method stub
		
	}

}
