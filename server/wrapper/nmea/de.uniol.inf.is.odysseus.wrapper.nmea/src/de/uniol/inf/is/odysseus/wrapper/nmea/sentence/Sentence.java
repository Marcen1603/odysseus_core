package de.uniol.inf.is.odysseus.wrapper.nmea.sentence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.nmea.util.SentenceUtils;

public abstract class Sentence {
	public static final char ALTERNATIVE_BEGIN_CHAR = '!';
	public static final char BEGIN_CHAR = '$';
	public static final char CHECKSUM_DELIMITER = '*';
	public static final char FIELD_DELIMITER = ',';
	public static final int MAX_LENGTH = 82;
	public static final String TERMINATOR = "\r\n";
	
	protected char beginChar;
	protected String talkerId;
	protected String sentenceId;
	protected List<String> fields;
	protected String nmea;
	
	public Sentence(String nmea) {
		this.nmea = nmea;
		beginChar = nmea.charAt(0);
		talkerId = SentenceUtils.getTalkerId(nmea);
		sentenceId = SentenceUtils.getSentenceId(nmea);
	}
	
	public Sentence(char beginChar, String talkerId, String sentenceId, int fieldCount) {
		this.beginChar = beginChar;
		this.talkerId = talkerId;
		this.sentenceId = sentenceId;
		this.fields = new ArrayList<String>(fieldCount);
		for (int i = 0; i < fieldCount; i++) {
			this.fields.add("");
		}
	}
	
	public void parse() {
		int begin = nmea.indexOf(Sentence.FIELD_DELIMITER);
		String temp = nmea.substring(begin + 1);

		// remove checksum
		if (temp.contains(String.valueOf(CHECKSUM_DELIMITER))) {
			int end = temp.indexOf(CHECKSUM_DELIMITER);
			temp = temp.substring(0, end);
		}

		String[] temp2 = temp.split(String.valueOf(FIELD_DELIMITER), -1);
		fields = new ArrayList<String>(temp2.length);
		for (String s : temp2) {
			fields.add(s);
		}
		decode();
	}
	
	public final int getFieldCount() {
		if (fields == null) {
			return 0;
		}
		return fields.size();
	}
	
	protected String getValue(int index) {
		if (fields.size() <= index) {
			return null;
		}
		try {
			String value = fields.get(index);
			if (value == null || "".equals(value)) {
				return null;
			}
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	
	protected void setValue(int index, String value) {
		while (fields.size() <= index) {
			fields.add("");
		}
		fields.set(index, value);
	}
	
	@Override
	public String toString() {
		return toNMEA();
	}
	
	public final String toNMEA() {
		encode();
		StringBuilder sb = new StringBuilder(MAX_LENGTH);
		sb.append(beginChar);
		sb.append(talkerId.toString());
		sb.append(sentenceId);

		for (String field : fields) {
			sb.append(FIELD_DELIMITER);
			sb.append(field == null ? "" : field);
		}

		String res;
		if (sb.length() > MAX_LENGTH - 2) {
			res = sb.substring(0, MAX_LENGTH - 2);
		} else {
			res = sb.toString();
		}
		String sum = SentenceUtils.calculateChecksum(res);
		return res + CHECKSUM_DELIMITER + sum;
	}
	
	public String getTalkerId() {
		return talkerId;
	}
	
	public String getSentenceId() {
		return sentenceId;
	}
	
	protected abstract void decode();
	
	protected abstract void encode();
	
	public abstract void fillMap(Map<String, Object> result);
}
