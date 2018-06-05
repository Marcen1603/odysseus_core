package de.uniol.inf.is.odysseus.wrapper.ncsa_hdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.ScalarDS;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractFileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class NcsaHDFTransportHandler extends AbstractFileHandler implements
		IIteratable<Tuple<IMetaAttribute>> {

	public static Logger LOG = LoggerFactory
			.getLogger(NcsaHDFTransportHandler.class);

	public static final String NAME = "NcsaHDFFile";
	public static final String PATH = "path";

	private String path;
	private List<Tuple<IMetaAttribute>> read = new LinkedList<>();

	public NcsaHDFTransportHandler() {
		try {
			System.loadLibrary("jhdf");
			System.loadLibrary("jhdf5");
		} catch (Exception e) {
			LOG.error("Error loading libraries ", e);
		}
	}

	public NcsaHDFTransportHandler(IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		super(protocolHandler, options);
		if (options.containsKey(PATH)) {
			path = options.get(PATH);
		} else {
			throw new IllegalArgumentException("No path given!");
		}
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new IllegalArgumentException(
				"This handler cannot be used for writing!");
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new NcsaHDFTransportHandler(protocolHandler, options);
	}

	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException(
				"This handler cannot be used for writing!");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		H5File f = new H5File(filename, FileFormat.READ);
		// Read metadata ... currently not supported
		//		HObject root;
//		List meta;
//		String start = "";
//		String end = "";
//		try {
//			f.open();
//			root = f.get("/");
//			meta = root.getMetadata();
//			for (Object o:root.getMetadata()){
//				System.out.println(o+" "+o.getClass());
//				ncsa.hdf.object.Attribute a = (ncsa.hdf.object.Attribute) o;
//				if ("start".equalsIgnoreCase(a.getName())){
//					start = ((String[]) a.getValue())[0];
//				}
//								
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		HObject out;
		String[] pathes = path.split(";");
		for (int p = 0; p < pathes.length; p++) {
			try {
				out = f.get(pathes[p]);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException(e);
			}
			if (out instanceof ScalarDS) {
				ScalarDS v = (ScalarDS) out;
				try {
					Object data = v.getData();
					readValues(pathes.length, v.getDatatype(), p, data);
				} catch (OutOfMemoryError | Exception e) {
					throw new IOException(e);
				}
			} else if (out instanceof H5CompoundDS) {
				H5CompoundDS compound = (H5CompoundDS) out;
					try {
						@SuppressWarnings("rawtypes")
						Vector data = (Vector) compound.getData();
						Datatype[] types = compound.getMemberTypes();
						for (int i = 0; i < data.size(); i++) {
							Object input = data.get(i);
							readValues(data.size(), types[i], i, input);
						}
					} catch (OutOfMemoryError | Exception e) {
						e.printStackTrace();
					}
			} else {
				throw new IllegalArgumentException(
						"Unsupported path destination " + path);
			}
		}
	}

	private void readValues(int size, Datatype type, int i, Object input) {
		switch(type.getDatatypeClass()){
		case Datatype.CLASS_REFERENCE:
			long[] long_values = (long[]) input;
			readValues(size,i,long_values);
			break;
		case Datatype.CLASS_STRING:
			String[] str_values = (String[]) input;
			readValues(size,i,str_values);							
			break;
		case Datatype.CLASS_INTEGER:
			int[] int_values = (int[]) input;
			readValues(size,i,int_values);
			break;
		case Datatype.CLASS_FLOAT:
			float[] float_values = (float[]) input;
			readValues(size,i,float_values);
			break;
		}
	}

	// ----
	// Grml ... is there a better way than to copy the method ...

	private void readValues(int tupleSize, int p, float[] values) {
		for (int i = 0; i < values.length; i++) {
			Tuple<IMetaAttribute> t;
			if (p == 0) {
				t = new Tuple<>(tupleSize + 1, false);
				t.setAttribute(0, i);
				t.setAttribute(1, values[i]);
				read.add(t);
			} else {
				t = read.get(i);
				t.setAttribute(p + 1,values[i]);
			}
		}
	}

	// DO NOT MODIFY CONTENT. COPY FROM ABOVE!!
	private void readValues(int tupleSize, int p, int[] values) {
		for (int i = 0; i < values.length; i++) {
			Tuple<IMetaAttribute> t;
			if (p == 0) {
				t = new Tuple<>(tupleSize + 1, false);
				t.setAttribute(0, i);
				t.setAttribute(1, values[i]);
				read.add(t);
			} else {
				t = read.get(i);
				t.setAttribute(p + 1,values[i]);
			}
		}
	}

	private void readValues(int tupleSize, int p, long[] values) {
		for (int i = 0; i < values.length; i++) {
			Tuple<IMetaAttribute> t;
			if (p == 0) {
				t = new Tuple<>(tupleSize + 1, false);
				t.setAttribute(0, i);
				t.setAttribute(1, values[i]);
				read.add(t);
			} else {
				t = read.get(i);
				t.setAttribute(p + 1,values[i]);
			}
		}
	}
	
	private void readValues(int tupleSize, int p, String[] values) {
		for (int i = 0; i < values.length; i++) {
			Tuple<IMetaAttribute> t;
			if (p == 0) {
				t = new Tuple<>(tupleSize + 1, false);
				t.setAttribute(0, i);
				t.setAttribute(1, values[i]);
				read.add(t);
			} else {
				t = read.get(i);
				t.setAttribute(p + 1,values[i]);
			}
		}
	}
	
	@Override
	public void processInClose() throws IOException {
		read.clear();
	}

	@Override
	public boolean hasNext() {
		return read.size() > 0;
	}

	@Override
	public Tuple<IMetaAttribute> getNext() {
		return read.remove(0);
	}

	@Override
	public boolean isDone() {
		return read.size() == 0;
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new IllegalArgumentException(
				"This handler cannot be used for writing!");
	}

	@Override
	public void processOutClose() throws IOException {
		// ignore
		//		throw new IllegalArgumentException(
		//				"This handler cannot be used for writing!");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
