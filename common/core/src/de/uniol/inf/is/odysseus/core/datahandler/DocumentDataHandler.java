package de.uniol.inf.is.odysseus.core.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Document;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class DocumentDataHandler extends AbstractDataHandler<Document<?>> {

	StringHandler stringHandler = new StringHandler();
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DocumentDataHandler.class);
	
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.DOCUMENT.getURI());
	}

	@Override
	public Document<?> readData(ByteBuffer buffer) {
		return new Document<>(stringHandler.readData(buffer));
	}

	@Override
	public Document<?> readData(String string) {
		return new Document<>(stringHandler.readData(string));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		stringHandler.writeData(buffer, ((Document<IMetaAttribute>)data).getContent());
	}
	
	@SuppressWarnings("unchecked")

	@Override
	public void writeData(List<String> output, Object data) {
		stringHandler.writeData(output, ((Document<IMetaAttribute>)data).getContent());
	}

	@SuppressWarnings("unchecked")
	@Override
	public int memSize(Object attribute) {
		return stringHandler.memSize(((Document<IMetaAttribute>)attribute).getContent());
	}

	@Override
	protected IDataHandler<Document<?>> getInstance(SDFSchema schema) {
		return new DocumentDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public Class<?> createsType() {
		return Document.class;
	}
	
}
