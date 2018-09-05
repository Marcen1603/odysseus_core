/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.wrapper.nmea;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.util.ByteBufferBackedInputStream;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.MessageXMLParser;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.NMEA2000ProtocolHandlerPlugin;

class N2KMessage
{
	public long timeStamp;
	public long PGN;
	public int priority;
	public int sourceAddress;
	public int destinationAddress;
	public int length;
	public byte[] payload;

	public static N2KMessage fromStream(LittleEndianDataInputStream inStream) throws IOException
	{
		N2KMessage result = new N2KMessage();

		// Read NMEA2K header
		result.timeStamp = readUnsignedInt(inStream);
		result.PGN = readUnsignedInt(inStream);
		result.priority = inStream.readUnsignedByte();
		result.sourceAddress = inStream.readUnsignedByte();
		result.destinationAddress = inStream.readUnsignedByte();
		result.length = (int)readUnsignedInt(inStream);

		// Read payload
		result.payload = new byte[result.length];
		if (inStream.read(result.payload) != result.length) throw new IOException("Not enough input data for payload");

		return result;
	}

	public static N2KMessage fromMap(Map<String, Object> inMap) throws IOException
	{
		N2KMessage result = new N2KMessage();

		result.timeStamp 			= (long) inMap.get("Timestamp");
		result.PGN 					= (long) inMap.get("PGN");
		result.priority 			= (int)  inMap.get("Priority");
		result.sourceAddress 		= (int)  inMap.get("SourceAddress");
		result.destinationAddress 	= (int)  inMap.get("DestinationAddress");

		if (result.PGN == 129025)
		{
			result.length = 8;
			result.payload = new byte[result.length];

			ByteBuffer buffer = ByteBuffer.wrap(result.payload);

			int lat = (int) ((double) inMap.get("Latitude") * 1.0e7);
			int lon = (int) ((double) inMap.get("Longitude") * 1.0e7);

			buffer.putInt(lat);
			buffer.putInt(lon);
		}
		else
			throw new UnsupportedOperationException("Serialization of PGN " + result.PGN + " not implemented!");

		return result;
	}

	public void toStream(LittleEndianDataOutputStream outStream) throws IOException
	{
		writeUnsignedInt(outStream, timeStamp);
		writeUnsignedInt(outStream, PGN);
		writeUnsignedByte(outStream, priority);
		writeUnsignedByte(outStream, sourceAddress);
		writeUnsignedByte(outStream, destinationAddress);
		writeUnsignedInt(outStream, length);
		outStream.write(payload);
	}

	public Map<String, Object> toMap() throws IOException
	{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("Timestamp", timeStamp);
		resultMap.put("PGN", PGN);
		resultMap.put("Priority", priority);
		resultMap.put("SourceAddress", sourceAddress);
		resultMap.put("DestinationAddress", destinationAddress);
//		resultMap.put("Length", length);

//		LittleEndianDataInputStream payloadStream = new LittleEndianDataInputStream(new ByteArrayInputStream(payload));

		NMEA2000ProtocolHandlerPlugin.INSTANCE.extractMessage(resultMap, (int)PGN, payload);

//		if (PGN == 129025)
//		{
//			int lat = payloadStream.readInt();
//			int lon = payloadStream.readInt();
//
//			resultMap.put("Latitude",  (double)(lat) * 1.0e-7);
//			resultMap.put("Longitude", (double)(lon) * 1.0e-7);
//		}

//		payloadStream.close();

		return resultMap;
	}

	private static long readUnsignedInt(LittleEndianDataInputStream inStream) throws IOException
	{
		return inStream.readInt() & 0xffffffffL;
	}

	private static void writeUnsignedByte(LittleEndianDataOutputStream outStream, int unsignedByte) throws IOException
	{
		outStream.writeByte(unsignedByte);
	}

	@SuppressWarnings("unused")
	private static void writeUnsignedShort(LittleEndianDataOutputStream outStream, int unsignedShort) throws IOException
	{
		outStream.writeShort(unsignedShort);
	}

	private static void writeUnsignedInt(LittleEndianDataOutputStream outStream, long unsignedInt) throws IOException
	{
		outStream.writeInt((int) unsignedInt);
	}
};

public class NMEA2000ProtocolHandler extends AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>>
{
	private final Logger LOG = LoggerFactory.getLogger(NMEA2000ProtocolHandler.class);

	public final String NAME = "NMEA2000";
	public final String DELAY = "delay";

	private LittleEndianDataInputStream 	inStream;	// InputStream for GenericPull
	private LittleEndianDataOutputStream 	outStream;
	private long delay = 0;	// Delay on GenericPull

	public NMEA2000ProtocolHandler()
	{
	}

	public NMEA2000ProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler, OptionMap optionsMap)
	{
		super(direction, access, dataHandler, optionsMap);
		delay = optionsMap.getInt(DELAY, 0);
	}

	@Override
	public void open() throws UnknownHostException, IOException
	{
		MessageXMLParser.INSTANCE.parse();
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN))
		{
			if (getAccessPattern().equals(IAccessPattern.PULL) || getAccessPattern().equals(IAccessPattern.ROBUST_PULL))
				inStream = new LittleEndianDataInputStream(getTransportHandler().getInputStream());
		}
		else
		{
			outStream = new LittleEndianDataOutputStream(getTransportHandler().getOutputStream());
		}
	}

	@Override
	public void close() throws IOException
	{
		try
		{
			if (inStream != null)
				inStream.close();
			if (outStream != null)
				outStream.close();
		}
		catch (Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		getTransportHandler().close();
	}

	@Override
	public boolean hasNext() throws IOException
	{
		if (delay > 0)
		{
			try
			{
				Thread.sleep(delay);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}

		try
		{
			return inStream.available() > 0;
		}
		catch (IOException e)
		{
			return false;
		}
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext() throws IOException
	{
		N2KMessage msg = N2KMessage.fromStream(inStream);

		return KeyValueObject.createInstance(msg.toMap());
	}

	@Override
	public void process(long callerId, ByteBuffer message)
			// TODO: check if callerId is relevant
	{
		InputStream stream = null;
		if (message.hasArray())
			stream = new ByteArrayInputStream(message.array());
		else
			stream = new ByteBufferBackedInputStream(message);

		process(stream);
	}

	@Override
	public void process(InputStream stream)
	{
		try
		{
			N2KMessage msg = N2KMessage.fromStream(new LittleEndianDataInputStream(stream));
			getTransfer().transfer(KeyValueObject.createInstance(msg.toMap()));
		} catch (IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException
	{
		N2KMessage msg = N2KMessage.fromMap(object.getAsKeyValueMap());
		msg.toStream(outStream);
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler)
	{
		return new NMEA2000ProtocolHandler(direction, access, dataHandler, options);
	}

	@Override
	public String getName() { return NAME; }

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o)
	{
		if (o instanceof NMEA2000ProtocolHandler)
			return ((NMEA2000ProtocolHandler) o).delay == delay;
		else
			return false;
	}
}
