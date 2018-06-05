
package de.uniol.inf.is.odysseus.video.physicaloperator;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.video.AbstractVideoImplementation;

public abstract class AbstractVideoStreamProtocolHandler extends AbstractProtocolHandler<Tuple<?>>
{
	public static final String USEDELAY = "usedelay";
	public static final String STREAMURL = "streamurl";
	public static final String TIMESTAMPMODE = "timestampmode";
	public static final String TIMESTAMPMODE_NONE = "none";
	public static final String TIMESTAMPMODE_SYNCFILE = "syncfile";
	public static final String TIMESTAMPMODE_FILETIME = "filetime";
	public static final String TIMESTAMPMODE_START = "start";

	private enum TimeStampMode
	{
		start, fileTime, syncFile, none
	};

	private static final Logger LOG = LoggerFactory.getLogger(AbstractVideoStreamProtocolHandler.class);

	private final Object processLock = new Object();

	private AbstractVideoImplementation videoImpl;

	private String streamUrl;
	private boolean useDelay;

	private TimeStampMode timeStampMode;
	private long startTime;			// Time stamp for first frame
	private String syncFileName = null;

	private LinkedList<Tuple<IMetaAttribute>> tupleQueue = new LinkedList<>();

	private boolean isDone;
	private double currentTime; // Current frame timestamp in seconds
	private double currentFrameSystemTime; // System timestamp for current frame

	public AbstractVideoStreamProtocolHandler()
	{
		super();
	}

	public AbstractVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<Tuple<?>> dataHandler,
												OptionMap optionMap)
	{
		super(direction, access, dataHandler, optionMap);

		optionMap.checkRequiredException(STREAMURL);

		videoImpl = new AbstractVideoImplementation()
		{
			@Override public FrameRecorder createRecorder(ImageJCV image)
			{
				LOG.info("Start streaming to " + streamUrl + ", w = " + image.getWidth() + ", h = " + image.getHeight());
				FrameRecorder recorder = AbstractVideoStreamProtocolHandler.this.createRecorder(image);
				LOG.info("Streaming to " + streamUrl + " is running");

				return recorder;
			}

			@Override public FrameGrabber createGrabber()
			{
				LOG.info("Starting grabber for " + streamUrl);
				FrameGrabber grabber = AbstractVideoStreamProtocolHandler.this.createGrabber();
				LOG.info("Grabber for " + streamUrl + " started!");

				return grabber;
			}

			@Override public void onRecorderStarted() {}

			@Override public void onGrabberStarted()
			{
				if (frameRate == 0.0)
				{
					if (timeStampMode == TimeStampMode.start || timeStampMode == TimeStampMode.fileTime)
						throw new IllegalArgumentException("Video source doesn't provide frame rate. Can't use \"start\" or \"fileTime\" as timeStampMode!");
					if (useDelay)
						throw new IllegalArgumentException("Video source doesn't provide frame rate. Can't use \"useDelay\", since frame length is unknown!");
				}
			}

		};
		videoImpl.getOptions(optionMap);

		streamUrl = optionMap.get(STREAMURL);
		useDelay = optionMap.getBoolean(USEDELAY,  false);
		String timeStampModeStr = optionMap.get(TIMESTAMPMODE, TIMESTAMPMODE_START);

			 if (timeStampModeStr.equals(TIMESTAMPMODE_START))		timeStampMode = TimeStampMode.start;
		else if (timeStampModeStr.equals(TIMESTAMPMODE_FILETIME))	timeStampMode = TimeStampMode.fileTime;
		else if (timeStampModeStr.equals(TIMESTAMPMODE_SYNCFILE))	timeStampMode = TimeStampMode.syncFile;
		else if (timeStampModeStr.equals(TIMESTAMPMODE_NONE))		timeStampMode = TimeStampMode.none;
		else
			throw new IllegalArgumentException("timeStampMode has an invalid value: " + timeStampModeStr);
	}

	@Override public void open() throws UnknownHostException, IOException
	{
		videoImpl.open();

		isDone = false;

		if (getDirection().equals(ITransportDirection.IN))
		{
			if (getAccessPattern().equals(IAccessPattern.PUSH) || getAccessPattern().equals(IAccessPattern.ROBUST_PUSH))
				throw new UnsupportedOperationException(getName() + " cannot be run in Push mode!");

			switch (timeStampMode)
			{
			case start:
				// Frame timestamps will be extrapolated from beginning time stamp specified in the options
				// If no timestamp is given, system time will be used
				// curTime = startTime + frameNum/fps
				startTime = getOptionsMap().getLong("starttime", -1);
				break;

			case fileTime:
				// Frame timestamps will be extrapolated from the file last modified time stamp
				// curTime = startTime + frameNum/fps
				startTime = new File(streamUrl).lastModified();

				if (startTime == 0)
					throw new IOException("Could not read last modified timestamp from \"" + streamUrl + "\"");
				break;

			case syncFile:
				// Frame timestamps will be read from synchronization file
				syncFileName = getOptionsMap().get("syncfilename");
				// TODO: Read first timestamp
				throw new UnsupportedOperationException("Timestamp mode \"syncfile\" not yet implemented for reading!");
//				break;

			case none:
				break;
			default:
				break;
			}

			if (startTime == -1)
				startTime = System.currentTimeMillis();

			currentTime = startTime / 1000.0;
		}
	}

	@Override
	public void close() throws IOException
	{
		synchronized (processLock)
		{
			videoImpl.stop();
			isDone = true;
		}
	}

	public abstract FrameRecorder createRecorder(ImageJCV image);

	@Override
	public void write(Tuple<?> object) throws IOException
	{
		synchronized (processLock)
		{
			long timeStamp = 0;
			TimeInterval timeInterval = (TimeInterval)object.getMetadata();
	        if (timeInterval != null)
	        	timeStamp = timeInterval.getStart().getMainPoint();

			ImageJCV image = (ImageJCV) object.getAttribute(0);
			videoImpl.record(image, timeStamp / 1000.0);
			LOG.debug("Frame written: " + image);
		}
	}

	public abstract FrameGrabber createGrabber();


	@Override
	public boolean hasNext() throws IOException
	{
		synchronized (processLock)
		{
			if (isDone) return true;

			try
			{
				ImageJCV receivedImage = videoImpl.grab();
				if (receivedImage == null)
				{
					isDone = true;
					return false;
				}

				LOG.debug("Frame received at " + System.currentTimeMillis());

				// Create result tuple and image, if not created yet
				Tuple<IMetaAttribute> currentTuple = new Tuple<>(getSchema().size(), false);
				int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
				if (attrs.length > 0)
				{
					currentTuple.setAttribute(attrs[0], receivedImage);
				}

				// Set timestamps
				if (timeStampMode != TimeStampMode.none)
				{
					long startTimeStamp = (long) (currentTime * 1000.0);

			        if (syncFileName != null)
			        {
				        	// TODO
			//        	currentTime = syncFileStream.readDouble();
			        }
			        else
			        {
			        	currentTime += 1.0 / videoImpl.frameRate;
			        }

			        long endTimeStamp = (long) (currentTime * 1000.0);

					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.START_TIMESTAMP);
					if (attrs.length > 0)
						currentTuple.setAttribute(attrs[0], startTimeStamp);

					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.END_TIMESTAMP);
					if (attrs.length > 0)
						currentTuple.setAttribute(attrs[0], endTimeStamp);
				}

				currentFrameSystemTime = System.currentTimeMillis() / 1000.0;

				tupleQueue.addLast(currentTuple);

				return true;
			}
			catch (Exception e)
			{
				isDone = true;
				throw new IOException(e);
			}
		}
	}

	@Override
	public Tuple<?> getNext() throws IOException
	{
		synchronized (processLock)
		{
			if (isDone)
				return null;

			// Apply delay if necessary
			if (useDelay)
				try
				{
					double frameTime = 1.0 / videoImpl.frameRate;
					while (System.currentTimeMillis() / 1000.0 - currentFrameSystemTime < frameTime)
						Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					Thread.currentThread().interrupt();
					return null;
				}

	        return tupleQueue.pop();
		}
	}

	@Override
	public ITransportExchangePattern getExchangePattern() {
		if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
			return ITransportExchangePattern.InOnly;
		} else {
			return ITransportExchangePattern.OutOnly;
		}
	}

	@Override public boolean isDone()
	{
		return isDone;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o)
	{
		if (!(o instanceof AbstractVideoStreamProtocolHandler))
			return false;

		AbstractVideoStreamProtocolHandler other = (AbstractVideoStreamProtocolHandler) o;
		if (!streamUrl.equals(other.streamUrl) || !videoImpl.equals(other.videoImpl))
			return false;

		return true;
	}

	public String getStreamUrl()
	{
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl)
	{
		throw new UnsupportedOperationException("Not implemented yet!");
//		this.streamUrl = streamUrl;
	}

	public double getFrameRate()
	{
		return videoImpl.frameRate;
	}

	public void setFrameRate(double frameRate)
	{
		throw new UnsupportedOperationException("Not implemented yet!");
//		this.frameRate = frameRate;
	}
}
