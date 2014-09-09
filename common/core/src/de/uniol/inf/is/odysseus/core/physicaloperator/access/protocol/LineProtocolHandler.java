/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class LineProtocolHandler<T> extends AbstractProtocolHandler<T> {

	protected static final Charset charset = Charset.forName("UTF-8");

	public static final String NAME = "Line";
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(LineProtocolHandler.class);

	protected BufferedReader reader;
	protected BufferedWriter writer;
	private long delay;
	private int nanodelay;
	private int delayeach = 0;
	private long delayCounter = 0L;

	protected boolean readFirstLine = true;
	protected boolean firstLineSkipped = false;
	private long dumpEachLine = -1;
	private long lastLine = -1;
	protected long lineCounter = 0L;
	private boolean debug = false;
	private boolean isDone = false;
	private boolean noDone = false;
	// private StringBuffer measurements = new StringBuffer("");
	private long measureEachLine = -1;
	private long lastDumpTime = 0;
	private long lastMeasureTime = 0;
	private long basetime;
	private String dumpFile = null;
	private PrintWriter dumpOut;
	private boolean dumpMemory;

	private String lastRunRemaining = "";

	//private Map<String, String> optionsMap;

	public static final String DELAY = "delay";
	public static final String NANODELAY = "delay";
	public static final String DELAYEACH = "delayeach";
	public static final String READFIRSTLINE = "readfirstline";
	public static final String DUMP_EACH_LINE = "dumpeachline";
	public static final String MEASURE_EACH_LINE = "measureeachline";
	public static final String LAST_LINE = "lastline";
	public static final String MAX_LINES = "maxlines";
	public static final String DEBUG = "debug";
	public static final String DUMPFILE = "dumpfile";
	public static final String DUMPMEMORY = "dumpmemory";
	public static final String NODONE = "nodone";

	public LineProtocolHandler() {
		super();
	}

	public LineProtocolHandler(ITransportDirection direction,
			IAccessPattern access, IDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		init_internal();
	}

	private void init_internal() {
		OptionMap options = optionsMap; 
		if (options.get(DELAY) != null) {
			setDelay(Long.parseLong(options.get(DELAY)));
		}
		if (options.get(NANODELAY) != null) {
			setNanodelay(Integer.parseInt(options.get(NANODELAY)));
		}
		if (options.get(DELAYEACH) != null) {
			setDelayeach(Integer.parseInt(options.get(DELAYEACH)));
		}

		if (options.get(READFIRSTLINE) != null) {
			readFirstLine = Boolean.parseBoolean(options.get(READFIRSTLINE));
		} else {
			readFirstLine = true;
		}
		if (options.get(DUMP_EACH_LINE) != null) {
			dumpEachLine = Integer.parseInt(options.get(DUMP_EACH_LINE));
		}

		if (options.get(MEASURE_EACH_LINE) != null) {
			measureEachLine = Integer.parseInt(options.get(MEASURE_EACH_LINE));
			// measurements.setLength(0);
		}

		if (options.get(DUMPMEMORY) != null) {
			dumpMemory = Boolean.parseBoolean(options.get(DUMPMEMORY));
		}

		if (options.get(LAST_LINE) != null) {
			lastLine = Integer.parseInt(options.get(LAST_LINE));
		}
		if (options.get(MAX_LINES) != null) {
			lastLine = Integer.parseInt(options.get(MAX_LINES));
		}
		if (options.get(DEBUG) != null) {
			debug = Boolean.parseBoolean(options.get(DEBUG));
		}
		if (options.get(DUMPFILE) != null) {
			dumpFile = options.get(DUMPFILE);
		}
		if (options.get(NODONE) != null) {
			noDone = Boolean.parseBoolean(options.get(NODONE));
		}
		lastDumpTime = System.currentTimeMillis();

	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern()
							.equals(IAccessPattern.ROBUST_PULL))) {
				reader = new BufferedReader(new InputStreamReader(
						getTransportHandler().getInputStream()));
			}
		} else {
			if ((this.getAccessPattern().equals(IAccessPattern.PULL))
					|| (this.getAccessPattern()
							.equals(IAccessPattern.ROBUST_PULL))) {
				writer = new BufferedWriter(new OutputStreamWriter(
						getTransportHandler().getOutputStream()));
			}
		}
		delayCounter = 0;
		lineCounter = 0;
		isDone = false;
		firstLineSkipped = false;
		if (debug) {
			ProtocolMonitor.getInstance().addToMonitor(this);
			if (dumpFile != null) {
				dumpOut = new PrintWriter(dumpFile);
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN)) {
			if (reader != null) {
				reader.close();
			}
		} else {
			if (writer != null) {
				writer.close();
			}
		}
		getTransportHandler().close();
		if (debug) {
			ProtocolMonitor.getInstance().informMonitor(this, lineCounter);
			ProtocolMonitor.getInstance().removeFromMonitor(this);
			if (dumpOut != null) {
				dumpOut.close();
			}
		}
	}

	@Override
	public boolean hasNext() throws IOException {
		return hasNext(reader);
	}
	
	private boolean hasNext( BufferedReader reader ) {
		try {
			if (reader.ready() == false) {
				isDone = true;
				return false;
			}
		} catch (Exception e) {
			LOG.error("Could not determine hasNext()", e);
			isDone = true;
			return false;
		}
		return true;
	}

	protected String getNextLine(BufferedReader toReadFrom) throws IOException {
		if (!firstLineSkipped && !readFirstLine) {
			toReadFrom.readLine();
			firstLineSkipped = true;
		}
		delay();
		String line = null;
		if (hasNext(toReadFrom)) {
			line = toReadFrom.readLine();
		} else {
			long time = System.currentTimeMillis();
			LOG.debug("Read last line. " + lineCounter + " " + time + " "
					+ (time - lastDumpTime) + " ("
					+ Integer.toHexString(hashCode()) + ") line.");
			return null;
		}

		if (debug) {
			if (dumpEachLine > 0) {
				if (lineCounter % dumpEachLine == 0) {
					long time = System.currentTimeMillis();
					LOG.debug(lineCounter
							+ " "
							+ time
							+ " "
							+ (time - lastDumpTime)
							+ (dumpMemory ? " M = " + RUNTIME.freeMemory()
									+ " " : " ") + line + " ("
							+ Integer.toHexString(hashCode()) + ") line: ");
					lastDumpTime = time;
				}
			}
			if (measureEachLine > 0) {
				if (lineCounter % measureEachLine == 0) {
					long time = System.currentTimeMillis();
					// measurements.append(lineCounter).append(";").append(time
					// - basetime).append("\n");
					if (dumpOut != null) {
						dumpOut(time);
					}
					lastMeasureTime = time;
				}
			}

			if (lastLine == lineCounter || lineCounter == 0) {
				long time = System.currentTimeMillis();
				if (lineCounter == 0) {
					basetime = time;
				}
				LOG.debug(lineCounter + " " + time);
				if (dumpOut != null) {
					dumpOut(time);
				}
				// measurements.append(lineCounter).append(";").append(time -
				// basetime).append("\n");
				if (lastLine == lineCounter) {
					// System.out.println(measurements);
					ProtocolMonitor.getInstance().informMonitor(this,
							lineCounter);
					isDone = true;
				}
			}
		}
		lineCounter++;
		return line;
	}

	public void dumpOut(long time) {
		dumpOut.print(lineCounter + ";" + (time - basetime) + ";"
				+ (time - lastMeasureTime));
		if (dumpMemory) {
			dumpOut.println(RUNTIME.freeMemory());
		} else {
			dumpOut.println();
		}
		dumpOut.flush();
	}

	@Override
	public T getNext() throws IOException {
		String line = getNextLine(reader);
		if (line != null) {
			return getDataHandler().readData(line);
		} else {
			return null;
		}
	}

	@Override
	public void write(T object) throws IOException {
		writer.write(object.toString());
	}

	private static String bb_to_str(ByteBuffer buffer) {
		String data = "";
		try {
			data = charset.decode(buffer).toString();
			// reset buffer's position to its original so it is not altered:
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return data;
	}

	@Override
	public void process(ByteBuffer message) {
		String strMsg = bb_to_str(message);
		String data = lastRunRemaining + strMsg;
		StringTokenizer t = new StringTokenizer(data, "\n\r");
		boolean process = true;

		while (process && t.hasMoreTokens()) {
			String token = t.nextToken();
			// The last token could be incomplete --> process next time
			if (t.hasMoreTokens()) {

				if (!firstLineSkipped && !readFirstLine) {
					firstLineSkipped = true;
					continue;
				} else {
					process(token);
				}

			} else {
				// There are some cases, where the last token contains the
				// whole line
				if (data.endsWith("\n") || data.endsWith("\r")) {
					process(token);
					lastRunRemaining = "";
				} else {
					lastRunRemaining = token;
				}
				process = false;
			}
		}
	}

	protected void process(String token) {
		T retValue = getDataHandler().readData(token);
		getTransfer().transfer(retValue);
	}

	@Override
	public void process(InputStream message) {
		boolean firstLineSkippedLocal = false;
		BufferedReader r = new BufferedReader(new InputStreamReader(message));
		String line;
		try {
			while ((line = getNextLine(r)) != null) {
				if (!firstLineSkippedLocal && !readFirstLine) {
					firstLineSkippedLocal = true;
					continue;
				} else {
					process(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void delay() {
		if (delayeach > 0) {
			delayCounter++;
			if (delayCounter < delayeach) {
				return;
			}
			delayCounter = 0;
		}
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// interrupting the delay might be correct
				// e.printStackTrace();
			}
		} else {
			if (nanodelay > 0) {
				try {
					Thread.sleep(0L, nanodelay);
				} catch (InterruptedException e) {
					// interrupting the delay might be correct
					// e.printStackTrace();
				}
			}
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		LineProtocolHandler<T> instance = new LineProtocolHandler<T>(direction,
				access, dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setNanodelay(int nanodelay) {
		this.nanodelay = nanodelay;
	}

	public int getNanodelay() {
		return nanodelay;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	public int getDelayeach() {
		return delayeach;
	}

	public void setDelayeach(int delayeach) {
		this.delayeach = delayeach;
	}

	@Override
	public boolean isDone() {
		if (noDone) {
			return false;
		} else {
			return isDone;
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof LineProtocolHandler)) {
			return false;
		}
		LineProtocolHandler<?> other = (LineProtocolHandler<?>) o;
		if (this.nanodelay != other.getNanodelay()
				|| this.delay != other.getDelay()
				|| this.delayeach != other.getDelayeach()
				|| this.dumpEachLine != other.getDumpEachLine()
				|| this.measureEachLine != other.getMeasureEachLine()
				|| this.lastLine != other.getLastLine()
				|| this.debug != other.isDebug()
				|| this.readFirstLine != other.isReadFirstLine()) {
			return false;
		}
		return true;
	}

	public boolean isReadFirstLine() {
		return readFirstLine;
	}

	public long getDumpEachLine() {
		return dumpEachLine;
	}

	public long getLastLine() {
		return lastLine;
	}

	public boolean isDebug() {
		return debug;
	}

	public long getMeasureEachLine() {
		return measureEachLine;
	}
}
