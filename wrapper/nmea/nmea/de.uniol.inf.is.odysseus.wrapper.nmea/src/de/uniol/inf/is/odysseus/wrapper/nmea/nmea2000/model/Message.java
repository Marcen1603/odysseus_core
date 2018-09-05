package de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.model;

import java.io.IOException;
import java.util.BitSet;
import java.util.Map;

public class Message {

	public Field[] fields;
	public String name;
	public int pgn;
	public int byteLength;
	
	public void parse(Map<String, Object> map,
			byte[] payload) throws IOException {
		BitSet bits = BitSet.valueOf(payload); 
		
		for(Field field : fields) {
			field.parse(map, payload, bits);
		}
	}
	
}
