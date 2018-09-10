
package de.uniol.inf.is.odysseus.video.physicaloperator;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameRecorder;
import org.bytedeco.javacv.VideoInputFrameGrabber;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class OpenCVVideoStreamProtocolHandler extends AbstractVideoStreamProtocolHandler 
{
	public static final String NAME = "OpenCVVideoStream";
	public static final String CAMERA_URL_PREFIX = "camera://";

	@Override public String getName() { return NAME; }
	
	public OpenCVVideoStreamProtocolHandler() {
		super();
	}

	public OpenCVVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<Tuple<?>> dataHandler, OptionMap options) {
		super(direction, access, dataHandler, options);
	}
	
	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IStreamObjectDataHandler<Tuple<?>> dataHandler) {
		return new OpenCVVideoStreamProtocolHandler(direction, access, dataHandler, options);
	}	
	
	@Override
	public FrameRecorder createRecorder(ImageJCV image) {
		return new OpenCVFrameRecorder(getStreamUrl(), image.getWidth(), image.getHeight());
	}
		
	@Override
	public FrameGrabber createGrabber() {
		if (getStreamUrl().startsWith(CAMERA_URL_PREFIX)) {
			int deviceId = Integer.parseInt(getStreamUrl().substring(CAMERA_URL_PREFIX.length()));
			return new VideoInputFrameGrabber(deviceId);
		}
		else {
			return new OpenCVFrameGrabber(getStreamUrl());
		}
	}
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof OpenCVVideoStreamProtocolHandler)) return false;
		if (!super.isSemanticallyEqualImpl(o)) return false;
				
		return true;
	}	
}
