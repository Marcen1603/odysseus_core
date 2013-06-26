package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.sentimentdetection.util.*;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ResultEntity;

public class KNearestNeighbor<T extends IMetaAttribute> extends
		AbstractClassifier<T> {

	private final String algo_type = "KNearestNeighbor";
	private String domain;

	private Map<String, Integer> freq = new HashMap<String, Integer>();
	private Map<List<String>, Integer> trainfeatures = new HashMap<List<String>, Integer>();
	private List<String> stopwords = new ArrayList<String>();

	private int ntr = 0;

	public KNearestNeighbor() {

	}

	public KNearestNeighbor(String name, String domain) {
		super();
	}

	public KNearestNeighbor(String domain) {
		new KNearestNeighbor<T>(algo_type.toLowerCase(), domain);
		setDomain(domain);
	}

	@Override
	public IClassifier<?> getInstance(String domain) {
		return new KNearestNeighbor<T>(domain);
	}

	@Override
	public String getType() {
		return algo_type;
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

	private List<String> getWords(String text) {
		PorterStemmer stemmer = new PorterStemmer();

		List<String> words = new ArrayList<String>();

		// split text in singlewords
		for (String singleword : text.split(" ")) {
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

	private void createStopWords() {

		stopwords.add("i");
		stopwords.add("a");
		stopwords.add("about");
		stopwords.add("an");
		stopwords.add("are");
		stopwords.add("as");
		stopwords.add("at");
		stopwords.add("be");
		stopwords.add("by");
		stopwords.add("com");
		stopwords.add("de");
		stopwords.add("en");
		stopwords.add("for");
		stopwords.add("rom");
		stopwords.add("how");
		stopwords.add("in");
		stopwords.add("is");
		stopwords.add("it");
		stopwords.add("la");
		stopwords.add("of");
		stopwords.add("on");
		stopwords.add("or");
		stopwords.add("that");
		stopwords.add("the");
		stopwords.add("this");
		stopwords.add("to");
		stopwords.add("was");
		stopwords.add("what");
		stopwords.add("when");
		stopwords.add("where");
		stopwords.add("who");
		stopwords.add("will");
		stopwords.add("with");
		stopwords.add("und");
		stopwords.add("the");
		stopwords.add("and");
		stopwords.add("but");
		stopwords.add("its");
		stopwords.add("it's");

	}

	@Override
	public void trainClassifier(Map<String, Integer> trainingset) {

		freq.clear();
		trainfeatures.clear();
		stopwords.clear();

		createStopWords();

		ntr = trainingset.size();

		for (Map.Entry<String, Integer> e : trainingset.entrySet()) {

			List<String> words = getWords(e.getKey());

			for (String word : words) {
				System.out.println(word);

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
	public String getDomain() {
		return domain;
	}

	@Override
	public void setDomain(String domain) {
		this.domain = domain;
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

}
