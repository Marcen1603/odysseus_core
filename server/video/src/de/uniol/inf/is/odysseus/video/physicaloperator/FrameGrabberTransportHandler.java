package de.uniol.inf.is.odysseus.video.physicaloperator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

class GrabResult
{
	public IplImage image;
	public Long startTimeStamp;
	public Long endTimeStamp;
}

public abstract class FrameGrabberTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> 
{
	private final Lock processLock = new ReentrantLock();
	
	protected FrameGrabber 	frameGrabber;
	protected Tuple<IMetaAttribute> currentTuple; 
	protected ImageJCV image;
	
	private Thread startupThread;
	protected Exception startupException;
	protected double fps = 0.0;
	
	public FrameGrabberTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);		
	}

	public FrameGrabberTransportHandler() 
	{
		super();
	}
	
	@Override public final Tuple<IMetaAttribute> getNext() 
	{
		Tuple<IMetaAttribute> tuple = currentTuple;
		currentTuple = null;		
        return tuple;
	}
	
	@Override public void processInOpen() throws IOException
	{
		image = null;
		currentTuple = null;		
		frameGrabber = null;

		startupException = null;
		startupThread = new Thread()
		{
			@Override public void run()
			{
				FrameGrabber newFrameGrabber = getFrameGrabber();
				try 
				{
					newFrameGrabber.start();
					if (fps == 0.0)
						fps = newFrameGrabber.getFrameRate();
					FrameGrabberTransportHandler.this.frameGrabber = newFrameGrabber;
				} 
				catch (FrameGrabber.Exception e) 
				{
					startupException = e;
				}
				
				startupThread = null;
			}
		};
		startupThread.start();
		
		fireOnConnect();
	}
	
	@SuppressWarnings("deprecation")
	@Override public void processInClose() throws IOException 
	{
		super.processInClose();
		
		boolean locked = false;
		try 
		{
			if (processLock.tryLock(1, TimeUnit.SECONDS))
				locked = true;
		} 
		catch (InterruptedException e) 
		{
			Thread.currentThread().interrupt();
		}
			
		if (startupThread != null)
		{
			try 
			{
				startupThread.join(1000);
			} 
			catch (InterruptedException e) 
			{
				Thread.currentThread().interrupt();
			}

			startupThread.stop();
			startupThread = null;
		}
			
		if (frameGrabber != null)
		{
			try 
			{
				frameGrabber.stop();
				frameGrabber.release();
			} 
			catch (Exception e)
			{
				throw new IOException(e);
			}
			finally 
			{				
				frameGrabber = null;
			}
		}

		currentTuple = null;
		image = null;		
		
		if (locked)
			processLock.unlock();
		
		fireOnDisconnect();
	}	
	

	@Override public final boolean hasNext()
	{
		synchronized (processLock)
		{
			try 
			{
				if (frameGrabber == null)
				{
					if (startupException == null)
						return false;
					else
						throw new RuntimeException(startupException);
				}				
				
				if (frameGrabber == null) return false;
				GrabResult result = getFrame();
				IplImage iplImage = result.image;				
				if (iplImage == null || iplImage.isNull()) return false;
				
				currentTuple = new Tuple<>(getSchema().size(), false);
				int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
				if (attrs.length > 0)
				{
					if (image == null || (image.getWidth() != iplImage.width()) || (image.getHeight() != iplImage.height()) || 
							 (image.getDepth() != iplImage.depth()) || (image.getNumChannels() != iplImage.nChannels()))	
						image = new ImageJCV(iplImage.width(), iplImage.height(), iplImage.depth(), iplImage.nChannels());
		
					image.getImageData().rewind();
					
					if (image.getWidthStep() != iplImage.widthStep())
					{
						image.getImage().copyFrom(iplImage.getBufferedImage());						
					}
					else
						image.getImageData().put(iplImage.getByteBuffer());
					
					currentTuple.setAttribute(attrs[0], image);
				}
				
				if (result.startTimeStamp != null)
				{
					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.START_TIMESTAMP);
					if (attrs.length > 0) currentTuple.setAttribute(attrs[0], result.startTimeStamp);					
				}

				if (result.endTimeStamp != null)
				{
					attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.END_TIMESTAMP);
					if (attrs.length > 0) currentTuple.setAttribute(attrs[0], result.endTimeStamp);
				}
				
				return true;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				return false;
			}
		}
	}	

	protected abstract FrameGrabber getFrameGrabber();
	protected abstract GrabResult getFrame() throws FrameGrabber.Exception;
}
