package de.uniol.inf.is.odysseus.sensormanagement.server.logging;

import java.io.File;
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
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;

public abstract class LoggerProtocolHandler extends AbstractProtocolHandler<Tuple<?>>
{
	private Object			processLock = new Object();
	
	private long 			lastTimeStamp;

	private String 			loggingDirectory;
	private String			fileNameBase;
	private long			logfileSizeLimit;	
	private SensorModel2	sensorModel;
	
	private boolean 		logSetUp;	
	private LogMetaData 	logMetaData;
	private String			logMetaDataFileName;
	
	public LoggerProtocolHandler() 
	{
		super();
	}	
	
	public LoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
		
		options.checkRequiredException("sensorxml");
		options.checkRequiredException("directory");
		
		sensorModel = XmlMarshalHelper.fromXml(options.get("sensorxml"), SensorModel2.class);
		
		loggingDirectory = options.get("directory");
		logfileSizeLimit = options.getLong("sizelimit", Long.MAX_VALUE);		
	}
	
	public String getFileNameBase() 	{ return fileNameBase; }
	public long   getLogfileSizeLimit() { return logfileSizeLimit; }

	@Override
	public final void open() throws IOException
	{
		synchronized(processLock)
		{
			logSetUp = false;
			lastTimeStamp = 0;
		}
	}
	
	@Override
	public final void close() throws IOException 
	{
		if (!logSetUp) return;

		synchronized (processLock)
		{
			stopLogging(lastTimeStamp);
			logSetUp = false;
		}
	}
	
	private void startLogging(Tuple<?> object, long firstTimeStamp) throws IOException
	{
		if (logSetUp) return;		
			        
        String startTimeString = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date(firstTimeStamp));
        fileNameBase = loggingDirectory + "/" + sensorModel.id + "_" + startTimeString;
        logMetaDataFileName = fileNameBase + ".cfg";		
		
		try
		{
			logMetaData = startLoggingInternal(object);
			logMetaData.startTime = firstTimeStamp;
			logMetaData.endTime = 0;
			logMetaData.sensor = sensorModel;
			
			XmlMarshalHelper.toXmlFile(logMetaData, new File(logMetaDataFileName));
		}
		catch (IOException e)
		{
			logMetaData = null;
			stopLoggingInternal(null);
			throw e;
		}
		
		logSetUp = true;
	}
	
	private void stopLogging(long lastTimeStamp)
	{
		if (!logSetUp) return;
		logSetUp = false;
		
		stopLoggingInternal(logMetaData);
		
		try
		{
			logMetaData.endTime = lastTimeStamp;
			XmlMarshalHelper.toXmlFile(logMetaData, new File(logMetaDataFileName));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			logMetaData = null;
		}			
	}
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		synchronized (processLock)
		{
	        TimeInterval timeStamp = (TimeInterval)object.getMetadata();
	        if (timeStamp != null)
	        	lastTimeStamp = timeStamp.getStart().getMainPoint();
	        else
	        	lastTimeStamp = System.currentTimeMillis();			
			
			if (!logSetUp)
				startLogging(object, lastTimeStamp);
		
			boolean logFileSizeLimitReached = writeInternal(object, lastTimeStamp) >= getLogfileSizeLimit();
		
			if (logFileSizeLimitReached)
				stopLogging(lastTimeStamp);
		}
	}
	
	protected abstract LogMetaData startLoggingInternal(Tuple<?> object) throws IOException;
	protected abstract void stopLoggingInternal(LogMetaData logMetaData);
	protected abstract long writeInternal(Tuple<?> object, long timeStamp) throws IOException;
}
