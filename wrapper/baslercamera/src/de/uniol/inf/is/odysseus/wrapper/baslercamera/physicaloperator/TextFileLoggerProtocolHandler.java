
package de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import de.uniol.inf.is.odysseus.wrapper.baslercamera.KeyValueFile;

public class TextFileLoggerProtocolHandler extends LoggerProtocolHandler 
{
	public static final String NAME = "TextFileLogger";
	static final Runtime RUNTIME = Runtime.getRuntime();

	Logger LOG = LoggerFactory.getLogger(TextFileLoggerProtocolHandler.class);

	private String			logFileName;
	private BufferedWriter 	logFileStream;
	private String 			extension;

	public TextFileLoggerProtocolHandler() 
	{
		super();
	}

	public TextFileLoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
		
		extension = options.get("extension", "raw");
	}
	
	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IDataHandler<Tuple<?>> dataHandler) 
	{
		return new TextFileLoggerProtocolHandler(direction, access, dataHandler, options);
	}	
	
	@Override protected void startLoggingInternal(KeyValueFile logConfigFile, Tuple<?> object) throws IOException 
	{
		logFileName = getFileNameBase() + "." + extension;
		logFileStream = new BufferedWriter(new FileWriter(logFileName));
		logConfigFile.set("RawFile", new File(logFileName).getName());
	}

	@Override protected void stopLoggingInternal(KeyValueFile logConfigFile) 
	{
		if (logFileStream != null)
		{
			try
			{
				logFileStream.close();
			}
			catch (IOException e) 
			{
			}
	
			logFileStream = null;
		}		
	}

	@Override protected long writeInternal(Tuple<?> object, long timeStamp) throws IOException 
	{
		
		
		String rawString = object.csvToString(',', '\'', null, null, false); //(String) object.getAttribute(0);

/*		if (rawString.length() > 40)
			System.out.println("write " + rawString.substring(0,  40));*/
		
		try
		{
			logFileStream.write(rawString + "\n");
		}		
		catch (Exception e)
		{
			throw new IOException(e);
		}
		
		long length = new File(logFileName).length();
//		System.out.println("Raw file size = " + length);
		
		return length;
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
		if (!(o instanceof TextFileLoggerProtocolHandler)) {
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
