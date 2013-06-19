package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class NaiveBayes<T extends IMetaAttribute> extends AbstractClassifier<T> {

	private static int ctr = 0;
	private final String algo_type = "NaiveBayes";
	
	@Override
	public String getType(){
		return algo_type;
	}
	
	
	@Override
	public String startDetect(String text) {

		String erg = "";

		System.out.println("Der Satz wird analysiert: " + text);

		if (ctr % 2 == 0) {
			erg = "positive";
		} else {
			erg = "negative";
		}

		ctr++;

		return erg;

	}


	@Override
	public IClassifier<?> getInstance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
