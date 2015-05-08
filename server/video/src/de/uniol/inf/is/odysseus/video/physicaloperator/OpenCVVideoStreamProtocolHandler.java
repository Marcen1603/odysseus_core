
package de.uniol.inf.is.odysseus.video.physicaloperator;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameRecorder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class OpenCVVideoStreamProtocolHandler extends AbstractVideoStreamProtocolHandler 
{
	private static final String NAME = "OpenCVVideoStream";
	public static final String CAMERA_URL_PREFIX = "camera://";
			
	public OpenCVVideoStreamProtocolHandler() 
	{
		super();
	}

	public OpenCVVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
	}
	
	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IDataHandler<Tuple<?>> dataHandler)
	{
		return new OpenCVVideoStreamProtocolHandler(direction, access, dataHandler, options);
	}	
	
	public FrameRecorder createRecorder(ImageJCV image)
	{
		return new OpenCVFrameRecorder(getStreamUrl(), image.getWidth(), image.getHeight());
	}
		
	public FrameGrabber createGrabber()
	{
		if (getStreamUrl().startsWith(CAMERA_URL_PREFIX))
		{
			int deviceId = Integer.parseInt(getStreamUrl().substring(CAMERA_URL_PREFIX.length()));
			return new OpenCVFrameGrabber(deviceId);
		}
		else
			return new OpenCVFrameGrabber(getStreamUrl());
	}

	@Override
	public String getName() { return NAME; }
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof OpenCVVideoStreamProtocolHandler)) 
			return false;
		
		if (!super.isSemanticallyEqualImpl(o)) 
			return false;
				
		return true;
	}	
}
