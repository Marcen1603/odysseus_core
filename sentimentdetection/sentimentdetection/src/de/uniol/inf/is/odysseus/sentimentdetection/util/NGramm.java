package de.uniol.inf.is.odysseus.sentimentdetection.util;



public class NGramm {
	
	public static String[] ngrams(String s, int len) {
	    String[] parts = s.split(" ");
	    String[] result = new String[parts.length - len + 1];
	    for(int i = 0; i < parts.length - len + 1; i++) {
	       StringBuilder sb = new StringBuilder();
	       for(int k = 0; k < len; k++) {
	           if(k > 0) sb.append(' ');
	           sb.append(parts[i+k]);
	       }
	       result[i] = sb.toString();
	    }
	    return result;
	}

}
