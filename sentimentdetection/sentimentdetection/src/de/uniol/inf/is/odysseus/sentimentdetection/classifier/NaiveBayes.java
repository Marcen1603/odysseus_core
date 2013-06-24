package de.uniol.inf.is.odysseus.sentimentdetection.classifier;


import java.util.HashMap;
import java.util.Map;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class NaiveBayes<T extends IMetaAttribute> extends AbstractClassifier<T> {

	private final String algo_type = "NaiveBayes";

	static Map<String, Integer> positivewords = new HashMap<String, Integer>();
	static Map<String, Integer> negativewords = new HashMap<String, Integer>();

	public void trainClassifier(Map<String, Integer> trainingset) {

	System.out.println("Trainingsset besteht aus: "+ trainingset.size());
		
	System.out.println("Positiv/Negativ Wortliste erstellen....");
		
	//	for ( Map.Entry<String, Integer> e : trainingset.entrySet() )
	// System.out.println( e.getKey() + "    "+ e.getValue() );

		for (Map.Entry<String, Integer> e : trainingset.entrySet()) {

			if (e.getValue() == 1) {
				// positive
				for (String singleword : e.getKey().split(" ")) {
					if (!positivewords.containsKey(singleword.toLowerCase())) {
						  positivewords.put(singleword.toLowerCase(), 1);
					} else {
						// exist, ctr + 1
						int ctr = positivewords.get(singleword.toLowerCase())+1;
						positivewords.put(singleword.toLowerCase(), ctr);
					}

				}
			} else {
				// negative
				for (String singleword : e.getKey().split(" ")) {
					if (!negativewords.containsKey(singleword.toLowerCase())) {
							negativewords.put(singleword.toLowerCase(), 1);
					} else {
						// exist,  ctr + 1
						int ctr = negativewords.get(singleword.toLowerCase())+1;
						negativewords.put(singleword.toLowerCase(), ctr);
					}

				}
			}

			// System.out.println( e.getKey() + "="+ e.getValue() );
		}

		System.out.println("Classifier erfolgreich initialisiert!");
		System.out.println("positivewords besteht aus: "+ positivewords.size());
		System.out.println("negativewords besteht aus: "+ negativewords.size());
		
	}

	@Override
	public String getType() {
		return algo_type;
	}

	@Override
	public int startDetect(String text) {

		//result
		int decision = 0;
		
		double decisionpos = 0.0;
		double decisionneg = 0.0;

		//split the record in single words
		for (String singleword : text.split(" ")) {

			double a = 1.0;
			double b = 1.0;

			// positive count
			if (positivewords.containsKey(singleword.toLowerCase())) {
				a = a + positivewords.get(singleword.toLowerCase());
			}

			// negative count
			if (negativewords.containsKey(singleword.toLowerCase())) {
				b = b + negativewords.get(singleword.toLowerCase());
			}

			//positive rate
			decisionpos += a/(a+b);
			//negative rate
			decisionneg += b/(a+b);
		}
		
		System.out.println("----analysis----");
		System.out.println("record: " + text);
		System.out.println("positive: " + decisionpos);
		System.out.println("negative: " + decisionneg);

		if (decisionpos > decisionneg) {
			decision = 1;
		} else {
			decision = -1;
		}

		return decision;

	}

	@Override
	public IClassifier<?> getInstance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
