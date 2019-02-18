package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public interface IStreamObjectDataHandler<T extends IStreamObject<? extends IMetaAttribute>> extends IDataHandler<T> {

	T readData(ByteBuffer buffer, boolean handleMetaData);

	T readData(InputStream inputStream, boolean handleMetaData) throws IOException;

	T readData(String input, boolean handleMetaData);

	T readData(Iterator<String> input, boolean handleMetaData);

	void writeData(ByteBuffer buffer, Object data, boolean handleMetaData);

	void writeData(ByteBuffer buffer, T object, boolean handleMetaData);

	void writeData(StringBuilder string, Object data, boolean handleMetaData);

	void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options);

	int memSize(Object attribute, boolean handleMetaData);

	boolean isHandleMetadata();

	void setMetaAttribute(IMetaAttribute metaAttribute);

	void writeCSVData(StringBuilder string, Object data, WriteOptions options);
	
}
