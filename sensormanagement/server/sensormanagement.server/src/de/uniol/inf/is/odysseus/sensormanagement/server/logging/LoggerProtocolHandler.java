package de.uniol.inf.is.odysseus.sensormanagement.server.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.AbstractLoggingStyle;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.AbstractLoggingStyle.ProcessResult;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LoggingStyleProvider;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.StreamObjectUtilities;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;

public abstract class LoggerProtocolHandler extends AbstractProtocolHandler<Tuple<?>>
{
	private Object processLock = new Object();
	
	private long lastTimeStamp;

	private String loggingDirectory;
	private String fileNameBase;
	private SensorModel	sensorModel;
	private String loggingStyleName;
	private AbstractLoggingStyle loggingStyle;
	
	private boolean logSetUp;	
	private LogMetaData logMetaData;
	private String logMetaDataFileName;
	
	private int[] remainingAttributes;
	private BufferedWriter additionalFileWriter;
	private WriteOptions csvWriteOptions = WriteOptions.defaultOptions2;
	
	public String getFileNameBase() { return fileNameBase; }
	public AbstractLoggingStyle getLoggingStyle() { return loggingStyle; }
	public SensorModel getSensorModel() { return sensorModel; }
	
	// Returns the current log file size in bytes 
	protected abstract long getLogFileSize();
	protected abstract LogMetaData startLoggingInternal(Tuple<?> object) throws IOException;
	protected abstract void stopLoggingInternal(LogMetaData logMetaData);
	protected abstract void writeInternal(Tuple<?> object, long timeStamp) throws IOException;
	
	// Returns tuple attribute positions which will not been written to the log file
	protected abstract int[] getRemainingAttributes();	
	
	public LoggerProtocolHandler() 
	{
		super();
	}	
	
	public LoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
		
		options.checkRequiredException("sensorxml");
		options.checkRequiredException("directory");
		
		sensorModel = XmlMarshalHelper.fromXml(options.get("sensorxml"), SensorModel.class);
		
		loggingDirectory = options.get("directory");
		loggingStyleName = options.get("loggingStyle", "Default");
	}
	
	@Override
	public final void open() throws IOException
	{
		synchronized(processLock)
		{
			logSetUp = false;
			lastTimeStamp = 0;
			loggingStyle = LoggingStyleProvider.createLoggingStyle(sensorModel.getLoggingStyle(loggingStyleName));
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
			loggingStyle = null;
		}
	}
	
	private void startLogging(Tuple<?> object, long firstTimeStamp) throws IOException
	{
		if (logSetUp) return;		
			        
        String startTimeString = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date(firstTimeStamp));
        fileNameBase = loggingDirectory + "/" + sensorModel.id + "_" + loggingStyleName + "_" + startTimeString;
        logMetaDataFileName = fileNameBase + ".cfg";		
		
		try
		{
			logMetaData = startLoggingInternal(object);
			logMetaData.startTime = firstTimeStamp;
			logMetaData.endTime = 0;
			logMetaData.sensorId = sensorModel.id;
			logMetaData.loggingStyle = loggingStyleName;
			
			remainingAttributes = getRemainingAttributes();
			if (remainingAttributes.length > 0)
			{
				// Create additional data file
				String additionalFileName = getFileNameBase() + ".csv";
					
				logMetaData.additionalData = new LogMetaData.AdditionalData();
				logMetaData.additionalData.fileName = new File(additionalFileName).getName();					
				additionalFileWriter = new BufferedWriter(new FileWriter(additionalFileName));
					
				// Collect attribute names for additional header in log meta data
				logMetaData.additionalData.header = "";					
				for (int attr : remainingAttributes)
					logMetaData.additionalData.header += getSchema().getAttribute(attr).getAttributeName() + ", ";
					
				logMetaData.additionalData.header = logMetaData.additionalData.header.substring(0, logMetaData.additionalData.header.length()-2);
			}
			
			
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
		
		if (additionalFileWriter != null) {
			try {
				additionalFileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			logMetaData.endTime = lastTimeStamp;
			XmlMarshalHelper.toXmlFile(logMetaData, new File(logMetaDataFileName));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		logMetaData = null;
		additionalFileWriter = null;
	}
	
	@Override
	public void write(Tuple<?> object) throws IOException 
	{
		synchronized (processLock)
		{
			lastTimeStamp = StreamObjectUtilities.getTimeStamp(object);
			
			ProcessResult result = loggingStyle.process(object);
			if (result.log)
			{
				if (!logSetUp)
					startLogging(object, lastTimeStamp);				
				
				System.out.println("Log " + loggingStyleName + " " + lastTimeStamp);
				writeInternal(object, lastTimeStamp);
				if (remainingAttributes.length > 0)
				{
					Tuple<?> remainingTuple = object.restrict(remainingAttributes, true);
					StringBuilder rawString = new StringBuilder();
					getDataHandler().writeCSVData(rawString, remainingTuple, csvWriteOptions);
					additionalFileWriter.write(rawString.toString() + "\n");
				}
				
				if (getLogFileSize() >= loggingStyle.sizeLimit || result.splitChunk)
					stopLogging(lastTimeStamp);				
			}			
		}
	}
		
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof LoggerProtocolHandler)) return false;		
		
		LoggerProtocolHandler other = (LoggerProtocolHandler) o;		
		if (!sensorModel.equals(other.sensorModel)) return false;		
		if (!loggingDirectory.equals(other.loggingDirectory)) return false;
		if (!loggingStyleName.equals(other.loggingStyleName)) return false;
	
		return true;
	}	
}
