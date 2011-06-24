/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.labdataserver;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import com.Ostermiller.util.CSVParser;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

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
	private static final String DEFAULT_PROPERTIES_FILE = "labdata_cfg/labdataserver.ini";
	private static final String DEFAULT_PORT = "55555";
	private static Object[][] cachedValues = null;

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		String filename = DEFAULT_PROPERTIES_FILE;

		Map<String, String> env = System.getenv();
		if (env.containsKey("labdata_cfg")) {
			filename = env.get("labdata_cfg");
		}

		Properties cfg = new Properties();
		try {
			URL configFileURL = Activator.getContext().getBundle()
					.getEntry(filename);
			cfg.load(configFileURL.openStream());
		} catch (FileNotFoundException e) {
			System.out.println("Did not find config file.");
			throw e;
		} catch (IOException e) {
			throw e;
		}

		// String inputFile =
		// "C:\\Diss\\OdysseusOSGi\\application\\de.uniol.inf.is.odysseus.labdataserver\\labdata_cfg\\movingObjectsOL.csv";
		String inputFile = "labdata_cfg/"
				+ cfg.getProperty("inputfile", "labdata");
		URL inputFileURL = Activator.getContext().getBundle()
				.getEntry(inputFile);

		Integer limit = Integer.parseInt(cfg.getProperty("limit", "-1"));

		Integer accelerationFactor = Integer.parseInt(cfg.getProperty(
				"acceleration", "1"));
		Integer port = Integer.parseInt(cfg.getProperty("port", DEFAULT_PORT));

		boolean useRaw = Boolean
				.parseBoolean(cfg.getProperty("useRaw", "true"));

		boolean useCSV = Boolean.parseBoolean(cfg
				.getProperty("useCSV", "false"));

		char delim = cfg.getProperty("csvDelim", ";").charAt(0);

		String streamName = cfg.getProperty("streamName", "test:labdata");

		String stream_definition_file = "labdata_cfg/"
				+ cfg.getProperty("streamDefinitons");

		int elementsPerSecond = 1;
		try {
			elementsPerSecond = Integer.parseInt(cfg.getProperty("frequency"));
		} catch (Exception e) {
			// ignore
		}

		SDFEntity stream = null;

		IDataDictionary dd = DataDictionaryFactory.getDefaultDataDictionary("Labdata Server");
		try {
			stream = dd.getEntity(streamName, UserManagement.getInstance()
					.getSuperUser());
		} catch (Exception e) {
			// Ignore
		}

		// Load only if not already registered
		if (stream == null) {

			URL stream_definition_URL = Activator.getContext().getBundle()
					.getEntry(stream_definition_file);
			InputStream input = stream_definition_URL.openStream();

			InputStreamReader reader = new InputStreamReader(input);
			try {
				CQLParser.getInstance().parse(reader,
						UserManagement.getInstance().getSuperUser(),dd);
			} catch (QueryParseException e) {
				e.printStackTrace();
			}

			stream = dd.getEntity(streamName, UserManagement.getInstance()
					.getSuperUser());
		}

		SDFAttributeList schema = stream.getAttributes();

		ObjectInputStream iStream = null;
		if (limit > 0) {
			// System.out.println("Begin caching of " + limit +
			// " values from file: " + inputFileURL);
			if (!useCSV) {

				iStream = new ObjectInputStream(inputFileURL.openStream());
				cachedValues = new RelationalTuple[limit][1];
				for (int i = 0; i < limit; ++i) {
					cachedValues[i][0] = iStream.readObject();
				}
				iStream.close();
			} else {
				cachedValues = new Object[limit][schema.size()];

				CSVParser csvParser = new CSVParser(inputFileURL.openStream());
				csvParser.changeDelimiter(delim);
				csvParser.getLine(); // first line are the attribute names, so
				// do not process it
				int count = 0;
				for (int i = 0; i < limit; i++) {
					String[] line = csvParser.getLine();
					// if line is null, eof is reached
					// so limit has been set higher than data is available
					if (line == null) {
						break;
					}
					inner: for (int u = 0; u < schema.size(); u++) {
						SDFAttribute attr = schema.get(u);
						// hack fix for timestamp @TODO fix it someday.
						if (attr.getDatatype().getURI(false)
								.endsWith("Timestamp")) {
							cachedValues[i][u] = Long.parseLong(line[u]);
						} else if (attr.getDatatype().isMeasurementValue()) {
							cachedValues[i][u] = Double.parseDouble(line[u]);
						} else if (attr.getDatatype().isDouble()) {
							cachedValues[i][u] = Double.parseDouble(line[u]);
						} else if (attr.getDatatype().isInteger()) {
							cachedValues[i][u] = Integer.parseInt(line[u]);
						} else if (attr.getDatatype().isLong()) {
							cachedValues[i][u] = Long.parseLong(line[u]);
						} else if (attr.getDatatype().isString()) {
							cachedValues[i][u] = line[u];
						} else if (attr.getDatatype().isDate()) {
							SimpleDateFormat df = new SimpleDateFormat(attr
									.getDtConstraint("format").getCType());
							try {
								Date date = df.parse(line[u]);
								Calendar cal = Calendar.getInstance();
								cal.setTime(date);
								cachedValues[i][u] = cal;
							} catch (ParseException e) {
								// maybe there is some error in the measurement
								// line
								// kill the whole line and parse the next one.
								i--;
								break inner;
							}
						}
					}
					count = i;
				}
				System.out.println("Read " + count + " csv lines.");
			}
			System.out.println("Caching of done.");
		}
		ServerSocket server = new ServerSocket(port);

		while (true) {
			try{
				System.out.println("Waiting for connection on port " + port);
				Socket s = server.accept();
				ClientHandler handler;
				if (useRaw) {
					handler = new RawHandler(s, iStream, limit, inputFile,
							cachedValues, accelerationFactor);
				} else if (useCSV) {
					handler = new CSVHandler(s, iStream, limit, inputFile,
							cachedValues, schema, elementsPerSecond, delim);
				} else {
					handler = new TupleHandler(s, iStream, limit, inputFile,
							cachedValues, accelerationFactor);
				}
				handler.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

abstract class ClientHandler extends Thread {

	protected Socket s;
	protected ObjectInputStream iStream;
	protected Integer limit;
	protected String inputFile;
	protected Object[][] cachedValues;
	protected Integer accelerationFactor;

	public ClientHandler(Socket s, ObjectInputStream iStream, Integer limit,
			String inputFile, Object[][] cachedValues,
			Integer accelerationFactor) {
		this.s = s;
		this.iStream = iStream;
		this.limit = limit;
		this.inputFile = inputFile;
		this.cachedValues = cachedValues;
		this.accelerationFactor = accelerationFactor;

	}

}

class TupleHandler extends ClientHandler {
	public TupleHandler(Socket s, ObjectInputStream stream, Integer limit,
			String inputFile, Object[][] cachedValues,
			Integer accelerationFactor) {
		super(s, stream, limit, inputFile, cachedValues, accelerationFactor);
	}

	@SuppressWarnings("unchecked")
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
				RelationalTuple obj = limit > 0 ? (RelationalTuple) cachedValues[i++][0]
						: (RelationalTuple) iStream.readObject();
				Long timestamp = (Long) obj.getAttribute(0);
				// obj.setMetadata(factory.createMetadata(obj));
				// System.out.println(obj);
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
			e.printStackTrace();
		} finally {
			try {
				oStream.close();
			} catch (Exception e) {
			}
		}

	}
}

class RawHandler extends ClientHandler {
	public RawHandler(Socket s, ObjectInputStream stream, Integer limit,
			String inputFile, Object[][] cachedValues,
			Integer accelerationFactor) {
		super(s, stream, limit, inputFile, cachedValues, accelerationFactor);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
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
				RelationalTuple obj = limit > 0 ? (RelationalTuple) cachedValues[i++][0]
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
	int elementsPerSecond;
	int periodLength;

	public CSVHandler(Socket s, ObjectInputStream stream, Integer limit,
			String inputFile, Object[][] cachedValues, SDFAttributeList schema,
			Integer elementsPerSecond, char delim) {
		super(s, stream, limit, inputFile, cachedValues, elementsPerSecond);
		this.schema = schema;
		this.csvDelim = delim;
		this.periodLength = 1000000000 / elementsPerSecond; // in nanoseconds
		System.out.println("Sending all " + this.periodLength + "ns.");
	}

	@Override
	public void run() {
		System.out.println("|->Connection from " + s.getRemoteSocketAddress());
		ObjectOutputStream oStream = null;
		try {
			oStream = new ObjectOutputStream(s.getOutputStream());

			// Long lastTimestamp = null;
			// Long diff = 0L;
			int i = 0;
			long startDuration = System.nanoTime();
			boolean stop = false;

			while ((limit < 1 || i < limit) && !stop) {
				//System.out.println("Sending Tuple");
				long lastTime = System.nanoTime();
				if (limit > 0) {

					for (int u = 0; u < cachedValues[i].length; u++) {
						// if the following condition is true,
						// limit is higher than data is available.
						if (cachedValues[i] == null
								|| cachedValues[i][u] == null) {
							stop = true;
							break;
						}
						SDFAttribute attr = this.schema.get(u);
						// hack fix for timestamp @TODO fix it someday.
						if (attr.getDatatype().getURI(false)
								.endsWith("Timestamp")) {
							oStream.writeLong((Long) cachedValues[i][u]);
						} else if (attr.getDatatype().isMeasurementValue()
								|| attr.getDatatype().isDouble()) {
							oStream.writeDouble((Double) cachedValues[i][u]);
						} else if (attr.getDatatype().isInteger()) {
							oStream.writeInt((Integer) cachedValues[i][u]);
						} else if (attr.getDatatype().isLong()) {
							oStream.writeLong((Long) cachedValues[i][u]);
						} else if (attr.getDatatype().isString()) {
							oStream.writeUTF((String) cachedValues[i][u]);
						} else if (attr.getDatatype().isDate()) {
							oStream.writeLong(((Calendar) cachedValues[i][u])
									.getTimeInMillis());
						}
						oStream.flush();
					}
				} else {
					URL inputFileURL = Activator.getContext().getBundle()
							.getEntry(inputFile);
					CSVParser csvParser = null;
					File file = null;
					try {
						file = new File(inputFile);
						csvParser = new CSVParser(inputFileURL.openStream());
					} catch (Exception e) {
						System.out.println("File not found: "
								+ file.getAbsolutePath());
						return;
					}
					csvParser.changeDelimiter(this.csvDelim);
					csvParser.getLine(); // first line are the attribute names,
											// so do
					// not process it.
					String[] line = null;
					try {
						line = csvParser.getLine();
						for (String e : line) {
							System.out.print(e + ";");
						}
						System.out.println();
						for (int u = 0; u < line.length; u++) {
							SDFAttribute attr = this.schema.get(i);
							if (attr.getDatatype().isMeasurementValue()
									|| attr.getDatatype().isDouble()) {
								oStream.writeDouble(Double.parseDouble(line[u]));
							} else if (attr.getDatatype().isInteger()) {
								oStream.writeInt(Integer.parseInt(line[u]));
							} else if (attr.getDatatype().isLong()) {
								oStream.writeLong(Long.parseLong(line[u]));
							} else if (attr.getDatatype().isString()) {
								oStream.writeUTF(line[u]);
							} else if (attr.getDatatype().isDate()) {
								SimpleDateFormat df = new SimpleDateFormat(attr
										.getDtConstraint("format").getCType());
								try {
									Date date = df.parse(line[u]);
									Calendar cal = Calendar.getInstance();
									cal.setTime(date);
									oStream.writeLong(cal.getTimeInMillis());
								} catch (ParseException e) {
									throw new RuntimeException(
											"Date not correctly formatted.");
								}

							}
						}
					} catch (IOException e) {
						break;
					} 
				}

				i++;
				// wait for the next element for this.periodLength nanoseconds
				long expectedTime = lastTime + this.periodLength;
				while (expectedTime > System.nanoTime()) {
					int m = 0;
					m = 1;
				}
			}
			long endDuration = System.nanoTime();
			System.out.println(" |->Done" + " i = " + i);
			System.out.println("Duration: " + (endDuration - startDuration));
			System.out
					.println("Frequency: "
							+ ((double) i * 1000000000 / (double) ((double) endDuration - (double) startDuration)));
//			oStream.close();
		} catch (EOFException e) {
			System.out.println(" |->Done");
		} catch (Exception e) {
			System.err.println(" |->Error: " + e.getMessage());
		} finally {
			try {
				oStream.close();
			} catch (Exception e) {
			}
			System.out.println("Terminating Client Handler");
		}

	}
}
