package de.uniol.inf.is.odysseus.sentimentanalysis.stopwords;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.sentimentdetection.util.NGramm;
import de.uniol.inf.is.odysseus.sentimentdetection.util.PorterStemmerEnglish;

public abstract class AbstractStopWords implements IStopWords {

	protected Set<String> stopWordSet = new HashSet<String>();

	@Override
	public String removeStopWords(String string) {
		String result = "";
		String[] words = string.split("\\s+");
		for (String word : words) {
			if (word.isEmpty())
				continue;
			if (isStopword(word))
				continue;
			result += (word + " ");
		}
		return result;
	}

	public boolean isStopword(String word) {
		if (stopWordSet.contains(word)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String stemmSentence(String sentence) {
		String result = "";

		PorterStemmerEnglish stemmer = new PorterStemmerEnglish();

		for (String singleword : NGramm.ngrams(sentence, 1)) {
			String stem = stemmer.stem(singleword);
			result += stem + " ";
		}

		return result;
	}

	public void setStopWords(String[] stopwords) {
		this.stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
	}

	public Set<String> getStopWords() {
		return stopWordSet;
	}

}
