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
package de.uniol.inf.is.odysseus.wrapper.fnirs.physicaloperator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class AppendFileTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(AppendFileTransportHandler.class);
	
	public static final String NAME = "AppendFile";

	public String fileName;
	public long checkDelay;
	public double lineDelay;
	
	private BufferedReader currentStream = null;
	private String currentLine = null;
	private long currentLineNumber;
	private double currentTimeStamp; // All timestamps in ms
	
	
	public AppendFileTransportHandler() 
	{
		super();
	}

	public AppendFileTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		fileName = options.get("filename");
		
		checkDelay = options.getLong("checkdelay", 250);
		
		double delay = options.getDouble("linedelay", 0.0);
		double freq  = options.getDouble("linefreq", 0.0);
		
		if (delay == 0.0 && freq == 0.0) throw new IllegalArgumentException("Neither lineFreq nor lineDelay specified in options");
		if (delay != 0.0 && freq != 0.0) throw new IllegalArgumentException("Cannot specify both lineFreq and lineDelay in options");
		
		lineDelay = (delay != 0.0) ? (delay) : (1000.0 / freq);
	}


	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new AppendFileTransportHandler(protocolHandler, options);
	}	
	
    @Override
    public void processInOpen() throws IOException 
    {
    	currentLine = null;
    	currentLineNumber = 1;
    }

    @Override
    public void processOutOpen() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processInClose() throws IOException 
    {
    	currentLine = null;
    	
    	if (currentStream != null)
    	{
    		try
    		{
    			currentStream.close();    			
    		}
    		catch (IOException e)
    		{
    			e.printStackTrace();
    		}
    		
    		currentStream = null;
    	}
    }

    @Override
    public void processOutClose() throws IOException 
    {
        throw new UnsupportedOperationException();
    }
		
	@Override public Tuple<IMetaAttribute> getNext() 
	{
//		System.out.println("Call getNext");
		
		if (currentLine == null)
			return null;
				
		Tuple<IMetaAttribute> tuple = new Tuple<>(4, false);		
		tuple.setAttribute(0, (long)(currentTimeStamp));
		tuple.setAttribute(1, (long)(currentTimeStamp+lineDelay));
		tuple.setAttribute(2, currentLineNumber);
		tuple.setAttribute(3, new String(currentLine)); 
		
		currentTimeStamp += lineDelay;
		currentLineNumber++;
		
        return tuple;		
	}
    
	private boolean checkNext()
	{
		if (currentStream == null)
		{
			try 
			{
				currentStream = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
				BasicFileAttributes attr = Files.readAttributes(Paths.get(fileName), BasicFileAttributes.class);

				System.out.println("creationTime: " + attr.creationTime());				
												
				currentTimeStamp = attr.creationTime().toMillis();
			} 
	    	catch (Exception e)
	    	{
	    		return false;
	    	}
		}
			
		try
		{
    		currentLine = currentStream.readLine();    	
    		return currentLine != null && !currentLine.equals("");
    	}
    	catch (Exception e)
    	{
   			throw new RuntimeException(e);
    	}		
	}
	
	@Override public boolean hasNext() 
	{
//		System.out.println("Call hasNext");		
   		if (checkNext())
   		{   			   		
//   			System.out.println("Call hasNext returned true");   			
   			return true;
   		}
   		else
   		{
	    	try 
	    	{
				Thread.sleep(checkDelay);
			} 
	    	catch (InterruptedException e) 
	    	{
	    		Thread.currentThread().interrupt();
			}	         
	    	
//	    	System.out.println("Call hasNext returned false");
	    	return false;
   		}
	}	

	@Override
	public String getName() { return NAME; }

    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof AppendFileTransportHandler)) 
    		return false;
    	
    	AppendFileTransportHandler other = (AppendFileTransportHandler)o;
    	if(!fileName.equals(other.fileName)) 
    		return false;
    	if(checkDelay != other.checkDelay) 
    		return false;
    	if(lineDelay != other.lineDelay) 
    		return false;
    	    	
    	return true;
    }
    
    @Override public void send(final byte[] message) throws IOException 
    {
        throw new UnsupportedOperationException();
    }	
}