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

	private Map<String, Integer> freq = new HashMap<String, Integer>();
	private Map<List<String>, Integer> trainfeatures = new HashMap<List<String>, Integer>();
	
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
	    List<ResultEntity> results  = new ArrayList<ResultEntity>();
		
	
		for (Map.Entry<List<String>, Integer> e : trainfeatures.entrySet()) {
			
			List<String> commonwords = new ArrayList<String>();
			
			System.out.println(e.getKey());
			
			Iterator<String> iterator = e.getKey().iterator();
			while (iterator.hasNext()) {
				
				System.out.println(iterator.next());
				
				if(testwords.contains(iterator.next())){
					commonwords.add(iterator.next());
				}
			}
			
			
	
			double score = 0.0;
			
			for(String word : commonwords ){
				System.out.println("log("+ntr+"/("+ freq.get(word) +")) = " + Math.log(ntr/freq.get(word)));
				score += Math.log(ntr/freq.get(word));
			}
			
			
			results.add(new ResultEntity(score,  e.getValue()));  
			
			
			
		}
		
		 //look at top 5 results / 5-NN classifier
		int classifierNN = 5;
		int decisionpos = 0; 
		int decisionneg = 0;
		
		
		Collections.sort(results ,Collections.reverseOrder());
		
		//only the first 5(classifierNN) scores desc
	    for(ResultEntity entity : results.subList(0,classifierNN))
	    {
	    	System.out.println( entity.getScore() + "= "+ entity.getLabel() );
	    	if(entity.getLabel() == 1){
	    		decisionpos++;
	    	}else{
	    		decisionneg++;
	    	}
	    	
	    }
	    		
	    if(decisionneg>decisionpos){
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
			if (singleword.trim().length() > 2) {
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
		
		//remove duplicates words
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
		createStopWords();
		
		ntr = trainingset.size();
		
		for (Map.Entry<String, Integer> e : trainingset.entrySet()) {

	
			List<String> words = getWords(e.getKey());
			
			for(String word : words ){
				   System.out.println(word);

	
				   if(!freq.containsKey(word)){
					   freq.put(word, 1);
				   }else{
					   int ctr = freq.get(word) + 1;
					   freq.put(word, ctr);
				   }
				   
			}
			
			   trainfeatures.put(words, e.getValue());
			
		}
		
		
			for ( Map.Entry<String, Integer> e : freq.entrySet() )
					System.out.println( e.getKey() + "\t\t"+ e.getValue() );
		
		
	
		
	}

	
	
	/** List order maintained 
	 * @return **/

	private List<String> removeDuplicateWithOrder(List<String> arlList)
	 {
	 Set set = new HashSet();
	 List<String> newList = new ArrayList();
	 for (Iterator<String> iter = arlList.iterator();    iter.hasNext(); ) {
	 Object element = iter.next();
	   if (set.add(element))
		  newList.add(element.toString());
		}
		arlList.clear();
		arlList.addAll(newList);
		
		return arlList;
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
