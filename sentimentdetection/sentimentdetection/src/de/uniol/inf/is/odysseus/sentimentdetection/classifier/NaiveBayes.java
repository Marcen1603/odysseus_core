package de.uniol.inf.is.odysseus.sentimentdetection.classifier;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class NaiveBayes<T extends IMetaAttribute> extends AbstractClassifier<T> {
	
	private final String algo_type = "NaiveBayes";
	private String domain;
	
	private Map<String, Integer> positivewords = new HashMap<String, Integer>();
	private Map<String, Integer> negativewords = new HashMap<String, Integer>();
	
	static Logger logger = LoggerFactory.getLogger(NaiveBayes.class);
	
	public NaiveBayes(){
		
	}
	
	public NaiveBayes(String name, String domain){
		super();
	}
	
	public NaiveBayes(String domain) {
		new NaiveBayes<T>(algo_type.toLowerCase(), domain);
		setDomain(domain);
	}
	

	@Override
	public String getType() {
		return algo_type;
	}
	
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getDomain(){
		return domain;
	}
	

	@Override
	public IClassifier<?> getInstance(String domain) {
		return new NaiveBayes<T>(domain);
	}
	
	
	@Override
	public void trainClassifier(Map<String, Integer> trainingset) {


	logger.debug("trainingsset size: " +  trainingset.size());
	logger.debug("domain: " + domain);

	//clear positivewords/negativewords
	positivewords.clear();
	negativewords.clear();

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
		}

		logger.debug("classifier successfully initialized!");
		logger.debug("positivewords size: "+ positivewords.size());
		logger.debug("negativewords size" + negativewords.size());
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
		
		
		logger.debug("----analysis----");
		logger.debug("record: " + text);
		logger.debug("positive: " + decisionpos);
		logger.debug("negative: " + decisionneg);


		if (decisionpos > decisionneg) {
			decision = 1;
		} else {
			decision = -1;
		}

		return decision;

	}

}
