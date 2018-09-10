package de.uniol.inf.is.odysseus.video;

import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_NONE;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_NONE;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.util.ImageFunctions;

public abstract class AbstractVideoImplementation 
{
	public static final String BITSPERPIXEL = "bitsPerPixel";
	public static final String FRAMESIZEMULTIPLE = "frameSizeMultiple";
	public static final String PIXELFORMAT = "pixelFormat";
	public static final String FRAMERATE = "framerate";
	public static final String FORMAT = "format";
	public static final String VIDEOQUALITY = "videoQuality";
	public static final String VIDEOCODEC = "videoCodec";
	public static final String BITRATE = "bitRate";
	public static final String OPTIONS_PREFIX = "codec:";
	
	public String format;	
	public double frameRate;
	public int frameSizeMultiple;
	public int bitRate;		
	public int pixelFormat;
	public int bitsPerPixel;
	public int videoCodec;
	public double videoQuality;
	public Map<String, String> videoOptions = new HashMap<>();
	public String syncFileName = null;
	
	public FrameRecorder recorder = null;
	public FrameGrabber grabber = null;	
	private ImageJCV receivedImage;
	private DataOutputStream syncFileStream;
	
	@Override
	public boolean equals(Object otherObject)
	{
		if (!(otherObject instanceof AbstractVideoImplementation)) return false;
		
		AbstractVideoImplementation other = (AbstractVideoImplementation) otherObject;
		if (!format.equals(other.format)) return false;
		if (frameRate != other.frameRate) return false;
		if (frameSizeMultiple != other.frameSizeMultiple) return false;
		if (bitRate != other.bitRate) return false; 
		if (pixelFormat != other.pixelFormat) return false;
		if (bitsPerPixel != other.bitsPerPixel) return false;
		if (videoCodec != other.videoCodec) return false;
		if (videoQuality != other.videoQuality) return false;
		if (!videoOptions.equals(other.videoOptions)) return false;
		
		return true;		
	}
	
	public void getOptions(OptionMap optionMap) 
	{
		frameRate = optionMap.getDouble(FRAMERATE, 0.0);
		bitRate = optionMap.getInt(BITRATE, 0);
		videoCodec = optionMap.getInt(VIDEOCODEC, 13);
		videoQuality = optionMap.getDouble(VIDEOQUALITY, 0);
		format = optionMap.get(FORMAT, null); 
		pixelFormat = optionMap.getInt(PIXELFORMAT, AV_PIX_FMT_NONE);
		frameSizeMultiple = optionMap.getInt(FRAMESIZEMULTIPLE, 1);
		bitsPerPixel = optionMap.getInt(BITSPERPIXEL, 0);
		
		for (String key : optionMap.getUnreadOptions())
		{
			if (key.startsWith(OPTIONS_PREFIX))
				videoOptions.put(key.substring(OPTIONS_PREFIX.length()), optionMap.get(key));
		}
		
		if (frameSizeMultiple < 1)
			throw new IllegalArgumentException("frameSizeMultiple must be greater or equal 1");
	}

	abstract public FrameRecorder createRecorder(ImageJCV image);
	abstract public FrameGrabber createGrabber();

	abstract public void onRecorderStarted();
	abstract public void onGrabberStarted();	
	
	public void createAndStartRecorder(ImageJCV image) throws IOException 
	{
		this.recorder = createRecorder(image);
		
		if (pixelFormat != AV_PIX_FMT_NONE) recorder.setPixelFormat(pixelFormat);		
		if (format != null) recorder.setFormat(format);
		if (videoCodec != AV_CODEC_ID_NONE) recorder.setVideoCodec(videoCodec);
		if (videoQuality != -1.0) recorder.setVideoQuality(videoQuality);
		if (frameRate != 0.0) recorder.setFrameRate(frameRate);
		if (bitRate != 0) recorder.setVideoBitrate(bitRate);
		
		for (Entry<String, String> entry : videoOptions.entrySet())
			recorder.setVideoOption(entry.getKey(), entry.getValue());
		
		try {
			recorder.start();			
			
			if (syncFileName != null && !syncFileName.equals(""))
				syncFileStream = new DataOutputStream(new FileOutputStream(syncFileName));
			else
				syncFileStream = null;
			
		} catch (IOException | FrameRecorder.Exception e) {
			recorder = null;
			throw new IOException(e);
		}
		
		onRecorderStarted();
	}

	public void createAndStartGrabber() throws FrameGrabber.Exception 
	{
		this.grabber = createGrabber();
		
		if (pixelFormat != AV_PIX_FMT_NONE) grabber.setPixelFormat(pixelFormat);
		if (bitsPerPixel != 0) grabber.setBitsPerPixel(bitsPerPixel);
		if (frameRate != 0.0) grabber.setFrameRate(frameRate);
		
		try {		
			grabber.start();
		} catch (Exception e) {
			grabber = null;
			throw e;
		}
		
		if (frameRate == 0.0)
			frameRate = grabber.getFrameRate();
		
		onGrabberStarted();
	}

	public void stop() 
	{
		if (recorder != null) 
		{
			try	{
				recorder.stop();
				recorder.release();
			} catch (FrameRecorder.Exception e) {
			}
			recorder = null;
		}
		
		if (grabber != null) 
		{
			try	{
				grabber.stop();
				grabber.release();
			} catch (FrameGrabber.Exception e) {
			} 
			grabber = null;
		} 		
		
		if (syncFileStream != null)
		{
			try {
				syncFileStream.close();
			} catch (IOException e)	{
			}
			syncFileStream = null;
		}		
		
	}

	public void open() 
	{
		recorder = null;
		grabber = null;
		syncFileStream = null;
	}

	public void record(ImageJCV image, double timeStamp) throws IOException
	{
		try 
		{
			if (frameSizeMultiple > 1)
				image = ImageFunctions.extendToMultipleOf(image, frameSizeMultiple);			
			
			if (recorder == null)
				createAndStartRecorder(image);		
					
			try
			{
				recorder.record(new OpenCVFrameConverter.ToIplImage().convert(image.getImage()));
			}
			catch (ArithmeticException e)
			{
				System.err.println("Error " + e.getMessage() + " using " + image);
			}			
			
			if (syncFileStream != null)
				syncFileStream.writeDouble(timeStamp);
		} 
		catch (FrameRecorder.Exception e) 
		{
			throw new IOException("Error while recording frame", e);
		}
		
	}

	public ImageJCV grab() throws FrameGrabber.Exception 
	{		
		if (grabber == null)
			createAndStartGrabber();										
		
		IplImage iplImage = new OpenCVFrameConverter.ToIplImage().convert(grabber.grab());
		if (iplImage == null || iplImage.isNull()) return null;
		
		if (receivedImage == null || (receivedImage.getWidth() != iplImage.width()) || (receivedImage.getHeight() != iplImage.height()) || 
				 (receivedImage.getDepth() != iplImage.depth()) || (receivedImage.getNumChannels() != iplImage.nChannels()))
		{
			receivedImage = ImageJCV.fromIplImage(iplImage, grabber.getPixelFormat());
		}
		else
			receivedImage.getImageData().put(iplImage.imageData().position(0).limit(iplImage.imageSize()).asByteBuffer());
		
		return receivedImage;
	}

	public void setSyncFileName(String syncFileName) 
	{
		this.syncFileName = syncFileName;
	}
}
