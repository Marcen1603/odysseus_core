package de.uniol.inf.is.odysseus.sentimentdetection.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.sentimentdetection.util.*;


public class KNearestNeighbor<T extends IMetaAttribute> extends
		AbstractClassifier<T> {

	private final String algo_type = "KNearestNeighbor";

	private Map<String, Integer> freq = new HashMap<String, Integer>();
	private Map<String, Integer> trainfeatures = new HashMap<String, Integer>();
	
	private int ntr = 0;
	private List<String> stopwords = new ArrayList<String>();
	private String domain;
	
	
	public KNearestNeighbor(){
		
	}
	
	public KNearestNeighbor(String name, String domain){
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

		int decision = 0;
		/*
		 * 1. Input sentence 2. split it up 3. clean up, remove stop words,
		 * punctuation 4. stemming
		 * 
		 * 
		 * use: Porter Stemming Algorithm
		 * http://ccl.pku.edu.cn/doubtfire/NLP/Lexical_Analysis
		 * /Word_Lemmatization/Porter/Porter%20Stemming%20Algorithm.htm
		 */
		
		List<String> testwords = getWords(text);
		TreeMap<Double, Integer> results = new TreeMap<Double, Integer>();
		
		
		
	
		for (Map.Entry<String, Integer> e : trainfeatures.entrySet()) {
			
			List<String> commonwords = new ArrayList<String>();
			
			if(testwords.contains(e.getKey())){
				commonwords.add(e.getKey());
			}
			
			double score = 0.0;
			
			for(String word : commonwords ){
				score += Math.log(ntr/freq.get(word));
			}
			
			results.put(score, e.getValue());
			
			
		}
		
		 //look at top 5 results / 5-NN classifier
		int classifierNN = 5;
		int numones = 0; 
		int numnegones = 0;
		
		


		//bad solution... fix soon...only the first 5(classifierNN) scores desc
	    for ( Map.Entry<Double, Integer> e : results.entrySet() ) {
	    
	    		if(e.getValue() == 1){
		    		numones++;
		    	}else{
		    		numnegones++;
		    	}
		    	System.out.println( e.getKey() + "= "+ e.getValue() );
		    

	    
	    }
	   
	 
	    if(numnegones>numones){
	    	decision=-1;
	    }else{
	    	decision = 1;
	    }


		return decision;
	}

	private List<String> getWords(String text) {
		PorterStemmer stemmer = new PorterStemmer();
		
		List<String> words = new ArrayList<String>();
	
		// split text in singlewords
		for (String singleword : text.split(" ")) {
			if (singleword.length() > 2) {
				//only add non stopwords
				if(!stopwords.contains(singleword)){
					
					String stem = stemmer.stem(singleword);
					System.out.println("input: " + singleword);
					System.out.println("output: " + stem);
					
					System.out.println("");
					System.out.println("");
					
					words.add(stem);
				}
			}
		}
		
		
		return words;

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
		createStopWords();
		
		ntr = trainingset.size();
		
		for (Map.Entry<String, Integer> e : trainingset.entrySet()) {

	
			List<String> words = getWords(e.getKey());
			
			for(String word : words ){
				   System.out.println(word);
				   
				   
				   trainfeatures.put(word, e.getValue());
				   
				   if(!freq.containsKey(word)){
					   freq.put(word, 1);
				   }else{
					   int ctr = freq.get(word) + 1;
					   freq.put(word, ctr);
				   }
				   
			}
			
		
		
		
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

}
