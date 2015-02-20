package de.uniol.inf.is.odysseus.video.physicaloperator;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class IntegratedCameraTransportHandler extends FrameGrabberTransportHandler 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(IntegratedCameraTransportHandler.class);
	
	private int cameraId;
	
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

	@Override
	protected FrameGrabber getFrameGrabber() 
	{
		return new OpenCVFrameGrabber(cameraId);
	}

	@Override
	protected GrabResult getFrame() throws FrameGrabber.Exception 
	{
		GrabResult result = new GrabResult();
		result.startTimeStamp = null;
		result.endTimeStamp = null;
		result.image = frameGrabber.grab();
		return result;
	}
}
