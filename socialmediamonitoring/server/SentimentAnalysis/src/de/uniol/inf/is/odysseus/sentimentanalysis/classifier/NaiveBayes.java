package de.uniol.inf.is.odysseus.sentimentanalysis.classifier;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sentimentdetection.util.NGramm;
import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;

/**
 * Naive Bayes classifier
 * @author Marc Preuschaft
 *
 */
public class NaiveBayes extends AbstractClassifier {

	private final String algo_type = "NaiveBayes";

	private Map<String, Integer> positivewords = new HashMap<String, Integer>();
	private Map<String, Integer> negativewords = new HashMap<String, Integer>();

	static Logger logger = LoggerFactory.getLogger(NaiveBayes.class);

	public NaiveBayes() {
		// OSGI
	}

	public NaiveBayes(String domain) {
		setDomain(domain);
	}

	@Override
	public IClassifier getInstance(String domain) {
		return new NaiveBayes(domain);
	}

	@Override
	public void trainClassifier(TrainSetEntry trainentry, boolean isTrained) {
	

			if (trainentry.getTrueDecisio() == 1) {
				// positive
				
				System.out.println("POSITIV TRAINED" + trainentry.getTrueDecisio() + " " + trainentry.getSentence());
				
				for (int i = 0; i < ngramUpTo; i++) {
					for (String singleword : NGramm.ngrams(trainentry.getSentence(), ngram- i))
					{
						if (!positivewords.containsKey(singleword.toLowerCase())) 
						{
							positivewords.put(singleword.toLowerCase(), 1);
						} 
						else 
						{
							// exist, ctr + 1
							int ctr = positivewords.get(singleword.toLowerCase()) + 1;
							positivewords.put(singleword.toLowerCase(), ctr);
						}

					}
				}
			} else {
				// negative
				for (int i = 0; i < ngramUpTo; i++) {
					for (String singleword : NGramm.ngrams(trainentry.getSentence(), ngram
							- i)) {
						if (!negativewords
								.containsKey(singleword.toLowerCase())) {
							negativewords.put(singleword.toLowerCase(), 1);
						} else {
							// exist, ctr + 1
							int ctr = negativewords.get(singleword
									.toLowerCase()) + 1;
							negativewords.put(singleword.toLowerCase(), ctr);
						}

					}
				}
			}
						
		if(debugModus){
			System.out.println("positivewords size: " + positivewords.size());
			System.out.println("negativewords size: " + negativewords.size());
		}
		
	}

	@Override
	public int startDetect(String text) {
		// result
		int decision = 0;
	
		double decisionpos = 0.0;
		double decisionneg = 0.0;

		// split the sentence in single words
		for (int i = 0; i < ngramUpTo; i++) {
			for (String singleword : NGramm.ngrams(text, ngram - i)) {
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

				// positive rate
				decisionpos += a / (a + b);
				// negative rate
				decisionneg += b / (a + b);
			}
		}
		
		if(debugModus){
			System.out.println("----analysis----");
			System.out.println("sentence: " + text);
			System.out.println("positive: " + decisionpos);
			System.out.println("negative: " + decisionneg);
		}


		if (decisionpos > decisionneg) {
			decision = 1;

		} else {
			decision = -1;

		}

		return decision;

	}

	@Override
	public String getType() {
		return algo_type;
	}

}
