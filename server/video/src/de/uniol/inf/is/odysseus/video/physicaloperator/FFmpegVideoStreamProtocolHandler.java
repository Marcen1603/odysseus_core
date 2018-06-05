
package de.uniol.inf.is.odysseus.video.physicaloperator;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class FFmpegVideoStreamProtocolHandler extends AbstractVideoStreamProtocolHandler 
{
	public static final String NAME = "FFmpegVideoStream";
	
	public FFmpegVideoStreamProtocolHandler() 
	{
		super();
	}

	public FFmpegVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IStreamObjectDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);		
	}
	
	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IStreamObjectDataHandler<Tuple<?>> dataHandler)
	{
		return new FFmpegVideoStreamProtocolHandler(direction, access, dataHandler, options);
	}	
	
	@Override
	public FrameRecorder createRecorder(ImageJCV image)
	{
		return new FFmpegFrameRecorder(getStreamUrl(), image.getWidth(), image.getHeight());
	}
		
	@Override
	public FrameGrabber createGrabber()
	{
		return new FFmpegFrameGrabber(getStreamUrl());
	}

	@Override
	public String getName() { return NAME; }
	
	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) 
	{
		if (!(o instanceof FFmpegVideoStreamProtocolHandler)) 
			return false;
		
		if (!super.isSemanticallyEqualImpl(o)) 
			return false;
		
		return true;
	}
}
