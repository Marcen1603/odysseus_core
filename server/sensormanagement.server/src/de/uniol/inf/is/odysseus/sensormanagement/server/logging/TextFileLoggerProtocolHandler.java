
package de.uniol.inf.is.odysseus.sensormanagement.server.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.TextLogMetaData;

public class TextFileLoggerProtocolHandler extends LoggerProtocolHandler
{
	public static final String NAME = "TextFileLogger";
	static final Runtime RUNTIME = Runtime.getRuntime();
	static final WriteOptions writeOptions = WriteOptions.defaultOptions2;

	Logger LOG = LoggerFactory.getLogger(TextFileLoggerProtocolHandler.class);

	private String			logFileName;
	private BufferedWriter 	logFileStream;
	private String 			extension;

	// Return that no attributes will be left
	@Override protected int[] getRemainingAttributes() { return new int[0]; }

	public TextFileLoggerProtocolHandler()
	{
		super();
	}

	public TextFileLoggerProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<Tuple<?>> dataHandler, OptionMap options)
	{
		super(direction, access, dataHandler, options);

		extension = options.get("extension", "raw");
	}

	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IStreamObjectDataHandler<Tuple<?>> dataHandler)
	{
		return new TextFileLoggerProtocolHandler(direction, access, dataHandler, options);
	}

	@Override protected LogMetaData startLoggingInternal(Tuple<?> object) throws IOException
	{
		logFileName = getFileNameBase() + "." + extension;
		logFileStream = new BufferedWriter(new FileWriter(logFileName));

		TextLogMetaData logMetaData = new TextLogMetaData();
		logMetaData.rawFile = new File(logFileName).getName();

		return logMetaData;
	}

	@Override protected void stopLoggingInternal(LogMetaData logMetaData)
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

	@Override protected void writeInternal(Tuple<?> object, long timeStamp) throws IOException
	{
		try
		{
			StringBuilder rawString = new StringBuilder();
			getDataHandler().writeCSVData(rawString, object, writeOptions);
			logFileStream.write(rawString.toString() + "\n");
		}
		catch (Exception e)
		{
			throw new IOException(e);
		}
	}

	@Override protected long getLogFileSize() {
		return new File(logFileName).length();
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
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override public boolean isDone() { return true; }

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o)
	{
		if (!(o instanceof TextFileLoggerProtocolHandler)) return false;
		if (!super.isSemanticallyEqualImpl(o)) return false;

		TextFileLoggerProtocolHandler other = (TextFileLoggerProtocolHandler) o;
		if (extension.equals(other.extension)) return false;

		return true;
	}
}
