package de.uniol.inf.is.odysseus.sentimentdetection.stopwords;

/**
 * german stopwords class
 * @author Marc Preuschaft
 *
 */
public class StopWordsGerman extends AbstractStopWords {

	private final String language = "german";

	private static String[] stopwords = { "aber", "als", "am", "an", "auch",
			"auf", "aus", "bei", "bin", "bis", "bist", "da", "dadurch",
			"daher", "darum", "das", "da�", "dass", "dein", "deine", "dem",
			"den", "der", "des", "dessen", "deshalb", "die", "dies", "dieser",
			"dieses", "doch", "dort", "du", "durch", "ein", "eine", "einem",
			"einen", "einer", "eines", "er", "es", "euer", "eure", "f�r",
			"hatte", "hatten", "hattest", "hattet", "hier", "hinter", "ich",
			"ihr", "ihre", "im", "in", "ist", "ja", "jede", "jedem", "jeden",
			"jeder", "jedes", "jener", "jenes", "jetzt", "kann", "kannst",
			"k�nnen", "k�nnt", "machen", "mein", "meine", "mit", "mu�", "mu�t",
			"musst", "m�ssen", "m��t", "nach", "nachdem", "nein", "nicht",
			"nun", "oder", "seid", "sein", "seine", "sich", "sie", "sind",
			"soll", "sollen", "sollst", "sollt", "sonst", "soweit", "sowie",
			"und", "unser", "unsere", "unter", "vom", "von", "vor", "wann",
			"warum", "was", "weiter", "weitere", "wenn", "wer", "werde",
			"werden", "werdet", "weshalb", "wie", "wieder", "wieso", "wir",
			"wird", "wirst", "wo", "woher", "wohin", "zu", "zum", "zur", "�ber" };

	public StopWordsGerman() {
		setStopWords(stopwords);
	}

	@Override
	public IStopWords getInstance() {
		return new StopWordsGerman();
	}

	@Override
	public String getLanguage() {
		return language;
	}

}
