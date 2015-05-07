
package de.uniol.inf.is.odysseus.video.physicaloperator;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class FFmpegVideoStreamProtocolHandler extends AbstractVideoStreamProtocolHandler 
{
	private static final String NAME = "FFmpegVideoStream";
	
	public FFmpegVideoStreamProtocolHandler() 
	{
		super();
	}

	public FFmpegVideoStreamProtocolHandler(ITransportDirection direction, IAccessPattern access, IDataHandler<Tuple<?>> dataHandler, OptionMap options) 
	{
		super(direction, access, dataHandler, options);
	}
	
	@Override
	public IProtocolHandler<Tuple<?>> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options, IDataHandler<Tuple<?>> dataHandler)
	{
		return new FFmpegVideoStreamProtocolHandler(direction, access, dataHandler, options);
	}	
	
	public FrameRecorder startRecorder(ImageJCV image) throws FrameRecorder.Exception
	{
		FrameRecorder recorder = new FFmpegFrameRecorder(getStreamUrl(), image.getWidth(), image.getHeight());
//		recorder.setVideoBitrate(716800);
//		recorder.setPixelFormat(avutil.PIX_FMT_YUV420P16);
//		recorder.setVideoCodec(13);
//		
		double fps = getFrameRate();
		
		if (fps == 0.0) fps = 30.0;
		
		recorder.setFrameRate(fps);
		recorder.setFormat("h264");
		recorder.start();
		
		return recorder;
	}
	
	public void recordFrame(ImageJCV image) throws FrameRecorder.Exception
	{
		getRecorder().record(image.getImage());
	}
	
	public FrameGrabber startGrabber() throws FrameGrabber.Exception
	{
		FrameGrabber grabber = new FFmpegFrameGrabber(getStreamUrl());
		grabber.start();
		return grabber;
	}

	@Override
	public String getName() { return NAME; }
}
