package handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.StringHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rdf.datamodel.Triple;

public class TripleDataHandler extends AbstractStreamObjectDataHandler<Triple<? extends IMetaAttribute>> {

	static protected List<String> types = new ArrayList<String>();

	static {
		types.add(SDFDatatype.TRIPLE.getURI());
	}

	final List<StringHandler> dataHandlers = new ArrayList<>();
	
	public TripleDataHandler() {
		for (int i=0;i<3;i++) {
			dataHandlers.add(new StringHandler());
		}
	}
	
	@Override
	public void setConversionOptions(ConversionOptions conversionOptions) {
		super.setConversionOptions(conversionOptions);
		if (dataHandlers != null) {
			for (IDataHandler<?> handler : dataHandlers) {
				handler.setConversionOptions(conversionOptions);
			}
		}
	}

	@Override
	public Triple<? extends IMetaAttribute> readData(ByteBuffer buffer, boolean handleMetaData) {
		String[] res = new String[3];
		for (int i = 0; i < 3; i++) {
			res[i] = dataHandlers.get(i).readData(buffer);
		}

		Triple<IMetaAttribute> t = new Triple<IMetaAttribute>(res[0], res[1], res[2]);

		if (handleMetaData) {
			IMetaAttribute meta = readMetaData(buffer);
			t.setMetadata(meta);
		}

		return t;
	}

	@Override
	public Triple<? extends IMetaAttribute> readData(InputStream inputStream, boolean handleMetaData)
			throws IOException {
		String[] res = new String[3];
		for (int i = 0; i < 3; i++) {
			res[i] = dataHandlers.get(i).readData(inputStream);
		}

		Triple<IMetaAttribute> t = new Triple<IMetaAttribute>(res[0], res[1], res[2]);

		if (handleMetaData) {
			IMetaAttribute meta = readMetaData(inputStream);
			t.setMetadata(meta);
		}

		return t;
	}

	@Override
	public Triple<? extends IMetaAttribute> readData(String input, boolean handleMetaData) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Triple<? extends IMetaAttribute> readData(Iterator<String> input, boolean handleMetaData) {
		String[] res = new String[3];
		for (int i = 0; i < 3; i++) {
			res[i] = dataHandlers.get(i).readData(input.next());
		}

		Triple<IMetaAttribute> t = new Triple<IMetaAttribute>(res[0], res[1], res[2]);

		if (handleMetaData) {
			IMetaAttribute meta = readMetaData(input);
			t.setMetadata(meta);
		}

		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void writeData(ByteBuffer buffer, Object data, boolean handleMetaData) {
		writeData(buffer, (Triple<? extends IMetaAttribute>) data, handleMetaData);

	}

	@Override
	public void writeData(ByteBuffer buffer, Triple<? extends IMetaAttribute> object, boolean handleMetaData) {
		Triple<? extends IMetaAttribute> triple = object;
		for (int i = 0; i < 3; i++) {
			dataHandlers.get(i).writeData(buffer, triple.get(i));
		}
		if (handleMetaData) {
			writeMetaData(buffer, triple.getMetadata());
		}
	}

	@Override
	public void writeData(StringBuilder string, Object data, boolean handleMetaData) {
		@SuppressWarnings("unchecked")
		Triple<? extends IMetaAttribute> triple = (Triple<? extends IMetaAttribute>) data;
		for (int i = 0; i < 3; i++) {
			dataHandlers.get(i).writeData(string, triple.get(i));
		}
		if (handleMetaData) {
			writeMetaData(string, triple.getMetadata());
		}
	}

	@Override
	public void writeData(List<String> output, Object data, boolean handleMetaData, WriteOptions options) {
		@SuppressWarnings("unchecked")
		Triple<? extends IMetaAttribute> triple = (Triple<? extends IMetaAttribute>) data;
		for (int i = 0; i < 3; i++) {
			dataHandlers.get(i).writeData(output, triple.get(i), options);
		}
		if (handleMetaData) {
			writeMetaData(output, triple.getMetadata(), options);
		}
	}

	@Override
	public int memSize(Object attribute, boolean handleMetaData) {
		@SuppressWarnings("unchecked")
		Triple<? extends IMetaAttribute> triple = (Triple<? extends IMetaAttribute>) attribute;
		int size = 0;
		for (int i = 0; i < dataHandlers.size(); i++) {
			size += dataHandlers.get(i).memSize(triple.get(i));
		}
		if (handleMetaData) {
			size += getMetaDataMemSize(triple.getMetadata());
		}
		return size;
	}

	@Override
	public Class<?> createsType() {
		return Triple.class;
	}

	@Override
	protected IDataHandler<Triple<? extends IMetaAttribute>> getInstance(SDFSchema schema) {
		return new TripleDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
