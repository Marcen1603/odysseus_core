
package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.cameras.KeyValueFile;

public class LMSLoggerProtocolHandler extends LoggerProtocolHandler 
{
	public static final String NAME = "LMSLogger";
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(LMSLoggerProtocolHandler.class);

	private String			rawFileName;
	private BufferedWriter rawFileStream;

	public LMSLoggerProtocolHandler() 
	{
		super();
	}

	public LMSLoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) throws IOException 
	{
		super(direction, access, dataHandler, options);
	}
	
	@Override public void open() throws UnknownHostException, IOException 
	{
		super.open();
	}
	
	@Override protected void startLoggingInternal(KeyValueFile logConfigFile, Tuple<?> object) throws IOException 
	{
		rawFileName = getFileNameBase() + ".raw";
		rawFileStream = new BufferedWriter(new FileWriter(rawFileName));
		logConfigFile.set("RawFile", new File(rawFileName).getName());
	}

	@Override protected void stopLoggingInternal(KeyValueFile logConfigFile) 
	{
		if (rawFileStream != null)
		{
			try
			{
				rawFileStream.close();
			}
			catch (IOException e) 
			{
			}
	
			rawFileStream = null;
		}		
	}

	@Override protected long writeInternal(Tuple<?> object, long timeStamp) throws IOException 
	{
		String rawString = (String) object.getAttribute(0);

		System.out.println("write " + rawString.substring(0,  40));
		
		try
		{
			rawFileStream.write(rawString + "\n");
		}		
		catch (Exception e)
		{
			throw new IOException(e);
		}
		
		long length = new File(rawFileName).length();
		System.out.println("Raw file size = " + length);
		
		return length;
	}	
	
	@Override
	public void close() throws IOException 
	{
		super.close();
	}

	@Override
	public boolean hasNext() throws IOException 
	{
		return false;
	}

	@Override
	public Tuple<?> getNext() throws IOException 
	{
		return null;
	}

	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction,
													 IAccessPattern access, OptionMap options,
													 IDataHandler<Tuple<?>> dataHandler) 
	{
		try 
		{
			return new LMSLoggerProtocolHandler(direction, access, dataHandler, options);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override public boolean isDone() { return true; }
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof LMSLoggerProtocolHandler)) {
			return false;
		}
/*		VideoLogger other = (VideoLogger) o;
		if (this.nanodelay != other.getNanodelay()
				|| this.delay != other.getDelay()
				|| this.delayeach != other.getDelayeach()
				|| this.dumpEachLine != other.getDumpEachLine()
				|| this.measureEachLine != other.getMeasureEachLine()
				|| this.lastLine != other.getLastLine()
				|| this.debug != other.isDebug()
				|| this.readFirstLine != other.isReadFirstLine()) {
			return false;
		}*/
		return true;
	}
}
