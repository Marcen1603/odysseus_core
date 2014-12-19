package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.DecodedAISPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * VDM/VDO - AIS Message (VDO = Own ship)<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1 2 3 4 5    6 7
 *        | | | | |    | |
 * $--VDM,x,x,x,a,c--c,x*hh
 * }
 * </pre>
 * <ol>
 * <li>Count of fragments</li>
 * <li>Fragment number</li>
 * <li>Sequential message id (only in multi fragments)</li>
 * <li>Channel (A = 161.975 Mhz, B = 162.025 Mhz)</li>
 * <li>AIS message</li>
 * <li>Fill bits</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class AISSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '!';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "AI";
	/** Sentence id. */
	public static final String SENTENCE_ID = "VDM";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 6;
	
	/** Count of fragments */
	private Integer fragmentsCount;
	/** Fragment number */
	private Integer fragmentId;
	/** Sequential message id (only in multi fragments) */
	private Integer messageId;
	/** Channel (A = 161.975 Mhz, B = 162.025 Mhz) */
	private String channel;
	/** AIS message */
	private String message;
	/** Fill bits */
	private Integer fillBits;
	
	/**The decoded message, this will be set by the AISSentenceHandler:
	 * 1- If the sentence is not fragmented, the AISSentenceHandler will set it after handling the sentence directly
	 * 2- If the sentence is fragmented, the AISSentenceHandler will set this member only if this sentence represents the last fragment. */
	private DecodedAISPayload decodedPayload;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public AISSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public AISSentence(String nmea) {
		super(nmea);
	}
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public AISSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("fragmentsCount")) fragmentsCount = (int) source.get("fragmentsCount");
		if (source.containsKey("fragmentId")) fragmentId = (int) source.get("fragmentId");
		if (source.containsKey("messageId")) messageId = (int) source.get("messageId");
		if (source.containsKey("channel")) channel = (String) source.get("channel");
		if (source.containsKey("message")) message = (String) source.get("message");
		if (source.containsKey("fillBits")) fillBits = (int) source.get("fillBits");
	}		
	
	
	@Override
	protected void decode() {
		int index = 0;
		fragmentsCount = ParseUtils.parseInteger(getValue(index++));
		fragmentId = ParseUtils.parseInteger(getValue(index++));
		messageId = ParseUtils.parseInteger(getValue(index++));
		channel = getValue(index++);
		message = getValue(index++);
		fillBits = ParseUtils.parseInteger(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(fragmentsCount));
		setValue(index++, ParseUtils.toString(fragmentId));
		setValue(index++, ParseUtils.toString(messageId));
		setValue(index++, channel);
		setValue(index++, message);
		setValue(index++, ParseUtils.toString(fillBits));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
//		//Fill the map with the decoded message parts
//		if (decodedPayload != null)
//			decodedPayload.fillMap(res);
//		//Fill the map with the original message parts
//		else{
			if (fragmentsCount != null) res.put("fragmentsCount", fragmentsCount);
			if (fragmentId != null) res.put("fragmentId", fragmentId);
			if (messageId != null) res.put("messageId", messageId);
			if (channel != null) res.put("channel", channel);
			if (message != null) res.put("message", message);
			if (fillBits != null) res.put("fillBits", fillBits);
//		}
	}
	
	public void toDecodedPayloadMap(Map<String, Object> res) {
		//Fill the map with the decoded message parts
		if (decodedPayload != null)
			decodedPayload.fillMap(res);
	}

	public Integer getFragmentsCount() {
		return fragmentsCount;
	}

	public void setFragmentsCount(Integer fragmentsCount) {
		this.fragmentsCount = fragmentsCount;
	}

	public Integer getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(Integer fragmentId) {
		this.fragmentId = fragmentId;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getFillBits() {
		return fillBits;
	}

	public void setFillBits(Integer fillBits) {
		this.fillBits = fillBits;
	}

	public DecodedAISPayload getDecodedPayload() {
		return decodedPayload;
	}

	public void setDecodedPayload(DecodedAISPayload decodedPayload) {
		this.decodedPayload = decodedPayload;
	}
}
