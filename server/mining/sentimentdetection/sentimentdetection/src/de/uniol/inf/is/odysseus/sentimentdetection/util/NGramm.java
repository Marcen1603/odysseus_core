package de.uniol.inf.is.odysseus.sentimentdetection.util;


/**
 * class to build N-Grams
 * @author Marc Preuschaft
 *
 */
public class NGramm {

	/**
	 * return a string array of ngrams
	 * @param s
	 * @param len
	 * @return
	 */
	public static String[] ngrams(String s, int len) {
		if (checkNgramString(s, len)) {
			String[] parts = s.split(" ");
			String[] result = new String[parts.length - len + 1];
			for (int i = 0; i < parts.length - len + 1; i++) {
				StringBuilder sb = new StringBuilder();
				for (int k = 0; k < len; k++) {
					if (k > 0)
						sb.append(' ');
					sb.append(parts[i + k]);
				}
				result[i] = sb.toString();
			}
			return result;
		}
		String[] result = new String[0];

		return result;
	}

	/**
	 * check if is possible to create a ngram  
	 * @param s
	 * @param len
	 * @return
	 */
	private static boolean checkNgramString(String s, int len) {
		String[] parts = s.split(" ");
		if (parts.length >= len) {
			return true;
		}
		return false;

	}

}
