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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class LineProtocolHandler<T> extends AbstractProtocolHandler<T> {

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
	private StringBuffer measurements = new StringBuffer("");
	private long measureEachLine = -1;
	private long lastDumpTime = 0;
	private long basetime;

	public LineProtocolHandler() {
		super();
	}

	public LineProtocolHandler(ITransportDirection direction, IAccessPattern access) {
		super(direction, access);
	}

	protected void init(Map<String, String> options) {
		if (options.get("delay") != null) {
			setDelay(Long.parseLong(options.get("delay")));
		}
		if (options.get("nanodelay") != null) {
			setNanodelay(Integer.parseInt(options.get("nanodelay")));
		}
		if (options.get("delayeach") != null) {
			setDelayeach(Integer.parseInt(options.get("delayeach")));
		}

		if (options.get("readfirstline") != null) {
			readFirstLine = Boolean.parseBoolean(options.get("readfirstline"));
		} else {
			readFirstLine = true;
		}
		if (options.get("dumpeachline") != null) {
			dumpEachLine = Integer.parseInt(options.get("dumpeachline"));
		}

		if (options.get("measureeachline") != null) {
			measureEachLine = Integer.parseInt(options.get("measureeachline"));
			measurements.setLength(0);
		}

		if (options.get("lastline") != null) {
			lastLine = Integer.parseInt(options.get("lastline"));
		}
		if (options.get("maxlines") != null) {
			lastLine = Integer.parseInt(options.get("maxlines"));
		}
		if (options.get("debug") != null) {
			debug = Boolean.parseBoolean(options.get("debug"));
		}
		lastDumpTime = System.currentTimeMillis();

	}

	@Override
	public void open() throws UnknownHostException, IOException {		
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccess().equals(IAccessPattern.PULL)) || (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
				reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
			}
        } else {
            if ((this.getAccess().equals(IAccessPattern.PULL)) || (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
                writer = new BufferedWriter(new OutputStreamWriter(getTransportHandler().getOutputStream()));
            }
        }
		delayCounter = 0;
		lineCounter = 0;
		isDone = false;
		firstLineSkipped = false;
		if(debug){
			ProtocolMonitor.getInstance().addToMonitor(this);
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
		if(debug){
			ProtocolMonitor.getInstance().informMonitor(this, lineCounter);
			ProtocolMonitor.getInstance().removeFromMonitor(this);
		}
	}

	@Override
	public boolean hasNext() throws IOException {
		if (reader.ready() == false) {
			isDone = true;
			return false;
		}
		return true;
	}

	protected String getNextLine() throws IOException {
		if (!firstLineSkipped && !readFirstLine) {
			reader.readLine();
			firstLineSkipped = true;
		}
		delay();
		String line = null;
		if (hasNext()) {
			line = reader.readLine();
		} else {
			long time = System.currentTimeMillis();
			LOG.debug("Read last line. "+ lineCounter + " " + time + " " + (time - lastDumpTime) + " (" + Integer.toHexString(hashCode()) + ") line.");
			return null;
		}

		if (debug) {
			if (dumpEachLine > 0) {
				if (lineCounter % dumpEachLine == 0) {
					long time = System.currentTimeMillis();
					LOG.debug(lineCounter + " " + time + " " + (time - lastDumpTime) + " (" + Integer.toHexString(hashCode()) + ") line: " + line);
					lastDumpTime = time;
				}
			}
			if (measureEachLine > 0) {
				if (lineCounter % measureEachLine == 0) {
					long time = System.currentTimeMillis();
					measurements.append(lineCounter).append(";").append(time - basetime).append("\n");
				}
			}

			if (lastLine == lineCounter || lineCounter == 0) {
				long time = System.currentTimeMillis();
				if (lineCounter == 0) {
					basetime = time;
				}
				LOG.debug(lineCounter + " " + time);
				measurements.append(lineCounter).append(";").append(time - basetime).append("\n");
				if (lastLine == lineCounter) {
					//System.out.println(measurements);
					ProtocolMonitor.getInstance().informMonitor(this, lineCounter);
					isDone = true;
				}
			}		
		}
		lineCounter++;
		return line;
	}

	@Override
	public T getNext() throws IOException {
		String line = getNextLine();
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
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, Map<String, String> options, IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		LineProtocolHandler<T> instance = new LineProtocolHandler<T>(direction, access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		instance.init(options);		
		return instance;
	}

	@Override
	public String getName() {
		return "Line";
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

	@Override
	public void process(ByteBuffer message) {
		getTransfer().transfer(getDataHandler().readData(message));
	}
	
	@Override
	public void process(String[] message) {
		getTransfer().transfer(getDataHandler().readData(message));		
	}

	public int getDelayeach() {
		return delayeach;
	}

	public void setDelayeach(int delayeach) {
		this.delayeach = delayeach;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}
}
