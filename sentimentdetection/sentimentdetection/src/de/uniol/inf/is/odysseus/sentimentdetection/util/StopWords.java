package de.uniol.inf.is.odysseus.sentimentdetection.util;

import java.util.ArrayList;
import java.util.List;

public class StopWords {
	
	private static List<String> stopwords = new ArrayList<String>();
	
	public static List<String> getStopWords(){
		
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
		
		return stopwords;
		
	}
	


}
