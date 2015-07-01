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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Timer;
import java.util.TimerTask;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;

public class MemoryMappedFileHandler extends AbstractPushTransportHandler 
{
	public static final String FILENAME = "filename";
	public static final String NAME = "MemoryMappedFile";
	public static final String SIZE = "size";

	private int size;
	private String filename;
	
	private RandomAccessFile memoryMappedFile;
	private MappedByteBuffer mapBuffer;
	private Timer timer;
	private Thread thread;
	
	public MemoryMappedFileHandler() 
	{
		super();
	}

	public MemoryMappedFileHandler(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		if ((protocolHandler.getAccessPattern() == IAccessPattern.ROBUST_PULL) || (protocolHandler.getAccessPattern() == IAccessPattern.PULL))
			throw new PlanManagementException(NAME + " can only be used with push access pattern!");
		
		options.checkRequiredException(FILENAME);			
		options.checkRequiredException(SIZE);
		size = options.getInt(SIZE, 0);
		filename = options.get(FILENAME);
	}

    private void startTimer() {
    	thread = new Thread()
    	{
    		@Override public void run()
    		{
    			while (isAlive())
    			{
    				MemoryMappedFileHandler.this.process();
    				try {
						sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	};
    	thread.start();
    	
/*        timer = new Timer();
        final TimerTask task = new TimerTask() 
        {
            @Override public void run() 
            {
            	MemoryMappedFileHandler.this.process();
            }
        };        
        timer.schedule(task, 0, 1000);*/    	
    }
    
    protected void process() 
    {
    	try
    	{
    		mapBuffer.rewind();
    		int size = mapBuffer.getInt();
    		if (size != 0)
    		{
    			ByteBuffer message = mapBuffer.slice();
    			message.position(size);
    			MemoryMappedFileHandler.this.fireProcess(message);
    			
    			mapBuffer.putInt(0, 0);
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
	}

	private void stopTimer() {
/*        timer.cancel();
        timer.purge();
        timer = null;*/
		try {
			thread.join(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread = null;
    }	
	
    private void createFile() throws IOException
    {
    	File file = new File(filename); 
    	if (!file.exists() || file.length() < size)
    	{
    		byte[] data = new byte[size];
    		FileOutputStream stream = new FileOutputStream(file);
    		stream.write(data);
    		stream.close();
    	}
    	
		memoryMappedFile = new RandomAccessFile(filename, "rw");
        mapBuffer = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);		    	
    }
    
	@Override
	public void processInOpen() throws IOException 
	{
		createFile();
		fireOnConnect();
		startTimer();
	}
	
	@Override public void processInClose() throws IOException
	{
		stopTimer();
		memoryMappedFile.close();
		fireOnDisconnect();
	}

	@Override
	public synchronized void processOutOpen() throws IOException 
	{
		createFile();
		fireOnConnect();
	}

	@Override public void send(final byte[] message) throws IOException 
	{
		while (mapBuffer.getInt(0) != 0)
		{			
		}
		
		mapBuffer.rewind();
		mapBuffer.putInt(message.length);
		mapBuffer.put(message);
		
		System.out.println("send complete");
	}	

	@Override public void processOutClose() throws IOException
	{
		memoryMappedFile.close();
		fireOnDisconnect();
	}

	@Override public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new MemoryMappedFileHandler(protocolHandler, options);
	}

	@Override
	public String getName() 
	{
		return NAME;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) 
	{
		if (!(o instanceof MemoryMappedFileHandler)) return false;
		if (!super.isSemanticallyEqual(o)) return false;

		MemoryMappedFileHandler other = (MemoryMappedFileHandler) o;
		if (this.size != other.size) return false;

		return true;
	}
}
