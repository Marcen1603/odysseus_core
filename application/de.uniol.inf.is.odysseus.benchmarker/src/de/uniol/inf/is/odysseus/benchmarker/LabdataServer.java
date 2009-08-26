package de.uniol.inf.is.odysseus.benchmarker;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.Ostermiller.util.CSVParser;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.interval_latency_priority.IntervalLatencyPriorityFactory;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Simple server for sending <a
 * href="http://db.csail.mit.edu/labdata/labdata.html">Intel lab data</a>
 * <code>RelationalTuple</code> elements over a socket. It manages only one
 * connection at a time. The server can be configured by a properties file which
 * can be set via command line parameter (otherwise it defaults to
 * labdataserver.ini). The configuration contains the following settings:
 * <ul>
 * <li>port - the port the server listens on (default = 55555)</li>
 * <li>inputfile - a file containing serialised lab data
 * <code>RelationalTuple</code> elements. These elements get send by the server
 * in the same order as in the file. (default = labdata)</li>
 * <li>acceleration - the elements get send in the same chronology as the
 * original data (one measurement per sensor per 3 minutes), the acceleration
 * factor accelerates the sending by the given factor (default = 1)</li>
 * <li>limit - limits the send tuples to the first limit elements. Those
 * elements get pre-cached by the server, so sending can be as fast as possible.
 * A limit less than 1 will result in no limit. (default = -1)</li>
 * </ul>
 * You can generate the serialised data out of the original .txt file with the
 * LabdataToRelationalTuple program.
 * 
 * @see LabdataToRelationalTuple
 * @author Jonas Jacobi
 */
public class LabdataServer {
	private static final String DEFAULT_PROPERTIES_FILE = "labdataserver.ini";
	private static final String DEFAULT_PORT = "55555";
	@SuppressWarnings("unchecked")
	private static Object[][] cachedValues = null;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		String filename = DEFAULT_PROPERTIES_FILE;
		if (args.length > 1) {
			System.err.println("usage: LabdataServer [filename]");
			return;
		}
		if (args.length == 1) {
			filename = args[0];
		}
		Properties cfg = new Properties();
		try {
			cfg.load(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			if (args.length == 1) {
				throw e;
			}
		} catch (IOException e) {
			throw e;
		}
		String inputFile = cfg.getProperty("inputfile", "labdata");
		Integer limit = Integer.parseInt(cfg.getProperty("limit", "-1"));
		Integer accelerationFactor = Integer.parseInt(cfg.getProperty(
				"acceleration", "1"));
		Integer port = Integer.parseInt(cfg.getProperty("port", DEFAULT_PORT));
		boolean useRaw = Boolean.parseBoolean(cfg
				.getProperty("useRaw", "true"));
		boolean useCSV = Boolean.parseBoolean(cfg.getProperty("useCSV", "false"));
		char delim = cfg.getProperty("csvDelim", ";").charAt(0);
		String streamName = cfg.getProperty("streamName", "test:labdata");
		
		DataDictionary dd = DataDictionary.getInstance();
		SDFEntity stream = dd.getEntity(streamName);
		SDFAttributeList schema = stream.getAttributes();
		
		ObjectInputStream iStream = null;
		if (limit > 0) {
			System.out.println("Begin caching of " + limit + " values");
			if(!useCSV){
				iStream = new ObjectInputStream(new FileInputStream(inputFile));
				cachedValues = new RelationalTuple[limit][1];
				for (int i = 0; i < limit; ++i) {
					cachedValues[i][0] = iStream.readObject();
				}
				iStream.close();
			}else{				
				cachedValues = new Object[limit][schema.size()];
				
				CSVParser csvParser = new CSVParser(new FileInputStream(inputFile));
				csvParser.changeDelimiter(delim);
				csvParser.getLine(); // first line are the attribute names, so do not process it
				for(int i = 0; i<limit; i++){
					String[] line = csvParser.getLine();
					inner:
					for(int u = 0; u<schema.size(); u++){
						SDFAttribute attr = schema.get(u);
						if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
							cachedValues[i][u] = Double.parseDouble(line[u]);
						}
						else if(SDFDatatypes.isDouble(attr.getDatatype())){
							cachedValues[i][u] = Double.parseDouble(line[u]);
						}
						else if(SDFDatatypes.isInteger(attr.getDatatype())){
							cachedValues[i][u] = Integer.parseInt(line[u]);
						}
						else if(SDFDatatypes.isLong(attr.getDatatype())){
							cachedValues[i][u] = Long.parseLong(line[u]);
						}
						else if(SDFDatatypes.isString(attr.getDatatype())){
							cachedValues[i][u] = line[u];
						}
						else if(SDFDatatypes.isDate(attr.getDatatype())){
							SimpleDateFormat df = new SimpleDateFormat(attr.getDtConstraint("format").getCType());
							try{
								Date date = df.parse(line[u]);
								Calendar cal = Calendar.getInstance();
								cal.setTime(date);
								cachedValues[i][u] = cal;
							}catch(ParseException e){
								// maybe there is some error in the measurement line
								// kill the whole line and parse the next one.
								i--;
								break inner;
							}
						}
					}
				}
			}
			System.out.println("Caching done");
		}
		IntervalLatencyPriorityFactory factory = new IntervalLatencyPriorityFactory();
		ServerSocket server = new ServerSocket(port);
		while (true) {
			System.out.println("Waiting for connection on port " + port);
			Socket s = server.accept();
			ClientHandler handler;
			if (useRaw) {
				handler = new RawHandler(s, iStream, limit, inputFile,
						cachedValues, factory, accelerationFactor);
			} else if(useCSV){
				handler = new CSVHandler(s, iStream, limit, inputFile, cachedValues, schema, factory, accelerationFactor, delim);
			}
			else {
				handler = new TupleHandler(s, iStream, limit, inputFile,
						cachedValues, factory, accelerationFactor);
			}
			handler.start();
		}
	}
}

abstract class ClientHandler extends Thread {

	protected Socket s;
	protected ObjectInputStream iStream;
	protected Integer limit;
	protected String inputFile;
	protected Object[][] cachedValues;
	protected IntervalLatencyPriorityFactory factory;
	protected Integer accelerationFactor;

	public ClientHandler(
			Socket s,
			ObjectInputStream iStream,
			Integer limit,
			String inputFile,
			Object[][] cachedValues,
			IntervalLatencyPriorityFactory factory,
			Integer accelerationFactor) {
		this.s = s;
		this.iStream = iStream;
		this.limit = limit;
		this.inputFile = inputFile;
		this.cachedValues = cachedValues;
		this.factory = factory;
		this.accelerationFactor = accelerationFactor;

	}

}

class TupleHandler extends ClientHandler {
	public TupleHandler(
			Socket s,
			ObjectInputStream stream,
			Integer limit,
			String inputFile,
			Object[][] cachedValues,
			IntervalLatencyPriorityFactory factory,
			Integer accelerationFactor) {
		super(s, stream, limit, inputFile, cachedValues, factory,
				accelerationFactor);
	}

	@Override
	public void run() {
		System.out.println("|->Connection from " + s.getRemoteSocketAddress());
		ObjectOutputStream oStream = null;
		try {
			if (limit < 1) {
				if (iStream != null) {
					iStream.close();
				}
				iStream = new ObjectInputStream(new FileInputStream(inputFile));
			}
			oStream = new ObjectOutputStream(s.getOutputStream());

			Long lastTimestamp = null;
			Long diff = 0L;
			int i = 0;
			while (limit < 1 || i < limit) {
				RelationalTuple obj = limit > 0 ? (RelationalTuple)cachedValues[i++][0]
						: (RelationalTuple) iStream.readObject();
				Long timestamp = (Long) obj.getAttribute(0);
				// obj.setMetadata(factory.createMetadata(obj));
				//System.out.println(obj);
				if (lastTimestamp == null) {
					oStream.writeObject(obj);
					lastTimestamp = timestamp;
				} else {
					Long start = System.currentTimeMillis();
					long millis = (timestamp - lastTimestamp)
							/ accelerationFactor;
					// diff compensates time divergence between expected an
					// actual
					// waiting originating from the imprecise wait by
					// Thread.sleep
					long delta = millis + diff;
					if (delta > 0) {
						Thread.sleep(delta);
					}
					// diff = expected waiting time - actual waiting time
					diff = millis - (System.currentTimeMillis() - start);
					oStream.writeObject(obj);
					lastTimestamp = timestamp;
				}
			}			
			System.out.println(" |->Done");
		} catch (EOFException e) {
			System.out.println(" |->Done");
		} catch (Exception e) {
			System.err.println(" |->Error: " + e.getMessage());
		} finally {
			try {
				oStream.close();
			} catch (Exception e) {
			}
		}

	}
}

class RawHandler extends ClientHandler {
	public RawHandler(
			Socket s,
			ObjectInputStream stream,
			Integer limit,
			String inputFile,
			Object[][] cachedValues,
			IntervalLatencyPriorityFactory factory,
			Integer accelerationFactor) {
		super(s, stream, limit, inputFile, cachedValues, factory,
				accelerationFactor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		System.out.println("|->Connection from " + s.getRemoteSocketAddress());
		ObjectOutputStream oStream = null;
		try {
			if (limit < 1) {
				if (iStream != null) {
					iStream.close();
				}
				iStream = new ObjectInputStream(new FileInputStream(inputFile));
			}
			oStream = new ObjectOutputStream(s.getOutputStream());

			Long lastTimestamp = null;
			Long diff = 0L;
			int i = 0;
			long startDuration = System.nanoTime();
			while (limit < 1 || i < limit) {
				RelationalTuple obj = limit > 0 ? (RelationalTuple)cachedValues[i++][0]
						: (RelationalTuple) iStream.readObject();
				Long timestamp = (Long) obj.getAttribute(0);
				// obj.setMetadata(factory.createMetadata(obj));
				if (lastTimestamp == null) {
					oStream.writeLong((Long) obj.getAttribute(0));
					oStream.writeInt((Integer) obj.getAttribute(1));
					oStream.writeInt((Integer) obj.getAttribute(2));
					oStream.writeDouble((Double) obj.getAttribute(3));
					oStream.writeDouble((Double) obj.getAttribute(4));
					oStream.writeDouble((Double) obj.getAttribute(5));
					oStream.writeDouble((Double) obj.getAttribute(6));
					lastTimestamp = timestamp;
				} else {
					Long start = System.currentTimeMillis();
					long millis = (timestamp - lastTimestamp)
							/ accelerationFactor;
					// diff compensates time divergence between expected an
					// actual
					// waiting originating from the imprecise wait by
					// Thread.sleep
					long delta = millis + diff;
					if (delta > 0) {
						Thread.sleep(delta);
					}
					// diff = expected waiting time - actual waiting time
					diff = millis - (System.currentTimeMillis() - start);
					oStream.writeLong((Long) obj.getAttribute(0));
					oStream.writeInt((Integer) obj.getAttribute(1));
					oStream.writeInt((Integer) obj.getAttribute(2));
					oStream.writeDouble((Double) obj.getAttribute(3));
					oStream.writeDouble((Double) obj.getAttribute(4));
					oStream.writeDouble((Double) obj.getAttribute(5));
					oStream.writeDouble((Double) obj.getAttribute(6));
					lastTimestamp = timestamp;
				}
			}
			long endDuration = System.nanoTime();
			System.out.println(" |->Done");
			System.out.println("Duration: " + (endDuration - startDuration));
		} catch (EOFException e) {
			System.out.println(" |->Done");
		} catch (Exception e) {
			System.err.println(" |->Error: " + e.getMessage());
		} finally {
			try {
				oStream.close();
			} catch (Exception e) {
			}
		}

	}
}

class CSVHandler extends ClientHandler {
	private SDFAttributeList schema;
	char csvDelim;
	
	public CSVHandler(
			Socket s,
			ObjectInputStream stream,
			Integer limit,
			String inputFile,
			Object[][] cachedValues,
			SDFAttributeList schema,
			IntervalLatencyPriorityFactory factory,
			Integer accelerationFactor, char delim) {
		super(s, stream, limit, inputFile, cachedValues, factory,
				accelerationFactor);
		this.schema = schema;
		this.csvDelim = delim;
	}

	@Override
	public void run() {
		System.out.println("|->Connection from " + s.getRemoteSocketAddress());
		ObjectOutputStream oStream = null;
		try {
			if (limit < 1) {
				if (iStream != null) {
					iStream.close();
				}
				iStream = new ObjectInputStream(new FileInputStream(inputFile));
			}
			oStream = new ObjectOutputStream(s.getOutputStream());

			Long lastTimestamp = null;
			Long diff = 0L;
			int i = 0;
			long startDuration = System.nanoTime();
			CSVParser csvParser = new CSVParser(new FileInputStream(inputFile));
			csvParser.changeDelimiter(this.csvDelim);
			csvParser.getLine(); // first line are the attribute names, so do not process it.
			while (limit < 1 || i < limit) {
				if(limit > 0){
					for(int u = 0; u<cachedValues[i].length; u++){
						SDFAttribute attr = this.schema.get(u);
						if(SDFDatatypes.isMeasurementValue(attr.getDatatype()) ||
								SDFDatatypes.isDouble(attr.getDatatype())){
							oStream.writeDouble((Double)cachedValues[i][u]);
						}
						else if(SDFDatatypes.isInteger(attr.getDatatype())){
							oStream.writeInt((Integer)cachedValues[i][u]);
						}
						else if(SDFDatatypes.isLong(attr.getDatatype())){
							oStream.writeLong((Long)cachedValues[i][u]);
						}
						else if(SDFDatatypes.isString(attr.getDatatype())){
							oStream.writeUTF((String)cachedValues[i][u]);
						}
						else if(SDFDatatypes.isDate(attr.getDatatype())){
							oStream.writeLong(((Calendar)cachedValues[i][u]).getTimeInMillis());
						}
					}
				}else{
					String[] line = null;
					try{
						line = csvParser.getLine();
						for(int u = 0; u<line.length; u++){
							SDFAttribute attr = this.schema.get(i);
							if(SDFDatatypes.isMeasurementValue(attr.getDatatype()) ||
									SDFDatatypes.isDouble(attr.getDatatype())){
								oStream.writeDouble(Double.parseDouble(line[u]));
							}
							else if(SDFDatatypes.isInteger(attr.getDatatype())){
								oStream.writeInt(Integer.parseInt(line[u]));
							}
							else if(SDFDatatypes.isLong(attr.getDatatype())){
								oStream.writeLong(Long.parseLong(line[u]));
							}
							else if(SDFDatatypes.isString(attr.getDatatype())){
								oStream.writeUTF(line[u]);
							}
							else if(SDFDatatypes.isDate(attr.getDatatype())){
								SimpleDateFormat df = new SimpleDateFormat(attr.getDtConstraint("format").getCType());
								try{
									Date date = df.parse(line[u]);
									Calendar cal = Calendar.getInstance();
									cal.setTime(date);
									oStream.writeLong(cal.getTimeInMillis());
								}catch(ParseException e){
									throw new RuntimeException("Date not correctly formatted.");
								}
								
							}
						}
					}catch(IOException e){
						break;
					}
				}
				i++;
			}
			long endDuration = System.nanoTime();
			System.out.println(" |->Done" + " i = " + i);
			System.out.println("Duration: " + (endDuration - startDuration));
		} catch (EOFException e) {
			System.out.println(" |->Done");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(" |->Error: " + e.getMessage());
		} finally {
			try {
				oStream.close();
			} catch (Exception e) {
			}
		}

	}
}
