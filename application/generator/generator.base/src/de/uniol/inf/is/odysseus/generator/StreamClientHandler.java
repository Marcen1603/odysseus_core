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
/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.valuegenerator.DataType;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;

public abstract class StreamClientHandler extends Thread {

	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
	private Socket connection;
	private List<IValueGenerator> generators = new ArrayList<IValueGenerator>();
	private Map<IValueGenerator, DataType> datatypes = new HashMap<IValueGenerator, DataType>();
	private boolean paused = false;
	private NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);
	private int instanceNumber = 0;
	private String streamName = "";
	/**
	 * Gibt an, ob der StreamClientHandler Security Aware ist, also Security
	 * Punctuation verarbeitet.
	 */
	private Boolean isSA = false;

	private boolean printthroughput = false;
	private int throughputEach;
	private int counter = 0;
	private long lastTime = 0;
	private long startTime = 0;
	private int lastSize = 0;
	private int totalSize = 0;

	public abstract void init();

	public abstract void close();

	public abstract List<DataTuple> next() throws InterruptedException;

	public void pause() {
		synchronized (this) {
			this.paused = true;
		}
	}

	public void stopClient() {
		this.interrupt();
	}

	public void proceed() {
		synchronized (this) {
			this.paused = false;
			notify();
		}
	}

	private void internalInit() {
		for (IValueGenerator gen : this.generators) {
			gen.init();
		}
		this.startTime = System.currentTimeMillis();
		this.lastTime = startTime;
		this.counter = 0;
		init();
	}

	@Override
	public void run() {
		internalInit();
		List<DataTuple> next;
		try {
			next = next();
		} catch (InterruptedException ie) {
			System.out.println("Thread interrupted. Stopping client for"+getInternalName());
			next = null;
		}
		while (next != null) {
			try {
				if (this.connection.isClosed()) {
					System.out.println("Connection closed for "+getInternalName());
					break;
				}
				for (DataTuple nextTuple : next) {							
					transferTuple(nextTuple);
					if (printthroughput) {
						counter++;						
						int size = nextTuple.memSize;
						this.totalSize += size;
						this.lastSize += size;
						if ((counter % throughputEach) == 0) {							
							printStats();
							lastTime = System.currentTimeMillis();
							lastSize = 0;
						}
					}
				}
				next = next();

			} catch (InterruptedException ie) {
				System.out.println("Thread interrupted. Stopping client for "+getInternalName());
				if (printthroughput) {
					System.out.println("Total stats:");
					printStats();
				}
				next = null;
			} catch (IOException e) {
				System.out.println("Connection closed for "+getInternalName());
				if (printthroughput) {
					System.out.println("Total stats for :");
					printStats();
				}
				break;
			}
			synchronized (this) {
				while (paused) {
					try {
						wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		close();
	}

	private String getInternalName(){
		return this.streamName+" #"+this.instanceNumber;
	}
	
	/**
	 * @param nextTuple
	 */
	protected void printStats() {
		System.out.println("--- "+getInternalName()+"---");
		double needed = System.currentTimeMillis()-lastTime;		
		double total = System.currentTimeMillis() - startTime;
		System.out.println("Duration for last " + nf.format(throughputEach) + " elements: \t" + nf.format(needed) + "\t ms for " + nf.format(lastSize) + "\t bytes. Throughput: \t"+Math.round((lastSize/needed)*100.)/100.+" bytes/ms");
		System.out.println("Total for "+nf.format(counter)+" elements: \t\t" + nf.format(total) + "\t ms for " + nf.format(totalSize) + "\t bytes. Throughput: \t"+Math.round((totalSize/total)*100.)/100.+" \tbytes/ms");
	}
		

	public void transferTuple(DataTuple tuple) throws IOException {
		if (tuple != null) {
			ByteBuffer buffer = getByteBuffer(tuple);
			synchronized (gbuffer) {
				gbuffer.clear();
				gbuffer.putInt(buffer.limit());
				gbuffer.flip();
				SocketChannel ch = connection.getChannel();
				ch.write(gbuffer);
				ch.write(buffer);
			}
		} else {
			connection.getChannel().close();

		}
	}

	private ByteBuffer getByteBuffer(DataTuple tuple) {
		bytebuffer = ByteBuffer.allocate(tuple.memSize(false));
		bytebuffer.clear();

		if (tuple instanceof SADataTuple) {
			if (isSA) {
				if (((SADataTuple) tuple).isSP()) {
					if(((SADataTuple) tuple).getSPType().equals("predicate")) {
						bytebuffer.putInt(2);
					} else { 
						bytebuffer.putInt(1);
					}
				} else {
					bytebuffer.putInt(0);
				}
			}
		}

		for (Object data : tuple.getAttributes()) {
			if (data instanceof Byte) {
				bytebuffer.put((Byte) data);
			} else if (data instanceof Integer) {
				bytebuffer.putInt((Integer) data);
			} else if (data instanceof Boolean) {
				boolean b = (Boolean) data;
				if (b) {
					bytebuffer.putInt(1);
				} else {
					bytebuffer.putInt(0);
				}
			} else if (data instanceof Double) {
				bytebuffer.putDouble((Double) data);
			} else if (data instanceof Long) {
				bytebuffer.putLong((Long) data);
			} else if (data instanceof String) {
				String s = (String) data;
				bytebuffer.putInt(s.length());
				for (int i = 0; i < s.length(); i++) {
					bytebuffer.putChar(s.charAt(i));
				}
			} else {
				throw new RuntimeException("illegal datatype " + data);
			}
		}
		bytebuffer.flip();
		return bytebuffer;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public void remove() {
	}

	@Override
	public abstract StreamClientHandler clone();

	public List<DataTuple> buildDataTuple() {
		DataTuple tuple = new DataTuple();
		for (IValueGenerator v : this.generators) {
			DataType datatype = datatypes.get(v);
			switch (datatype) {
			case BYTE:
				tuple.addByte(v.nextValue());
				break;
			case BOOLEAN:
				tuple.addBoolean(v.nextValue());
				break;
			case DOUBLE:
				tuple.addDouble(v.nextValue());
				break;
			case INTEGER:
				tuple.addInteger(v.nextValue());
				break;
			case LONG:
				tuple.addLong(v.nextValue());
				break;
			case OBJECT:
				tuple.addAttribute(v.nextValue());
				break;
			case STRING:
				tuple.addString(v.nextValue());
				break;
			}
		}
		return tuple.asList();
	}

	protected void addGenerator(IValueGenerator generator, DataType datatype) {
		this.generators.add(generator);
		this.datatypes.put(generator, datatype);
	}

	protected void addGenerator(IValueGenerator generator) {
		addGenerator(generator, DataType.DOUBLE);
	}

	protected void removeGenerator(IValueGenerator generator) {
		this.generators.remove(generator);
		this.datatypes.remove(generator);
	}

	public void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setIsSA(Boolean isSA) {
		this.isSA = isSA;
	}

	public Boolean isSA() {
		return this.isSA;
	}

	/**
	 * @param throughputEach
	 */
	public void setThroughputEach(int throughputEach) {
		if (throughputEach > 0) {
			this.printthroughput = true;
			this.throughputEach = throughputEach;
		}

	}

	public int getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(int instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
}
