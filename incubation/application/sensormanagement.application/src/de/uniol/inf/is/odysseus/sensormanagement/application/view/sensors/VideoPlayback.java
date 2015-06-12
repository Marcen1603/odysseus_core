package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import static org.bytedeco.javacpp.opencv_core.cvFlip;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewException;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.VideoLogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public class VideoPlayback extends PlaybackReceiver 
{
	private FFmpegFrameGrabber 	capture;
	private double 				eventTime;
	private double				fps;	
	private double[]			syncData;
	private int					currentFrame;
	private boolean 			rotate180;

	public VideoPlayback(SensorModel sensorModel, LogMetaData logMetaData) 
	{
		super(sensorModel, logMetaData);
	}

	@Override protected void onStart()
	{
		VideoLogMetaData videoLogMetaData = (VideoLogMetaData) logMetaData;
		
		try
		{		
			capture = new FFmpegFrameGrabber(videoLogMetaData.path + videoLogMetaData.videoFile);
			capture.start();
			
			if (videoLogMetaData.syncFile != null)
			{
				FileInputStream stream = new FileInputStream(videoLogMetaData.path + videoLogMetaData.syncFile);
				syncData = new double[(int)stream.getChannel().size() / 8];
				
				DataInputStream syncFileStream = new DataInputStream(stream);
				
				try
				{
					int idx=0;
					while (true)
						syncData[idx++] = syncFileStream.readDouble();
				}
				catch (EOFException e)
				{
					
				}
			}
			
			rotate180 = videoLogMetaData.doRotate180;
			
			String fpsString = videoLogMetaData.fps;
			if (fpsString != null) 
				fps = Double.parseDouble(fpsString);
			else
				fps = capture.getFrameRate();
			
			eventTime = getStartTime();
			currentFrame = 0;
		}
		catch (Exception e)
		{
			stop();
			throw new ViewException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@Override protected void onStop()
	{
		try
		{
			if (capture != null)
				capture.stop();
		}
		catch (Exception e)
		{
			stop();
			throw new ViewException(e.getClass().getName() + ": " + e.getMessage());
		}
		finally
		{
			capture = null;
		}
	}
	
	@Override protected Event getNextEvent() throws ViewException
	{
		if (capture == null) return null;
		
		try
		{
			Event e = null;
			
			if (syncData != null)
			{
				if (currentFrame == syncData.length)
					return null;
				else
					eventTime = syncData[currentFrame];
			}
			
			IplImage image = capture.grab();
			if (image != null && !image.isNull())
			{		
/*				BufferedImage capturedImg = image.getBufferedImage();
				ColorModel cm = capturedImg.getColorModel();
				boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
				WritableRaster raster = capturedImg.copyData(null);
				BufferedImage buffImg = new BufferedImage(cm, raster, isAlphaPremultiplied, null);*/				
				
				if (rotate180)
				{
					cvFlip(image, image, -1);
					
/*					if (rotationTransformOp == null)
					{
						AffineTransform rotationTransform = AffineTransform.getScaleInstance(-1, -1);
						rotationTransform.translate(-buffImg.getWidth(null), -buffImg.getHeight(null));
						rotationTransformOp = new AffineTransformOp(rotationTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
					}
					
					buffImg = rotationTransformOp.filter(buffImg, null);*/
				}
				
				double eventSecondsValid = 1.0; //1.0 / fps * 2.0;
//				e =  new Event(buffImg, eventTime, eventSecondsValid);
				e =  new Event(image, eventTime, eventSecondsValid);
				
				if (syncData == null)
					eventTime += 1.0 / fps;
//				System.out.println("FPS = " + capture.getFrameRate());
				
//				System.out.println("currentFrame =  " + currentFrame);
				
				currentFrame++;
			}
			else
			{
				VideoLogMetaData videoLogMetaData = (VideoLogMetaData) logMetaData;
				System.out.println("capture.grab() returned null image on " + videoLogMetaData.videoFile + ", frame " + capture.getFrameNumber() + " @ " + eventTime);
			}
					
			return e;			
		}
		catch (Exception e)
		{
			stop();
			throw new ViewException(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	@Override protected void setPlaybackPositionInternal(double now)
	{
		double timeFromStart = now - getStartTime();		
		int frameNumber = 0;

		if (timeFromStart > 0)
		{
			if (syncData != null)
			{
				int startFrame=0;
				int endFrame=currentFrame++;;
				
				if (now > eventTime)
				{
					startFrame = currentFrame++;;
					endFrame = capture.getLengthInFrames()-1;
				}
				
				int numSteps = 0;
				double time = 0.0;
				while (startFrame < endFrame-1)
				{
					int middle = (startFrame + endFrame) / 2;
					time = syncData[middle];
					if (now <= time)
						endFrame = middle;
					else
						startFrame = middle;
					
					numSteps++;
				}
				
				eventTime = time;
				
				frameNumber = startFrame;
				System.out.println(numSteps + " steps to find time, last diff = " + (now - time));
			}
			else
			{					
				eventTime = now;
				frameNumber = (int) (timeFromStart * fps);
			}
		}
		else
		{
			eventTime = getStartTime();
			frameNumber = 0;
		}
		
		try
		{
			System.out.println("setFrameNumber " + frameNumber);
			capture.setFrameNumber(frameNumber);
			currentFrame = frameNumber;
		}
		catch (FFmpegFrameGrabber.Exception e)
		{
			stop();
			throw new ViewException("FrameGrabber.Exception: " + e.getMessage());
		}		
	}
}