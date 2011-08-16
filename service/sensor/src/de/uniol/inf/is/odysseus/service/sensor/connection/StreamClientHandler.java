/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.service.sensor.connection;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.service.sensor.data.DataTuple;
import de.uniol.inf.is.odysseus.service.sensor.data.DataType;

/**
 * Handles a connected client (which should be normally an instance of Odysseus).
 */
public class StreamClientHandler {

	/** The gbuffer provided the limit of the bytebuffer. */
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	
	/** The bytebuffer is the byte representation for the current tuple that has to be sent. */
	private ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
	
	/** The socket connection to the client. */
	private Socket connection;
	
	/** The attributes. */
	private Map<String, DataType> attributes = new HashMap<String, DataType>();
	
	/** The is dead. */
	private boolean isDead = false;

	/**
	 * Instantiates a new stream client handler.
	 *
	 * @param connection the connection to the client
	 * @param attributes the attributes for the sensor
	 */
	public StreamClientHandler(Socket connection, Map<String, DataType> attributes) {
		this.connection = connection;
		this.attributes = attributes;
	}

	/**
	 * Transfers a tuple via the socket to the client.
	 *
	 * @param tuple the tuple that should be transfered
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void transferTuple(DataTuple tuple) throws IOException {
		if (tuple != null) {
			if (!this.isDead) {
				ByteBuffer buffer = getByteBuffer(tuple);
				synchronized (gbuffer) {
					gbuffer.clear();
					gbuffer.putInt(buffer.limit());
					gbuffer.flip();
					SocketChannel ch = connection.getChannel();
					ch.write(gbuffer);
					ch.write(buffer);
				}
			}
		} else {
			connection.getChannel().close();
			this.isDead = true;
		}
	}

	/**
	 * Gets the byte buffer for a given tuple. So it transforms 
	 * each value into a byte representation. It also checks, if 
	 * the given values for an attribute are according the data 
	 * type that was given for this sensor. If this check fails, 
	 * the method tries to cast/convert the given value into the
	 * data type that was given for the attribute.
	 * 
	 * @param tuple the tuple to convert
	 * @return the byte buffer for the tuple
	 */
	private ByteBuffer getByteBuffer(DataTuple tuple) {
		bytebuffer = ByteBuffer.allocate(tuple.memSize(false));
		bytebuffer.clear();

		for (Entry<String, DataType> e : this.attributes.entrySet()) {
			Object data = tuple.getAttribute(e.getKey());			
			this.isDataTypeOK(e, data);
			DataType expectedType = getSimpleType(e.getValue());
			
			if (expectedType == DataType.INTEGER) {
				bytebuffer.putInt(getInteger(data));
			} else if (expectedType == DataType.DOUBLE) {
				bytebuffer.putDouble(getDouble(data));
			} else if (expectedType == DataType.LONG) {
				bytebuffer.putLong(getLong(data));
			} else if (expectedType == DataType.STRING) {
				String s = data.toString();
				bytebuffer.putInt(s.length());
				for (int i = 0; i < s.length(); i++) {
					bytebuffer.putChar(s.charAt(i));
				}
			} else {
				throw new RuntimeException("Illegal datatype " + data + ". It should be an Integer, a Double, a Long or a String!");
			}
		}
		bytebuffer.flip();
		return bytebuffer;
	}

	/**
	 * Checks if the data type is ok.
	 *
	 * @param soll the name and data type
	 * @param data the data to check
	 */
	private void isDataTypeOK(Entry<String, DataType> soll, Object data) {
		DataType sollType = soll.getValue();
		DataType istType = getAccordingDataType(data);
		// first every timestamp is a long
		sollType = getSimpleType(sollType);

		if (istType != sollType) {
			System.out.println("WARN: " + "You have put an " + istType + " into the tuple for the attribute \"" + soll.getKey() + "\", although the attribute is defined as an "
					+ sollType.toString().toUpperCase());
			if (sollType == DataType.LONG) {
				System.out.println("Remember: ENDTIMESTAMP, STARTTIMESTAMP and TIMESTAMP are LONGs!");
			}			
		}		
	}
	
	
	/**
	 * Converts a DataType to its simple type.
	 * E.g. each timestamp is a simple long. 
	 *
	 * @param toTest the to test
	 * @return the simple type
	 */
	private DataType getSimpleType(DataType toTest){
		if (toTest == DataType.ENDTIMESTAMP || toTest == DataType.STARTTIMESTAMP || toTest == DataType.TIMESTAMP) {
			toTest = DataType.LONG;
		}
		return toTest;
	}

	/**
	 * Gets the according data type for an object.
	 *
	 * @param data the data
	 * @return the according data type
	 */
	private DataType getAccordingDataType(Object data) {
		if (data instanceof Integer) {
			return DataType.INTEGER;
		}
		if (data instanceof Double) {
			return DataType.DOUBLE;
		}
		if (data instanceof Long) {
			return DataType.LONG;
		}
		if (data instanceof String) {
			return DataType.STRING;
		}
		return null;
	}

	/**
	 * Checks if this handler is dead (e.g. connection was lost).
	 *
	 * @return true, if is dead
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * Sets if this handler is dead.
	 *
	 * @param isDead the new dead
	 */
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	/**
	 * Converts an object to a long or
	 * tries to convert it to a long.
	 * Raises an exception if the cast
	 * fails.
	 *
	 * @param data the data to convert
	 * @return the converted long
	 */
	private Long getLong(Object data) {
		if (data instanceof Long) {
			return (Long) data;
		} else {
			System.out.println("Trying to cast to a Long");
			return new Long(data.toString());
		}
	}

	/**
	 * Converts an object to an integer or
	 * tries to convert it to an integer.
	 * Raises an exception if the cast
	 * fails.
	 *
	 * @param data the data to convert
	 * @return the converted  integer
	 */
	private Integer getInteger(Object data) {
		if (data instanceof Integer) {
			return (Integer) data;
		} else {
			System.out.println("Trying to cast to a Integer");
			return new Integer(data.toString());
		}
	}

	/**
	 * Converts an object to a double or
	 * tries to convert it to a double.
	 * Raises an exception if the cast
	 * fails.
	 *
	 * @param data the data to convert
	 * @return the converted double
	 */
	private Double getDouble(Object data) {
		if (data instanceof Double) {
			return (Double) data;
		} else {
			System.out.println("Trying to cast to a Double");
			return new Double(data.toString());
		}
	}
}
