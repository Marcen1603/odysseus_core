package de.uniol.inf.is.odysseus.video.physicaloperator;

import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

public class IntegratedCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(IntegratedCameraTransportHandler.class);
	private final Object processLock = new Object();
	
	private int 				cameraId;
	private OpenCVFrameGrabber 	cameraCapture;
	
	private ImageJCV currentImage = null;
	
	public IntegratedCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public IntegratedCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		cameraId = options.getInt("cameraid", 0);
	}	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new IntegratedCameraTransportHandler(protocolHandler, options);
	}

	@Override public String getName() { return "IntegratedCamera"; }

	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			cameraCapture = new OpenCVFrameGrabber(cameraId);
			try 
			{
				cameraCapture.start();
			} 
			catch (FrameGrabber.Exception e) 
			{
				cameraCapture = null;
				throw new IOException(e.getMessage());
			}
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			if (cameraCapture != null)
			{
				try 
				{
					cameraCapture.stop();
					cameraCapture.release();
				} 
				catch (Exception e)
				{
					throw new IOException(e.getMessage());
				}
				finally 
				{				
					cameraCapture = null;
				}
			}
		}
	}	
	
	@Override public Tuple<IMetaAttribute> getNext() 
	{
		synchronized (processLock)
		{
			if (currentImage == null) return null;
		
			Tuple<IMetaAttribute> tuple = new Tuple<>(getSchema().size(), false);
			int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
			if (attrs.length > 0) 
				tuple.setAttribute(attrs[0], currentImage);
			
	        currentImage = null;
	        return tuple;
		}
	}
    
	@Override public boolean hasNext()
	{
		synchronized (processLock)
		{
			try 
			{				
				if (cameraCapture == null) return false;
				IplImage iplImage = cameraCapture.grab();
				
				if (iplImage == null || iplImage.isNull()) return false;
				currentImage = new ImageJCV(iplImage.clone());
				return true;
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				return false;
			}
		}
	}
		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof IntegratedCameraTransportHandler)) {
    		return false;
    	}
    	IntegratedCameraTransportHandler other = (IntegratedCameraTransportHandler)o;
    	if(this.cameraId != other.cameraId)
    		return false;
    	
    	return true;
    }
}
