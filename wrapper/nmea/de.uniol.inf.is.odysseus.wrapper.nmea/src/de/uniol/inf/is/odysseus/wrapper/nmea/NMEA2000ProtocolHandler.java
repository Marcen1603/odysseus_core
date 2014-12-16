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

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * This wrapper can be used as a ProtocolHandler (NMEA 2000) with GenericPush or
 * GenericPull. Still WIP
 * 
 * @author Henrik Surm <henrik.surm@uni-oldenburg.de>
 * 
 */

class N2KMessage
{
	public long timeStamp;
	public long PGN;
	public int priority;
	public int sourceAddress;
	public int destinationAddress;
	public long length;		
	public byte[] payload;

	public Map<String, Object> headerToMap()
	{
		Map<String, Object> result = new HashMap<>();
		result.put("Timestamp", timeStamp);
		result.put("PGN", PGN);
		result.put("Priority", priority);
		result.put("SourceAddress", sourceAddress);
		result.put("DestinationAddress", destinationAddress);
//		result.put("Length", length);
		
		return result;
	}
	
	public static N2KMessage fromStream(LittleEndianDataInputStream inStream) throws IOException
	{
		N2KMessage result = new N2KMessage();
		
		// Read NMEA2K header
		result.timeStamp = readUnsignedInt(inStream);
		result.PGN = readUnsignedInt(inStream);
		result.priority = inStream.readUnsignedByte();
		result.sourceAddress = inStream.readUnsignedByte();
		result.destinationAddress = inStream.readUnsignedByte();
		result.length = readUnsignedInt(inStream);
		
		// Read payload
		result.payload = new byte[(int)result.length];
		if (inStream.read(result.payload) != result.length) throw new IOException("Not enough input data for payload");
		
		return result;
	}

	
	
	private static long readUnsignedInt(LittleEndianDataInputStream inStream) throws IOException 
	{
		return inStream.readInt() & 0xffffffffL;
	}	
};

public class NMEA2000ProtocolHandler extends AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> 
{
	private final Logger LOG = LoggerFactory.getLogger(NMEA2000ProtocolHandler.class);
	
	/** Input stream as BufferedReader (Only in GenericPull). */
	private LittleEndianDataInputStream inStream; 
	
	/** Delay on GenericPull. */
	private long delay = 0;
	
	public NMEA2000ProtocolHandler() 
	{
	}

	public NMEA2000ProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler, OptionMap optionsMap) 
	{
		super(direction, access, dataHandler, optionsMap);
		delay = optionsMap.getInt("delay", 0);
	}

	@Override
	public void open() throws UnknownHostException, IOException 
	{
		getTransportHandler().open();
		if (getDirection().equals(ITransportDirection.IN)) 
		{
			if (getAccessPattern().equals(IAccessPattern.PULL) || getAccessPattern().equals(IAccessPattern.ROBUST_PULL))
			{
				inStream = new LittleEndianDataInputStream(getTransportHandler().getInputStream());
			}
		} else {
			// TODO: Implement output NMEA
			// this.output = this.getTransportHandler().getOutputStream();
		}
	}

	@Override
	public void close() throws IOException 
	{
		try 
		{
			if (inStream != null) 
			{
				inStream.close();
			}
		} catch (Exception e) 
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
		
		return new KeyValueObject<>(parseMessage(msg));
	}

	@Override
	public void process(ByteBuffer message) 
	{
		InputStream stream = null;
		if (message.hasArray())
			stream = new ByteArrayInputStream(message.array());
		else
		{
			// TODO: Implement a ByteBufferInputStream
		}
		
		process(stream);
	}
	
	@Override
	public void process(InputStream stream)
	{
		try 
		{
			N2KMessage msg = N2KMessage.fromStream(new LittleEndianDataInputStream(stream));
			getTransfer().transfer(new KeyValueObject<>(parseMessage(msg)));
		} catch (IOException e) 
		{
			LOG.error("Error while processing NMEA2K message", e);
		}
		
		try 
		{
			stream.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private Map<String, Object> parseMessage(N2KMessage msg) throws IOException 
	{		
		Map<String, Object> msgMap = msg.headerToMap();
		
		LittleEndianDataInputStream payloadStream = new LittleEndianDataInputStream(new ByteArrayInputStream(msg.payload));
		
		if (msg.PGN == 129025)
		{
			int lon = payloadStream.readInt();
			int lat = payloadStream.readInt();
			
			msgMap.put("Longitude", (double)(lon) * 1.0e-7);
			msgMap.put("Latitude",  (double)(lat) * 1.0e-7);
		}
		
		payloadStream.close();
		
		return msgMap;
	}
	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException 
	{
		// TODO
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
																					 IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) 
	{
		return new NMEA2000ProtocolHandler(direction, access, dataHandler, options);
	}

	@Override
	public String getName() { return "NMEA2000"; }

	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof NMEA2000ProtocolHandler)) {
			return false;
		} else {
			// the datahandler was already checked in the
			// isSemanticallyEqual-Method of AbstractProtocolHandler
			return true;
		}
	}
}
