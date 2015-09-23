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

public abstract class AbstractVideoImplementation 
{
	private static final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	public static final String OPTIONS_PREFIX = "codec:";
	
	public String format;	
	public double frameRate;
	public boolean stretchToFit;
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
		if (stretchToFit != other.stretchToFit) return false;
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
		frameRate = optionMap.getDouble("framerate", 0.0);
		bitRate = optionMap.getInt("bitRate", 0);
		videoCodec = optionMap.getInt("videoCodec", 13);
		videoQuality = optionMap.getDouble("videoQuality", 0);
		format = optionMap.get("format", null); 
		pixelFormat = optionMap.getInt("pixelFormat", AV_PIX_FMT_NONE);
		stretchToFit = optionMap.getBoolean("stretchToFit", true);
		bitsPerPixel = optionMap.getInt("bitsPerPixel", 0);
		
		for (String key : optionMap.getUnreadOptions())
		{
			if (key.startsWith(OPTIONS_PREFIX))
				videoOptions.put(key.substring(OPTIONS_PREFIX.length()), optionMap.get(key));
		}		
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
			if (!stretchToFit)
				image = ImageJCV.extendToMultipleOf(image, 2);			
			
			if (recorder == null)
				createAndStartRecorder(image);		
					
			recorder.record(converter.convert(image.getImage()));
			
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
		
		IplImage iplImage = converter.convert(grabber.grab());
		if (iplImage == null || iplImage.isNull()) return null;
		
		if (receivedImage == null || (receivedImage.getWidth() != iplImage.width()) || (receivedImage.getHeight() != iplImage.height()) || 
				 (receivedImage.getDepth() != iplImage.depth()) || (receivedImage.getNumChannels() != iplImage.nChannels()))
		{
			receivedImage = ImageJCV.fromIplImage(iplImage, grabber.getPixelFormat());
		}
		else
			receivedImage.copyFrom(iplImage);
		
		return receivedImage;
	}

	public void setSyncFileName(String syncFileName) 
	{
		this.syncFileName = syncFileName;
	}
}
