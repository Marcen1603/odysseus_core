package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import de.uniol.inf.is.odysseus.sentimentdetection.util.*;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ResultEntity;

public class KNearestNeighbor extends AbstractClassifier {

	static Logger logger = LoggerFactory.getLogger(KNearestNeighbor.class);
	
	private final String algo_type = "KNearestNeighbor";
	private String domain;
	private int ngram = 1;

	private Map<String, Integer> freq = new HashMap<String, Integer>();
	private Map<List<String>, Integer> trainfeatures = new HashMap<List<String>, Integer>();
	private List<String> stopwords = new ArrayList<String>();

	private int ntr = 0;

	public KNearestNeighbor() {

	}

	public KNearestNeighbor(String domain) {
		setDomain(domain);
	}

	@Override
	public IClassifier getInstance(String domain) {
		return new KNearestNeighbor(domain);
	}
	
	@Override
	public void trainClassifier(Map<String, Integer> trainingset, boolean isTrained) {

	if(!isTrained){	
		freq.clear();
		trainfeatures.clear();
		stopwords.clear();
		ntr = trainingset.size();
		setStopWords();
	}else{
		ntr += trainingset.size();
	}
	

		for (Map.Entry<String, Integer> e : trainingset.entrySet()) {

			List<String> words = getWords(e.getKey());

			for (String word : words) {
				if (!freq.containsKey(word)) {
					freq.put(word, 1);
				} else {
					int ctr = freq.get(word) + 1;
					freq.put(word, ctr);
				}
			}

			trainfeatures.put(words, e.getValue());
		}

	}

	@Override
	public int startDetect(String text) {
		/*
		 * 1. Input sentence 2. split it up 3. clean up, remove stop words,
		 * punctuation 4. stemming
		 */

		// final decision
		int decision = 0;
		
		// look at top 5 results / 5-NN classifier
		int classifierNN = 5;
		
		// counter for positive and negative decisions
		int decisionpos = 0;
		int decisionneg = 0;

		List<String> testwords = getWords(text);
		List<ResultEntity> results = new ArrayList<ResultEntity>();

		for (Map.Entry<List<String>, Integer> e : trainfeatures.entrySet()) {

			List<String> commonwords = new ArrayList<String>();

			Iterator<String> iterator = e.getKey().iterator();
			
			while (iterator.hasNext()) {
				String singleword = iterator.next();
				if (testwords.contains(singleword)) {
					commonwords.add(singleword);
				}
			}

			double score = 0.0;

			for (String word : commonwords) {
				score += Math.log(ntr / freq.get(word));
			}

			results.add(new ResultEntity(score, e.getValue()));
		}
		
		Collections.sort(results, Collections.reverseOrder());

		// only the first 5(classifierNN) scores desc
		for (ResultEntity entity : results.subList(0, classifierNN)) {
			if (entity.getLabel() == 1) {
				decisionpos++;
			} else {
				decisionneg++;
			}
		}

		
		if (decisionneg > decisionpos) {
			decision = -1;
		} else {
			decision = 1;
		}

		return decision;
	}





	/*
	 * remove duplicates with order 
	 */
	private List<String> removeDuplicateWithOrder(List<String> oldList) {
		Set<Object> set = new HashSet<Object>();
		List<String> newList = new ArrayList<String>();
		for (Iterator<String> iter = oldList.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element.toString());
		}
		oldList.clear();
		oldList.addAll(newList);

		return oldList;
	}
	
	
	private List<String> getWords(String text) {
		PorterStemmer stemmer = new PorterStemmer();

		List<String> words = new ArrayList<String>();

		// split text in singlewords
		for (String singleword : NGramm.ngrams(text, ngram)) {
			//only add words length > 2
			if (singleword.trim().length() > 2) {
				// only add none stopwords
				if (!stopwords.contains(singleword)) {
					String stem = stemmer.stem(singleword);
					words.add(stem);
				}
			}
		}
		// remove duplicates words
		return removeDuplicateWithOrder(words);
	}
	

	private void setStopWords() {
		this.stopwords = StopWords.getStopWords();
	}
	
	
	@Override
	public String getType() {
		return algo_type;
	}
	
	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Override
	public void setNgram(int ngram){
		this.ngram = ngram;
	}

	

}
