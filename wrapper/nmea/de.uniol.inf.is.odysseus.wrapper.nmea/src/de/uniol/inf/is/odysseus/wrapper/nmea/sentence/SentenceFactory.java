package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

/**
 * The sentence factory creates sentences from strings in
 * {@link #createSentence(String)}. New sentence types can be registered by
 * calling {@link #registerSentenceType(String, Class)} and passing the
 * sentenceId as String and the class, that extends {@link Sentence} and handles
 * the parsing of the given string.
 * 
 * @author jboger <juergen.boger@offis.de>
 * 
 */
public class SentenceFactory {
	/** Holds the registered sentence types. */
	private Map<String, Class<? extends Sentence>> sentenceTypes = new HashMap<String, Class<? extends Sentence>>();
	
	/**
	 * Gets the instance of this singleton class.
	 * @return
	 * SentenceFactory instance.
	 */
	public static SentenceFactory getInstance() {
		if (instance == null) {
			instance = new SentenceFactory();
		}
		return instance;
	}
	
	/** Holds the singleton instance. */
	private static SentenceFactory instance;
	
	/**
	 * Hides the constructor for singleton pattern. Call {@link #getInstance()}
	 * to get an instance of this class.
	 */
	private SentenceFactory() {
		registerSentenceType("RMC", RMCSentence.class);
		registerSentenceType("GLL", GLLSentence.class);
		registerSentenceType("VTG", VTGSentence.class);
		registerSentenceType("VBW", VBWSentence.class);
		registerSentenceType("RSA", RSASentence.class);
		registerSentenceType("MWV", MWVSentence.class);
		registerSentenceType("MTW", MTWSentence.class);
		registerSentenceType("RPM", RPMSentence.class);
		registerSentenceType("HDT", HDTSentence.class);
		registerSentenceType("DPT", DPTSentence.class);
		registerSentenceType("GGA", GGASentence.class);
		registerSentenceType("HDG", HDGSentence.class);
		registerSentenceType("TTM", TTMSentence.class);
		registerSentenceType("VDM", AISSentence.class);
		registerSentenceType("VDO", AISSentence.class);
		registerSentenceType("OSD", OSDSentence.class);
		registerSentenceType("ASHR", ASHRSentence.class);
		registerSentenceType("EXT", EXTSentence.class);
		registerSentenceType("TLL", TLLSentence.class);
		registerSentenceType("ZDA", ZDASentence.class);
		registerSentenceType("ROT", ROTSentence.class);		
	}
	
	/**
	 * Parses a valid sentence and returns a concrete instance of
	 * {@link Sentence}. It does not validate the String, pass only valid
	 * Strings here, you can use {@link SentenceUtils#validateSentence(String)},
	 * before calling this.
	 * 
	 * @param nmea
	 *            The String to parse.
	 * @return A concrete instance of a {@link Sentence}
	 * @throws IllegalArgumentException
	 *             <ol>
	 *             <li>The given String could not be parsed, cause there is no parser registered for the sentenceId given by the String.</li>
	 *             <li>A given extended {@ling Sentence} could not be instantiated (error in the constructor).</li>
	 *             </ol>
	 */
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
	
	/**
	 * Checks whether a given sentenceId is already registered.
	 * 
	 * @param type
	 *            A sentenceId.
	 * @return true, if there is already a parser registered for the given
	 *         sentenceId.
	 */
	public boolean hasSentenceType(String type) {
		return sentenceTypes.containsKey(type);
	}
	
	/**
	 * Registers a new sentenceId with a given concrete class of
	 * {@link Sentence} as a parser.
	 * 
	 * @param type
	 *            SentenceId to register.
	 * @param sentenceType
	 *            Class of the extended {@link Sentence} as a parser.
	 * @throws IllegalArgumentException
	 *             <ol>
	 *             <li>The given sentenceId is already registered.</li>
	 *             <li>The given class does not have a constructor with a String.</li>
	 *             </ol>
	 */
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

	public Sentence createSentence(Map<String, Object> source) 
	{
		try 
		{
			Class<? extends Sentence> c = sentenceTypes.get(source.get("sentenceId"));
			Constructor<? extends Sentence> co = c.getConstructor(Map.class);
			return co.newInstance(source);
		} 
		catch (Exception e) 
		{
			throw new IllegalStateException("Unable to instanciate sentence.", e);
		}
	}
}
