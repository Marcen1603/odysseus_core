package de.uniol.inf.is.odysseus.wrapper.dds.dds;

import java.util.HashMap;
import java.util.Map;

public class DDSDataReaderFactory {
	
	static final Map<String, IDDSDataReader<?>> dataReader = new HashMap<String, IDDSDataReader<?>>();
	static{
		dataReader.put("string", new DDSStringDataReader());
		dataReader.put("floatseq", new DDSFloatSeqDataReader());
		dataReader.put("long", new DDSLongDataReader());
		// TODO: Add further reader
	}
	
	public static IDDSDataReader<?> getReader(String type){
		return dataReader.get(type.toLowerCase());
	}

}
