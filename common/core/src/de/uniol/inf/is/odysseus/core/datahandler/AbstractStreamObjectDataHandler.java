package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class AbstractStreamObjectDataHandler<T extends IStreamObject<? extends IMetaAttribute>>
		extends AbstractDataHandler<T>implements IStreamObjectDataHandler<T> {

	protected IMetaAttribute metaAttribute;
	private boolean handleMetadata = false;
	private List<NullAwareTupleDataHandler> metaDataHandler;

	protected IDataHandler<T> IStreamObjectDataHandler(List<SDFDatatype> schema) {
		// Hint: Currently only needed for TupleDataHandler
		return null;
	}

	public AbstractStreamObjectDataHandler() {
		super();
	}

	public AbstractStreamObjectDataHandler(SDFSchema schema) {
		super(schema);
	}

	// ------------------------------------------------------------------------------------------
	// Meta data handling
	// ------------------------------------------------------------------------------------------

	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		this.metaAttribute = metaAttribute;
		this.metaDataHandler = new ArrayList<>();
		for (SDFSchema schema : metaAttribute.getSchema()) {
			this.metaDataHandler.add((NullAwareTupleDataHandler) new NullAwareTupleDataHandler().getInstance(schema));
		}
		this.handleMetadata = true;
	};

	@Override
	public boolean isHandleMetadata() {
		return handleMetadata;
	}

	public boolean hasMetadata(){
		return metaDataHandler != null;
	}

	protected final IMetaAttribute readMetaData(InputStream inputStream) throws IOException {
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			res.add(dh.readData(inputStream));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected void readAndAddMetadata(InputStream inputStream, boolean handleMetaData, IStreamObject<IMetaAttribute> ret)
			throws IOException {
		if (handleMetaData && hasMetadata()) {
			IMetaAttribute meta = readMetaData(inputStream);
			ret.setMetadata(meta);
		}
	}



	protected final IMetaAttribute readMetaData(Iterator<String> input) {
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected void readAndAddMetadata(Iterator<String> input, boolean handleMetaData, IStreamObject<IMetaAttribute> tuple) {
		if (handleMetaData && hasMetadata()) {
			IMetaAttribute meta = readMetaData(input);
			tuple.setMetadata(meta);
		}
	}


	protected final IMetaAttribute readMetaData(ByteBuffer input) {
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected void readAndAddMetadata(ByteBuffer input, boolean handleMetaData, IStreamObject<IMetaAttribute> tuple) {
		if (handleMetaData && hasMetadata()) {
			IMetaAttribute meta = readMetaData(input);
			tuple.setMetadata(meta);
		}
	}


	@Override
	public T readData(String input, boolean handleMetaData) {
		List<String> list = new ArrayList<String>();
		list.add(input);
		return readData(list.iterator(), handleMetaData);
	}

	protected final void writeMetaData(List<String> output, IMetaAttribute metaAttribute, WriteOptions options) {
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			dh.writeData(output, v.get(i++), options);
		}
	}

	protected final void writeMetaData(StringBuilder output, IMetaAttribute metaAttribute) {
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			dh.writeData(output, v.get(i++));
		}
	}

	protected final void writeMetaData(ByteBuffer output, IMetaAttribute metaAttribute) {
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			dh.writeData(output, v.get(i++));
		}
	}

	protected final long getMetaDataMemSize(IMetaAttribute metaAttribute) {
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		long size = 0;
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			size += dh.memSize(v.get(i++));
		}
		return size;
	}

	// --------------------------------------------------------------------------------------------------
	// Default implementation for cases where no meta data should be treated
	// (e.g. if a tuple is part of
	// another tuple)
	// --------------------------------------------------------------------------------------------------

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		writeData(buffer, data, handleMetadata);
	}

	@Override
	public void writeData(List<String> output, Object data, WriteOptions options) {
		writeData(output, data, handleMetadata, options);
	}

	@Override
	public void writeData(StringBuilder string, Object data) {
		writeData(string, data, handleMetadata);
	}

	@Override
	public T readData(ByteBuffer buffer) {
		return readData(buffer, handleMetadata);
	}

	@Override
	public T readData(String input) {
		return readData(input, handleMetadata);
	}

	@Override
	public T readData(Iterator<String> input) {
		return readData(input, handleMetadata);
	}

	@Override
	public T readData(InputStream inputStream) throws IOException {
		return readData(inputStream, handleMetadata);
	}

	@Override
	public int memSize(Object attribute) {
		return memSize(attribute, handleMetadata);
	}

	/**
	 * Default implementation of CVS, BSON, JSON
	 */

	@Override
	public void writeCSVData(StringBuilder string, Object data, WriteOptions options) {
		List<String> values = new ArrayList<>();

		writeData(values, data, options);

		for (int i=0;i<values.size();i++){
			string.append(values.get(i));
			if (i<values.size()-1){
				string.append(options.getDelimiter());
			}
		}
	}
	

}
