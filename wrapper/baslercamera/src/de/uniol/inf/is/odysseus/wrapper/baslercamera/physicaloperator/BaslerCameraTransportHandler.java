package de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerCamera;
import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerCamera.OperationMode;

public class BaslerCameraTransportHandler extends AbstractSimplePullTransportHandler<Tuple<IMetaAttribute>> implements ICommandProvider  
{
	private final Logger LOG = LoggerFactory.getLogger(BaslerCameraTransportHandler.class);

	private String serialNumber;
	private BaslerCamera.OperationMode operationMode;
	
	private BaslerCamera cameraCapture;
	private Tuple<IMetaAttribute> currentTuple;
	private ImageJCV imageJCV;
	private final Object processLock = new Object();
	
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
		
		serialNumber = options.get("serialnumber", "");
		
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


	@Override public String getName() { return "BaslerCamera"; }

	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
				LOG.debug("Starting basler camera...");
		 		cameraCapture = new BaslerCamera(serialNumber)
		 			{
		 				@Override public void onGrabbed(ByteBuffer buffer)
		 				{
							Tuple<IMetaAttribute> tuple = new Tuple<>(getSchema().size(), false);
							int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
							if (attrs.length > 0)
							{
								imageJCV.getImageData().rewind();
								imageJCV.getImageData().put(buffer);				 								
								tuple.setAttribute(attrs[0], imageJCV);
							}
								
							fireProcess(tuple);		 					
		 				}
		 			};
				cameraCapture.start(operationMode);				
				imageJCV = new ImageJCV(cameraCapture.getImageWidth(), cameraCapture.getImageHeight(), IPL_DEPTH_8U, 3);
				cameraCapture.setLineLength(imageJCV.getWidthStep());
				
				currentTuple = null;
				
				LOG.debug("Basler camera started.");
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
			LOG.debug("Stopping basler camera...");
			if (cameraCapture != null)
			{
				cameraCapture.stop();
				cameraCapture = null;
			}
			
			imageJCV = null;
			currentTuple = null;
			LOG.debug("Basler camera stopped");
		}
	}

	private double smoothFPS = 0.0f;
	private double alpha = 0.95f;
	private long lastTime = 0;
	private int imageCount = 0;
	
	private void logStats()
	{
		long now = System.nanoTime();
		double dt = (now - lastTime) / 1.0e9;
		double fps = 1.0/dt;

		smoothFPS = alpha*smoothFPS + (1.0-alpha)*fps; 
		
//		System.out.println("getNext " + now / 1.0e9 + ", dt = " + dt + " = " + 1.0/dt + " FPS");
		System.out.println(String.format("%d %s: %.4f FPS (%.4f)", imageCount++, serialNumber, smoothFPS, fps));
		lastTime = now;		
	}
	
	@Override public Tuple<IMetaAttribute> getNext() 
	{
		logStats();
		
		Tuple<IMetaAttribute> tuple = currentTuple;
		currentTuple = null;				
        return tuple;					
	}
	
	@Override public boolean hasNext() 
	{
		synchronized (processLock)
		{
			if (cameraCapture == null) return false;

			if (!cameraCapture.grabRGB8(imageJCV.getImageData(), 1000))
			{
				// TODO: Sometimes the camera is opened successfully, but grab always returns false. 
				// Check if it is OK to throw an exception on first grab failure, or if there are 10 in a row...?
				return false;
			}
			
			LOG.debug("Grabbed frame from Basler camera " + serialNumber);

			currentTuple = new Tuple<IMetaAttribute>(getSchema().size(), false);
			int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
			if (attrs.length > 0)
			{
				currentTuple.setAttribute(attrs[0], imageJCV); 
			}

			return true;
		}
	}	
	
    @Override public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof BaslerCameraTransportHandler)) return false;
    	
    	BaslerCameraTransportHandler other = (BaslerCameraTransportHandler)o;
    	
    	return serialNumber.equals(other.serialNumber) && operationMode.equals(other.operationMode); 
    }
    
    @Override
    public Command getCommandByName(String commandName, SDFSchema schema) 
    {
    	switch (commandName)
    	{
	    	case "trigger":
	    	{
	    		return new Command()
	    		{
	    			@Override public boolean run(IStreamObject<?> input) 
	    			{
	    				return cameraCapture.trigger();
	    			}
	    		};
	    	}

	    	default: return null;
    	}
    } 
}
