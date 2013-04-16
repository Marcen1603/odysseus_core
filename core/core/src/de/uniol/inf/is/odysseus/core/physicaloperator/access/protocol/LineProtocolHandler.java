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

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class LineProtocolHandler<T> extends AbstractProtocolHandler<T> {

	protected BufferedReader reader;
	protected BufferedWriter writer;
	private long delay;
	private int nanodelay;
	private int delayeach = 0;
	private long counter = 0L;

	public LineProtocolHandler() {
		super();
	}

	public LineProtocolHandler(ITransportDirection direction,
			IAccessPattern access) {
		super(direction, access);
	}

	protected void init(Map<String, String> options) {
		if (options.get("delay") != null) {
			setDelay(Long.parseLong(options.get("delay")));
		}
		if (options.get("nanodelay") != null) {
			setNanodelay(Integer.parseInt(options.get("nanodelay")));
		}
		if(options.get("delayeach") !=null){
			setDelayeach(Integer.parseInt(options.get("delayeach")));
		}

	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) {
			if ((this.getAccess().equals(IAccessPattern.PULL))
					|| (this.getAccess().equals(IAccessPattern.ROBUST_PULL))) {
				reader = new BufferedReader(new InputStreamReader(
						getTransportHandler().getInputStream()));
			}
		} else {
			writer = new BufferedWriter(new OutputStreamWriter(
					getTransportHandler().getOutputStream()));
		}
		counter = 0;
	}

	@Override
	public void close() throws IOException {
		if (getDirection().equals(ITransportDirection.IN)) {
			if (reader != null) {
				reader.close();
			}
		} else {
			writer.close();
		}
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException {
		if (isDone()){
			return true; // To read null!
		}
		return reader.ready();
	}

	@Override
	public T getNext() throws IOException {
		delay();
		if (reader.ready()) {
			return getDataHandler().readData(reader.readLine());
		} else {
			return null;
		}
	}

	@Override
	public void write(T object) throws IOException {
		writer.write(object.toString());
	}

	protected void delay() {
		if(delayeach>0){
			counter++;
			if(counter<delayeach){				
				return;
			}
			counter = 0;
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
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		LineProtocolHandler<T> instance = new LineProtocolHandler<T>(direction,
				access);
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

	public int getDelayeach() {
		return delayeach;
	}

	public void setDelayeach(int delayeach) {
		this.delayeach = delayeach;
	}
}
