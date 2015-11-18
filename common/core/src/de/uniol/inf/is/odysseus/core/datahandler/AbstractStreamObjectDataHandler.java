package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ICSVToString;
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

	protected final IMetaAttribute readMetaData(InputStream inputStream) throws IOException {
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			res.add(dh.readData(inputStream));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected final IMetaAttribute readMetaData(String[] input) {
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
	}

	protected final IMetaAttribute readMetaData(List<String> input) {
		List<Tuple<?>> res = new ArrayList<Tuple<?>>();
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			res.add(dh.readData(input));
		}
		IMetaAttribute newMeta = metaAttribute.clone();
		newMeta.writeValues(res);
		return newMeta;
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

	protected final void writeMetaData(List<String> output, IMetaAttribute metaAttribute) {
		List<Tuple<?>> v = new ArrayList<Tuple<?>>();
		metaAttribute.retrieveValues(v);
		int i = 0;
		for (NullAwareTupleDataHandler dh : metaDataHandler) {
			dh.writeData(output, v.get(i++));
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
	public void writeData(List<String> output, Object data) {
		writeData(output, data, handleMetadata);
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
	public T readData(List<String> input) {
		return readData(input, handleMetadata);
	}

	@Override
	public T readData(InputStream inputStream) throws IOException {
		return readData(inputStream, handleMetadata);
	}

	@Override
	public T readData(String[] input) {
		return readData(input, handleMetadata);
	}

	@Override
	public int memSize(Object attribute) {
		return memSize(attribute, handleMetadata);
	}

	/**
	 * Default implemtation of CVS, BSON, JSON
	 */

	@Override
	public byte[] writeBSONData(Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeJSONData(StringBuilder string, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeCSVData(StringBuilder string, Object data, WriteOptions options) {
		if (data instanceof ICSVToString) {
			string.append(((ICSVToString) data).csvToString(options));
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
