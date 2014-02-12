package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

public class SentenceFactory {
	private Map<String, Class<? extends Sentence>> sentenceTypes = new HashMap<String, Class<? extends Sentence>>();
	
	public static SentenceFactory getInstance() {
		if (instance == null) {
			instance = new SentenceFactory();
		}
		return instance;
	}
	
	private static SentenceFactory instance;
	private SentenceFactory() {
		registerSentenceType("RMC", RMCSentence.class);
		registerSentenceType("GGA", GGASentence.class);
		registerSentenceType("HDG", HDGSentence.class);
	}
	
	public Sentence createSentence(String nmea) {
		String sid = SentenceUtils.getSentenceId(nmea);
		
		if (!hasSentenceType(sid)) {
			String msg = String.format("Unknown sentence type '%s'", sid);
			throw new IllegalArgumentException(msg);
		}

		Sentence parser = null;
		Class<?> klass = nmea.getClass();

		try {
			Class<? extends Sentence> c = sentenceTypes.get(sid);
			Constructor<? extends Sentence> co = c.getConstructor(klass);
			parser = co.newInstance(nmea);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to instanciate sentence.", e);
		}
		
		return parser;
	}
	
	public boolean hasSentenceType(String type) {
		return sentenceTypes.containsKey(type);
	}
	
	public void registerSentenceType(String type, Class<? extends Sentence> sentenceType) {
		if (hasSentenceType(type)) {
			throw new IllegalArgumentException(String.format("Sentence type '%s' already registered", type));
		}
		try {
			sentenceType.getConstructor(String.class);
			sentenceTypes.put(type, sentenceType);
		} catch (NoSuchMethodException e) {
			String msg = "Required constructors not found; Sentence(String)";
			throw new IllegalArgumentException(msg, e);
		}
	}
}
