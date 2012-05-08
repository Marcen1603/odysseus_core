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
package de.uniol.inf.is.odysseus.generator;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.valuegenerator.DataType;
import de.uniol.inf.is.odysseus.generator.valuegenerator.IValueGenerator;

public abstract class StreamClientHandler extends Thread {

	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
	private Socket connection;
	private List<IValueGenerator> generators = new ArrayList<IValueGenerator>();
	private Map<IValueGenerator, DataType> datatypes = new HashMap<IValueGenerator, DataType>();

	public abstract void init();

	public abstract void close();

	public abstract List<DataTuple> next();

	private void internalInit() {
		for (IValueGenerator gen : this.generators) {
			gen.init();
		}
		init();
	}

	@Override
	public void run() {
		internalInit();
		List<DataTuple> next = next();
		while (next != null) {
			try {
				if (this.connection.isClosed()) {
					System.out.println("Connection closed.");
					break;
				}
				for (DataTuple nextTuple : next) {
					transferTuple(nextTuple);
				}
				next = next();

			} catch (IOException e) {
				System.out.println("Connection closed.");
				break;
			}
		}
		close();
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
		for (Object data : tuple.getAttributes()) {
			if (data instanceof Integer) {
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
}
