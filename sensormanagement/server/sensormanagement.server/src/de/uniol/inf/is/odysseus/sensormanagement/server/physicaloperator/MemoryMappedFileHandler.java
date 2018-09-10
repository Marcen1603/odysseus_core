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
package de.uniol.inf.is.odysseus.sensormanagement.server.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.MemoryMappedFile;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.MemoryMappedFile.Direction;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleCallbackListener;

public class MemoryMappedFileHandler extends AbstractPushTransportHandler 
{
	public static final String NAME = "MemoryMappedFile";	
	public static final String FILENAME = "filename";
	public static final String SIZE = "size";

	private int size;
	private String filename;
	
	private MemoryMappedFile mmFile;
	
	@Override public String getName() { return NAME; }
	
	public MemoryMappedFileHandler() 
	{
		super();
	}

	public MemoryMappedFileHandler(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		if ((protocolHandler.getAccessPattern() == IAccessPattern.ROBUST_PULL) || (protocolHandler.getAccessPattern() == IAccessPattern.PULL))
			throw new PlanManagementException(NAME + " can only be used with push access pattern!");
		
		options.checkRequiredException(SIZE);
		size = options.getInt(SIZE, 0);
		filename = options.get(FILENAME, null);
	}

	@Override
	public void processInOpen() throws IOException 
	{		
		mmFile = new MemoryMappedFile(filename, size, Direction.READ);
		mmFile.callback.addListener(new SimpleCallbackListener<MemoryMappedFile, ByteBuffer>() 
		{
			@Override public void raise(MemoryMappedFile sender, ByteBuffer event) 
			{
				fireProcess(event);
			}
		});
		fireOnConnect();
	}
	
	@Override public void processInClose() throws IOException
	{
		mmFile.close();
		mmFile = null;
		fireOnDisconnect();
	}

	@Override
	public synchronized void processOutOpen() throws IOException 
	{
		mmFile = new MemoryMappedFile(filename, size, Direction.WRITE);
		fireOnConnect();
	}
	
	@Override public void send(final byte[] message) throws IOException 
	{
		mmFile.write(message);
	}	

	@Override public void processOutClose() throws IOException
	{
		mmFile.close();
		mmFile = null;		
		fireOnDisconnect();
	}

	@Override public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new MemoryMappedFileHandler(protocolHandler, options);
	}	

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) 
	{
		if (!(o instanceof MemoryMappedFileHandler)) return false;
		if (!super.isSemanticallyEqual(o)) return false;

		MemoryMappedFileHandler other = (MemoryMappedFileHandler) o;
		if (size != other.size) return false;
		if (!filename.equals(other.filename)) return false;

		return true;
	}
}
