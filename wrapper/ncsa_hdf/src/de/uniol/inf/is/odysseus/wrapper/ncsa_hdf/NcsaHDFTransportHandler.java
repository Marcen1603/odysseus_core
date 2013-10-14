package de.uniol.inf.is.odysseus.wrapper.ncsa_hdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.ScalarDS;
import ncsa.hdf.object.h5.H5File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProvidesStringArray;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractFileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class NcsaHDFTransportHandler extends AbstractFileHandler implements
		IProvidesStringArray {

	public static Logger LOG = LoggerFactory
			.getLogger(NcsaHDFTransportHandler.class);

	public static final String NAME = "NcsaHDFFile";
	public static final String PATH = "path";

	private String path;
	private List<String[]> read = new LinkedList<>();

	public NcsaHDFTransportHandler() {
		try {
			System.loadLibrary("jhdf");
			System.loadLibrary("jhdf5");
		} catch (Exception e) {
			LOG.error("Error loading libraries ", e);
		}
	}

	public NcsaHDFTransportHandler(IProtocolHandler<?> protocolHandler,
			Map<String, String> options) {
		super(protocolHandler);
		setOptionsMap(options);
	}

	@Override
	public void setOptionsMap(Map<String, String> options) {
		super.setOptionsMap(options);
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
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
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
		HObject out;
		String[] pathes = path.split(";");
		for (int p = 0; p< pathes.length; p++) {
			try {
				out = f.get(pathes[p]);
			} catch (Exception e) {
				throw new IOException(e);
			}
			if (out instanceof ScalarDS) {
				ScalarDS v = (ScalarDS) out;
				try {
					Object data = v.getData();
					switch (v.getDatatype().getDatatypeClass()) {
					case Datatype.CLASS_FLOAT:
						float[] float_values = (float[]) data;
						readValues(pathes.length, p, float_values);
						break;
					case Datatype.CLASS_INTEGER:
						int[] int_values = (int[]) data;
						readValues(pathes.length, p, int_values);
						break;	
					}
				} catch (OutOfMemoryError | Exception e) {
					throw new IOException(e);
				}
			} else {
				throw new IllegalArgumentException(
						"Unsupported path destination " + path);
			}
		}
	}
	
	// ----
	// Grml ... is there a better way than to copy the method ...
	
	private void readValues(int pathesLength, int p, float[] values) {
		for (int i = 0; i < values.length; i++) {
			String[] t;
			if (p==0) {
				t = new String[pathesLength + 1];
				t[0] = "" + i;
				t[1] = "" + values[i];
			} else {
				t = read.get(i);
				t[p+1] = ""+values[i]; 
			}
			read.add(t);
		}
	}

	// DO NOT MODIFY CONTENT. COPY FROM ABOVE!!
	private void readValues(int pathesLength, int p, int[] values) {
		for (int i = 0; i < values.length; i++) {
			String[] t;
			if (p==0) {
				t = new String[pathesLength + 1];
				t[0] = "" + i;
				t[1] = "" + values[i];
			} else {
				t = read.get(i);
				t[p+1] = ""+values[i]; 
			}
			read.add(t);
		}
	}

	
	@Override
	public boolean hasNext() {
		return read.size() > 0;
	}

	@Override
	public String[] getNext() {
		return read.remove(0);
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new IllegalArgumentException(
				"This handler cannot be used for writing!");
	}

	@Override
	public void processOutClose() throws IOException {
		throw new IllegalArgumentException(
				"This handler cannot be used for writing!");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

}
