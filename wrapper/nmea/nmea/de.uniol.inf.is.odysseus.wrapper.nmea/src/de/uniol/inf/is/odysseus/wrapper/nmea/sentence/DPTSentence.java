package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.util.ParseUtils;

/**
 * DPT - Depth of Water<br>
 * <br>
 * 
 * <pre>
 * {@code
 * .      1   2   3
 *        |   |   |
 * $--DPT,x.x,x.x*hh
 * }
 * </pre>
 * <ol>
 * <li>Depth, meters</li>
 * <li>Offset from transducer
 * 	   positive means distance from transducer to water line,
 * 	   negative means distance from transducer to keel</li>
 * <li>Checksum</li>
 * </ol>
 * 
 * @author aoppermann <alexander.oppermann@offis.de>
 *
 */

public class DPTSentence extends Sentence{
	/** Default begin char for this sentence type. */
	public static final char BEGIN_CHAR = '$';
	/** Default talker for this sentence. */
	public static final String DEFAULT_TALKER = "IN";
	/** Sentence id. */
	public static final String SENTENCE_ID = "DPT";
	/** Default count of fields. */
	public static final int FIELD_COUNT = 2;
	
	/** Depth, meters. */
	private Double depth;
	/** Offset from transducer;
	 *  positive means distance from transducer to water line,
	 *  negative means distance from transducer to keel. */
	private Double offset;
	
	/**
	 * Default constructor for writing. Empty Sentence to fill attributes and
	 * call {@link #toNMEA()}.
	 */
	public DPTSentence() {
		super(BEGIN_CHAR, DEFAULT_TALKER, SENTENCE_ID, FIELD_COUNT);
	}

	/**
	 * Default constructor for parsing.
	 * 
	 * @param nmea
	 *            Nmea String to be parsed.
	 */
	public DPTSentence(String nmea) {
		super(nmea);
	}
	
	/**
	 * Constructor for creating a message from a map. Reverse function of fillMap()
	 * 
	 * @param source
	 *            Map containing specific keys.
	 */		
	public DPTSentence(Map<String, Object> source)	
	{
		super(source, FIELD_COUNT);
		
		if (source.containsKey("depth"))  depth  = (double) source.get("depth");
		if (source.containsKey("offset")) offset = (double) source.get("offset");
	}				
	
	@Override
	protected void decode() {
		int index = 0;
		depth = ParseUtils.parseDouble(getValue(index++));
		offset = ParseUtils.parseDouble(getValue(index++));
	}

	@Override
	protected void encode() {
		int index = 0;
		setValue(index++, ParseUtils.toString(depth));
		setValue(index++, ParseUtils.toString(offset));
	}

	@Override
	protected void fillMap(Map<String, Object> res) {
		if (depth != null) res.put("depth", depth);
		if (offset != null) res.put("offset", offset);
	}
	
	public Double getDepth(){
		return depth;
	}
	
	public void setDepth(Double depth){
		this.depth = depth;
	}
	
	public Double getOffset(){
		return offset;
	}
	
	public void setOffset(Double offset){
		this.offset = offset;
	}
}