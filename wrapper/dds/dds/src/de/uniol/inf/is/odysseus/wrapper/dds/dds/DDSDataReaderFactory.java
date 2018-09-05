package de.uniol.inf.is.odysseus.wrapper.dds.dds;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.wrapper.dds.dds.kind.AbstractDDSKindDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.kind.DDSStringDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.kind.DDSWStringDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.seq.AbstractDDSSequenceDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.seq.DDSFloatSeqDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.AbstractDDSSimpleTypeDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSLongDataReader;

public class DDSDataReaderFactory {
	
	static final Map<String, IDDSDataReader<?>> dataReaders = new HashMap<String, IDDSDataReader<?>>();
	static final Map<String, IDDSDataReader<?>> sequenceDataReaders = new HashMap<String, IDDSDataReader<?>>();
	static final Map<String, IDDSDataReader<?>> kindDataReaders = new HashMap<String, IDDSDataReader<?>>();

	static{
		add(new DDSStringDataReader());
		add(new DDSWStringDataReader());
		add(new DDSFloatSeqDataReader());
		add(new DDSLongDataReader());
		// TODO: Add further reader
	}
	
	public static IDDSDataReader<?> getReader(String type){
		return dataReaders.get(type.toLowerCase());
	}


	private static void add(AbstractDDSSimpleTypeDataReader<?> dataReader) {
		dataReaders.put(dataReader.getTypeCode().get_type_as_string().toLowerCase(), dataReader);
	}
	
	private static void add(AbstractDDSSequenceDataReader<?> dataReader) {
		sequenceDataReaders.put(dataReader.getSubTypeCode().get_type_as_string().toLowerCase(), dataReader);
	}

	public static IDDSDataReader<?> getSequenceReader(String type){
		return sequenceDataReaders.get(type.toLowerCase());
	}
	
	private static void add(AbstractDDSKindDataReader<?> dataReader) {
		kindDataReaders.put(dataReader.getKind().name().toLowerCase(), dataReader);
	}
	
	public static IDDSDataReader<?> getKindReader(String type){
		return kindDataReaders.get(type.toLowerCase());
	}
	
	
	
	
	


}
