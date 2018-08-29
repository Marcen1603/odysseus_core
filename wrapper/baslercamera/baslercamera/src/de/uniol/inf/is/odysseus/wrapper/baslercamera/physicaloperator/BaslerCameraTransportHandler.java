package de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.avutil.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerCamera;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerCamera.OperationMode;

@SuppressWarnings("unused")
public class BaslerCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>>  
{
	private static final Logger LOG = LoggerFactory.getLogger(BaslerCameraTransportHandler.class);
	public static final String NAME = "BaslerCamera";
	public static final String SERIALNUMBER = "serialnumber";

	private String serialNumber;
	private BaslerCamera.OperationMode operationMode;
	
	private BaslerCamera cameraCapture;
	private Tuple<IMetaAttribute> currentTuple;
	private ImageJCV imageJCV;
	private final Object processLock = new Object();
	
	@Override public String getName() { return NAME; }
	
	public BaslerCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public BaslerCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		serialNumber = options.get(SERIALNUMBER, "");
		
		if ((protocolHandler.getAccessPattern() == IAccessPattern.ROBUST_PULL) || 
			(protocolHandler.getAccessPattern() == IAccessPattern.PULL))
			operationMode = OperationMode.PULL;
		else
			operationMode = OperationMode.PUSH;
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new BaslerCameraTransportHandler(protocolHandler, options);
	}	

	long startupTimeStamp = 0;
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
				LOG.info("Starting basler camera...");
		 		cameraCapture = new BaslerCamera(serialNumber)
		 			{
		 				@Override public void onGrabbed(double timeStamp, ByteBuffer buffer)
		 				{
							imageJCV.getImageData().put(buffer);				 										 							 					
							fireProcess(generateTuple(timeStamp));
		 				}
		 			};
				cameraCapture.start(operationMode);	
				startupTimeStamp = System.currentTimeMillis();
				
				imageJCV = new ImageJCV(cameraCapture.getImageWidth(), cameraCapture.getImageHeight(), IPL_DEPTH_8U, 3, AV_PIX_FMT_BGR24);
				cameraCapture.setLineLength(imageJCV.getWidthStep());
				
				imageCount = 0;
				currentTuple = null;
				
				LOG.info("Basler camera started.");
			}
			catch (Exception e) {
				try {
					processInClose();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				throw new IOException(e);
			}
		}
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			LOG.info("Stopping basler camera...");
			if (cameraCapture != null)
			{
				cameraCapture.stop();
				cameraCapture = null;
			}
			
			imageJCV = null;
			currentTuple = null;
			LOG.info("Basler camera stopped");
		}
	}

	private double smoothFPS = 0.0f;
	private double alpha = 0.95f;
	private long lastTime = 0;
	private int imageCount = 0;
	
	private void logStats(long now)
	{
		imageCount++;
		double dt = (now - lastTime) / 1.0e3;
		double fps = 1.0/dt;

		smoothFPS = alpha*smoothFPS + (1.0-alpha)*fps; 
		
//		System.out.println("getNext " + now / 1.0e9 + ", dt = " + dt + " = " + 1.0/dt + " FPS");
		System.out.println(String.format("%d %s: %.4f FPS (%.4f)", imageCount, serialNumber, smoothFPS, fps));
		lastTime = now;		
	}
	
	@Override public Tuple<IMetaAttribute> getNext() 
	{		
		Tuple<IMetaAttribute> tuple = currentTuple;
		currentTuple = null;				
        return tuple;					
	}	
	
	private Tuple<IMetaAttribute> generateTuple(double cameraTimePassed)
	{
		long timestamp = startupTimeStamp + (long) (cameraTimePassed * 1000);
		double systemTimePassed = (System.currentTimeMillis() - startupTimeStamp) / 1000.0;
		
		if (LOG.isTraceEnabled()){
			logStats(timestamp);
		}
		
//		LOG.debug("Grabbed frame from Basler camera " + serialNumber);
//		System.out.println("Grabbed frame from Basler camera " + serialNumber + " grab timestamp = " + cameraTimePassed + " systime = " + systemTimePassed + " diff = " + (systemTimePassed - cameraTimePassed) * 1000 + "ms");

		int attrs[];
		Tuple<IMetaAttribute> newTuple = new Tuple<IMetaAttribute>(getSchema().size(), false);
		
		attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
		if (attrs.length > 0)
			newTuple.setAttribute(attrs[0], imageJCV); 

		attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.START_TIMESTAMP);
		if (attrs.length > 0) 
		{			
			newTuple.setAttribute(attrs[0], timestamp);
		}		
		
		return newTuple;
	}
	
	@Override public boolean hasNext() 
	{
		synchronized (processLock)
		{
			if (cameraCapture == null) return false;

//			if (System.currentTimeMillis() > ImageJCV.startTime + 10000) return false;
			
			int grabTries = 0;
			while (!cameraCapture.grabRGB8(imageJCV.getImageData(), 1000))
			{
				// Sometimes the camera is opened successfully, but grab always returns false.
				// 
				if (imageCount == 0)
				{
					grabTries++;
					if (grabTries > 3)
						throw new RuntimeException("Camera opened successfully, but cannot grab frames");
					
					cameraCapture.stop();
					cameraCapture.start(operationMode);
					startupTimeStamp = System.currentTimeMillis();
				}
				else
					return false;
			}
			
			currentTuple = generateTuple(cameraCapture.getLastTimeStamp());						
			return true;
		}
	}	
	
    @Override public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof BaslerCameraTransportHandler)) return false;
    	
    	BaslerCameraTransportHandler other = (BaslerCameraTransportHandler)o;
    	
    	return serialNumber.equals(other.serialNumber) && operationMode.equals(other.operationMode); 
    }
    
    public boolean trigger() {
    	return cameraCapture.trigger();
    } 
}
