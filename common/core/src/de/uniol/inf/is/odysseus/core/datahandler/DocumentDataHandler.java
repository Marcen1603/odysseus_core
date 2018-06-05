package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Document;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class DocumentDataHandler extends AbstractStreamObjectDataHandler<Document<?>> {

	StringHandler handler = new StringHandler();

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DocumentDataHandler.class);

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.DOCUMENT.getURI());
	}

	@Override
	public void setConversionOptions(ConversionOptions conversionOptions) {
		super.setConversionOptions(conversionOptions);
		if (this.handler != null) {
			this.handler.setConversionOptions(conversionOptions);
		}
	}


	@Override
	public Document<?> readData(ByteBuffer buffer, boolean handleMetaData) {
		return new Document<>(handler.readData(buffer));
	}

	@Override
	public Document<?> readData(Iterator<String> input, boolean handleMetaData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document<?> readData(String string, boolean handleMetaData) {
		return new Document<>(handler.readData(string));
	}

	@Override
	public Document<?> readData(InputStream inputStream, boolean handleMetaData) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuffer out = new StringBuffer();
		
		while ((line = in.readLine()) != null) {
			out.append(line+System.lineSeparator());
		}
		
		// remove last lineSeperator
				
		return new Document<>(out.substring(0,out.length()-1));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeData(ByteBuffer buffer, Object data, boolean handleMetaData) {
		handler.writeData(buffer, ((Document<IMetaAttribute>) data).getContent());
	}

	@SuppressWarnings("unchecked")

	@Override
	public void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options) {
		handler.writeData(output, ((Document<IMetaAttribute>) data).getContent(), options);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int memSize(Object attribute, boolean handleMetaData) {
		return handler.memSize(((Document<IMetaAttribute>) attribute).getContent());
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

	@Override
	public void writeData(ByteBuffer buffer, Document<?> object, boolean handleMetaData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeData(StringBuilder string, Object data, boolean handleMetaData) {
		// TODO Auto-generated method stub

	}

}
