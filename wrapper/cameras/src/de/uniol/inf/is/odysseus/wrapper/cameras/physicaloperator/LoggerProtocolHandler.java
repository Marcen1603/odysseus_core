package de.uniol.inf.is.odysseus.wrapper.cameras.physicaloperator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.wrapper.cameras.KeyValueFile;

public abstract class LoggerProtocolHandler extends AbstractProtocolHandler<Tuple<?>>
{
	private boolean 		logSetUp;
	private KeyValueFile 	logConfigFile;
	private long 			lastTimeStamp;
	
	private String 			sensorName;
	private String 			sensorPosition;
	private String 			sensorEthernetAddr;
	private String 			sensorType;
	private String 			filePath;	
	private String			fileNameBase;
	private long			logfileSizeLimit;
	
	public LoggerProtocolHandler() 
	{
		super();
	}	
	
	public LoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) throws IOException 
	{
		super(direction, access, dataHandler, options);
		
		sensorName 			= options.get("sensorname");
		sensorPosition 		= options.get("sensorposition");
		sensorEthernetAddr	= options.get("sensorethernetaddr");
		sensorType			= options.get("sensortype");		
		
		if (sensorName 			== null) throw new IOException("Parameter not specified in Options: sensorName");
		if (sensorPosition 		== null) throw new IOException("Parameter not specified in Options: sensorPosition");
		if (sensorEthernetAddr 	== null) throw new IOException("Parameter not specified in Options: sensorEthernetAddr");
		if (sensorType 			== null) throw new IOException("Parameter not specified in Options: sensorType");
		
		filePath  			= options.get("filename", "");
		logfileSizeLimit	= options.getLong("sizelimit", Long.MAX_VALUE);		
	}
	
	public String getFileNameBase() 	{ return fileNameBase; }
	public long   getLogfileSizeLimit() { return logfileSizeLimit; }

	@Override
	public void open() throws IOException
	{
		logSetUp = false;
		lastTimeStamp = 0;
	}
	
	@Override
	public void close() throws IOException 
	{
		if (logSetUp)
		{
			stopLogging(lastTimeStamp);
			logSetUp = false;
		}
	}
	
	public void startLogging(Tuple<?> object) throws IOException
	{
		if (logSetUp) return;		
			
        TimeInterval timeStamp = (TimeInterval)object.getMetadata();
        long now = timeStamp.getStart().getMainPoint();
        
        String hostName = sensorEthernetAddr.replace(':', '_');        
        
        String startTimeString = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date(now));
        fileNameBase = filePath + "/" + hostName + "_" + sensorType + "_" + startTimeString;
        String logFileName = fileNameBase + ".cfg";		
		
		try
		{
			logConfigFile = new KeyValueFile(logFileName, false);
			logConfigFile.set("Name", 		 	sensorName);
			logConfigFile.set("Position", 	 	sensorPosition);
			logConfigFile.set("EthernetAddr", 	sensorEthernetAddr);
			logConfigFile.set("Type", 		 	sensorType);
			
			logConfigFile.set("StartTime", Long.toString(now));
			logConfigFile.set("EndTime", "0");
			
			startLoggingInternal(logConfigFile, object);
			logConfigFile.save();
		}
		catch (IOException e)
		{
			logConfigFile = null;
			stopLoggingInternal(null);
			throw e;
		}
		
		logSetUp = true;
	}
	
	public void stopLogging(long lastTimeStamp)
	{
		if (!logSetUp) return;
		logSetUp = false;
		
		stopLoggingInternal(logConfigFile);
		
		try
		{
			logConfigFile.set("EndTime" , Long.toString(lastTimeStamp));
			logConfigFile.save();			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			logConfigFile = null;
		}			
	}
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		if (!logSetUp)
			startLogging(object);
		
        TimeInterval timeStamp = (TimeInterval)object.getMetadata();
        lastTimeStamp = timeStamp.getStart().getMainPoint();		
		
        boolean logFileSizeLimitReached = writeInternal(object, lastTimeStamp) >= getLogfileSizeLimit();
		
		if (logFileSizeLimitReached)
			stopLogging(lastTimeStamp);
	}
	
	protected abstract void 	startLoggingInternal(KeyValueFile logConfigFile, Tuple<?> object) throws IOException;
	protected abstract void 	stopLoggingInternal(KeyValueFile logConfigFile);
	protected abstract long 	writeInternal(Tuple<?> object, long timeStamp) throws IOException;
}
