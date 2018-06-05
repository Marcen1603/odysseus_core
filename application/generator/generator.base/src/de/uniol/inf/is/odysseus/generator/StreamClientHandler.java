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
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class StreamClientHandler extends Thread implements IProviderRunner {

	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
	final private List<Socket> connections = new LinkedList<>();
	final private List<Socket> toAdd = new LinkedList<>();
	private boolean paused = false;
	private NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);
	private int instanceNumber = 0;
	private String streamName = "";
	/**
	 * Gibt an, ob der StreamClientHandler Security Aware ist, also Security
	 * Punctuation verarbeitet.
	 */
	private Boolean isSA = false;

	private Charset charset = Charset.forName("UTF-8");
	private CharsetEncoder encoder = charset.newEncoder();

	private boolean printthroughput = false;
	private int throughputEach;
	private static int counter = 0;
	private long lastTime = 0;
	private long startTime = 0;
	private int lastSize = 0;
	private int totalSize = 0;

	private long delay = 0;
	private long lastTransfer = -1;
	private boolean delayEachTuple = false;
	private IDataGenerator generator = null;

	public StreamClientHandler(){
		setPriority(MAX_PRIORITY);
	}
	
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
		this.startTime = System.currentTimeMillis();
		this.lastTime = startTime;				
		counter = 0;		
		generator.init(this);
	}

	public void setGenerator(IDataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run() {
		internalInit();
		List<DataTuple> next;
		try {
			next = generator.next();
		} catch (InterruptedException ie) {
			System.out.println("Thread interrupted. Stopping client for"
					+ getInternalName());
			next = null;
		}
		while (!isInterrupted()) {
			
			updateConnections();
			synchronized (connections) {
				while (connections.size() == 0) {
					try {
						System.out.println("Waiting for connections ...");
						connections.wait();
						updateConnections();
					} catch (InterruptedException e) {
					}
				}
			}

			;
			Iterator<Socket> connectionIter = connections.iterator();
			while (connectionIter.hasNext()) {
				Socket connection = connectionIter.next();
				try {
					if (connection.isClosed()) {
						System.out.println("Connection closed for "
								+ getInternalName());
						connectionIter.remove();
						continue;
					}
					for (DataTuple nextTuple : next) {
						if (delayEachTuple) {
							delay();
						}
						transferTuple(connection.getChannel(), nextTuple);
						printThroughput(nextTuple);
					}
					if (!delayEachTuple) {
						delay();
					}
					next = generator.next();

				} catch (InterruptedException ie) {
					System.out
							.println("Thread interrupted. Stopping client for "
									+ getInternalName());
					if (printthroughput) {
						System.out.println("Total stats:");
						printStats();
					}
					connectionIter.remove();
					continue;
				} catch (IOException e) {
					System.out.println("Connection closed for "
							+ getInternalName());
					if (printthroughput) {
						System.out.println("Total stats for :");
						printStats();
					}
					connectionIter.remove();
					continue;
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
		}
		generator.close();
	}

	private void printThroughput(DataTuple nextTuple) {
		if (printthroughput) {
			increaseCounter();
			int size = nextTuple.memSize;
			this.totalSize += size;
			this.lastSize += size;
			if ((getCounter() % throughputEach) == 0) {
				printStats();
				lastTime = System.currentTimeMillis();
				lastSize = 0;
			}
		}
	}
	
	private static synchronized int getCounter() {
		return counter;
	}

	private static synchronized void increaseCounter(){
		counter++;
	}

	private void delay() {
		if (delay > 0) {
			long now = System.currentTimeMillis();
			if (lastTransfer > 0) {
				long sleepingTime = delay - (now - lastTransfer);
				if (sleepingTime > 0) {
					try {
						Thread.sleep(sleepingTime);
					} catch (InterruptedException e) {
					}
				}
			}
			lastTransfer = now;
		}
	}

	private String getInternalName() {
		return this.streamName + " #" + this.instanceNumber;
	}

	@Override
	public void printStats() {
		System.out.println("--- " + getInternalName() + "---");
		double needed = System.currentTimeMillis() - lastTime;
		double total = System.currentTimeMillis() - startTime;
		System.out.println("Duration for last " + nf.format(throughputEach)
				+ " elements: \t" + nf.format(needed) + "\t ms for "
				+ nf.format(lastSize) + "\t bytes. Throughput: \t"
				+ nf.format(Math.round((lastSize / needed) * 100.) / 100.) + " bytes/ms \t"
				+ nf.format(Math.round((throughputEach / needed) * 1000.))
				+ " tupel/s");
		System.out
				.println("Total for " + nf.format(getCounter()) + " elements: \t\t"
						+ nf.format(total) + "\t ms for "
						+ nf.format(totalSize) + "\t bytes. Throughput: \t"
						+ nf.format(Math.round((totalSize / total) * 100.) / 100.)
						+ " bytes/ms \t"
						+ nf.format(Math.round((getCounter() / total) * 1000.))
						+ " tupel/s");
	}

	@Override
	public double getLastThroughput() {
		double total = System.currentTimeMillis() - startTime;
		return Math.round((totalSize / total) * 100.) / 100.;
	}

	public void transferTuple(SocketChannel ch, DataTuple tuple)
			throws IOException {
		if (tuple != null) {
			ByteBuffer buffer = getByteBuffer(tuple);
			synchronized (gbuffer) {
				gbuffer.clear();
				gbuffer.putInt(buffer.limit());
				gbuffer.flip();
				ch.write(gbuffer);
				ch.write(buffer);
			}
		} else {
			ch.close();

		}
	}

	private ByteBuffer getByteBuffer(DataTuple tuple) {
		bytebuffer = ByteBuffer.allocate(tuple.memSize(false));
		bytebuffer.clear();

		if (tuple instanceof SADataTuple) {
			if (isSA) {
				if (((SADataTuple) tuple).isSP()) {
					if (((SADataTuple) tuple).getSPType().equals("predicate")) {
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
			} else if (data instanceof Short) {
				bytebuffer.putShort((Short) data);
			} else if (data instanceof Long) {
				bytebuffer.putLong((Long) data);
			} else if (data instanceof String) {
				// String s = (String) data;
				// bytebuffer.putInt(s.length());
				// for (int i = 0; i < s.length(); i++) {
				// bytebuffer.putChar(s.charAt(i));
				// }
				String s = (String) data;
				ByteBuffer charBuffer;
				try {
					charBuffer = encoder.encode(CharBuffer.wrap(s));
					bytebuffer.putInt(charBuffer.limit());
					bytebuffer.put(charBuffer);
				} catch (CharacterCodingException e) {
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException("illegal datatype " + data);
			}
		}
		bytebuffer.flip();
		return bytebuffer;
	}

	public void setConnection(Socket connection) {
		this.connections.clear();
		connections.add(connection);
	}

	@Override
	public void setDelay(long delay) {
		this.delay = delay;
	}

	public void setDelayEachTuple(boolean delayEachTuple) {
		this.delayEachTuple = delayEachTuple;
	}

	public void remove() {
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

	public void printStatus() {
		System.out.println("------ Current Status ------");
		printStats();
		System.out.println("----------------------------");
	}

	public void addConnection(Socket connection) {
		synchronized (toAdd) {
			toAdd.add(connection);
		}
		synchronized(connections){
			connections.notifyAll();
		}
	}

	private void updateConnections() {
		if (toAdd.size() > 0) {
			synchronized (toAdd) {
				connections.addAll(toAdd);
				toAdd.clear();
			}
		}
		synchronized (connections) {
			connections.notifyAll();
		}
	}
}
