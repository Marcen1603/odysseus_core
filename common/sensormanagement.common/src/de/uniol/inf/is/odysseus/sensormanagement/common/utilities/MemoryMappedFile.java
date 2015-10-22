package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemoryMappedFile 
{
	public enum Direction
	{
		READ,
		WRITE
	}
	
	private int size;
	private String filename;
	private Direction direction;
	
	private RandomAccessFile memoryMappedFile;
	private MappedByteBuffer mapBuffer;
	private Thread thread;
	
	final public Callback<MemoryMappedFile, ByteBuffer> callback = new Callback<>();
	
	public MemoryMappedFile(String filename, int size, Direction direction) throws IOException 
	{
		this.filename = filename;
		this.size = size;
		
		createFile();
		
		if (direction == Direction.READ)
			startThread();
	}
	
    private void startThread() {
    	thread = new Thread()
    	{
    		@Override public void run()
    		{
    			while (isAlive())
    			{
    				MemoryMappedFile.this.process();
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
    			callback.raise(this, message);
    			
    			mapBuffer.putInt(0, 0);
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
	}

	private void stopThread() 
	{
		try {
			thread.join(1000);
		} catch (InterruptedException e) {
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
    
	public void close() throws IOException
	{
		if (direction == Direction.READ)
			stopThread();
		
		memoryMappedFile.close();
	}

	public void write(ByteBuffer message) throws IOException 
	{
		while (mapBuffer.getInt(0) != 0)
		{			
		}
		
		mapBuffer.rewind();
		mapBuffer.putInt(message.remaining());
		mapBuffer.put(message);
	}	
	
	public void write(byte[] message) throws IOException 
	{
		while (mapBuffer.getInt(0) != 0)
		{			
		}
		
		mapBuffer.rewind();
		mapBuffer.putInt(message.length);
		mapBuffer.put(message);
	}	
}
