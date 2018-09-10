package de.uniol.inf.is.odysseus.imagejcv.util;

import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class ImageFunctions {

	public static ImageJCV reinterpret(ImageJCV input, int width, int height, int depth, int channels, int pixelFormat)
	{
		ImageJCV newImage = new ImageJCV(width, height, depth, channels, pixelFormat);

		ByteBuffer oldBuffer = input.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();

		if (newBuffer.limit() != oldBuffer.limit())
			throw new IllegalArgumentException("Buffer size of reinterpreted image doesn't match original buffer size!");		

		newBuffer.put(oldBuffer);
		return newImage;		
	}

	public static ImageJCV stretchContrast(ImageJCV input, int oldMin, int oldMax, int newMin, int newMax)
	{
		if ((input.getDepth() != IPL_DEPTH_16S) && (input.getDepth() != IPL_DEPTH_16U))
			throw new UnsupportedOperationException("StretchContrast not implemented for images with depth != 16!");

		ImageJCV newImage = new ImageJCV(input.getWidth(), input.getHeight(), IPL_DEPTH_8U, 3, AV_PIX_FMT_BGR24);		

		ByteBuffer oldBuffer = input.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++) 
			{
				int oldIndex = y * input.getWidthStep() + x * input.getNumChannels()*2;
				int newIndex = y * newImage.getWidthStep() + x * newImage.getNumChannels();

				double value = oldBuffer.getShort(oldIndex);

				int newVal = (int)( (value - oldMin) / (oldMax - oldMin) * (newMax - newMin) + newMin);
				if (newVal < 0) newVal = 0;
				if (newVal > 255) newVal = 255;

				// Sets the pixel to a value (RGB, stored in BGR order).
				newBuffer.put(newIndex, 		(byte) (newVal));
				newBuffer.put(newIndex + 1, 	(byte) (newVal));
				newBuffer.put(newIndex + 2, 	(byte) (newVal));
			}		

		return newImage;		
	}
	
	public static ImageJCV extendToMultipleOf(ImageJCV image, int multiple)
	{
		int newWidth  = image.getWidth();
		int newHeight = image.getHeight(); 
		
		if (newWidth  % multiple != 0) newWidth  = (newWidth / multiple + 1) * multiple;
		if (newHeight % multiple != 0) newHeight = (newHeight / multiple + 1) * multiple;
		
		if (newWidth == image.getWidth() && newHeight == image.getHeight()) return image;
		
		ImageJCV newImage = new ImageJCV(newWidth, newHeight, image.getDepth(), image.getNumChannels(), image.getPixelFormat());
		cvCopyMakeBorder(image.getImage(), newImage.getImage(), new int[]{0, 0, newWidth - image.getWidth(), newHeight - image.getHeight()}, 0); 
		return newImage;
	}		
	
	// Returns this image converted to grayscale. Returns this, when this image already is a grayscale image and doClone = false
	static public ImageJCV toGrayscaleImage(ImageJCV input, boolean doClone)
	{
		int conversion = -1;
		switch (input.getNumChannels())
		{
			case 1:
				return doClone ? new ImageJCV(input) : input;
				
			case 3:
				conversion = CV_BGR2GRAY; 				
				break;
				
			case 4:
				conversion = CV_BGRA2GRAY;
				break;
			
			default: 
				throw new UnsupportedOperationException("toGrayscale not implemented for " + input.getNumChannels() + " channels!");
		}
		
		ImageJCV result = new ImageJCV(input.getWidth(), input.getHeight(), IPL_DEPTH_8U, 1, AV_PIX_FMT_GRAY8);		
		cvCvtColor(input.getImage(), result.getImage(), conversion);		
		return result;
	}

	public static ImageJCV create16BitTestImage(int imageWidth, int imageHeight, int imageNum) 
	{
		ImageJCV result = new ImageJCV(imageWidth, imageHeight, IPL_DEPTH_16S, 1, AV_PIX_FMT_GRAY16);
		ByteBuffer buf = result.getImageData();

		short val = (short) (Short.MIN_VALUE + imageNum);
		while (buf.remaining() >= 2)
		{
			buf.putShort(val++);
		}
		
		return result;
	}

	public static ImageJCV resize(ImageJCV image, int width, int height) 
	{
		ImageJCV result = new ImageJCV(width, height, image.getDepth(), image.getNumChannels(), image.getPixelFormat());		
		cvResize(image.getImage(), result.getImage());		
		return result;
	}

	private static final Map<Pair<Integer, Integer>, Integer> conversionMap;
	
	static
	{
		conversionMap = new HashMap<>();
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR24, 	AV_PIX_FMT_BGRA), 	CV_BGR2BGRA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGB24, 	AV_PIX_FMT_RGBA), 	CV_RGB2RGBA);		
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGRA,	AV_PIX_FMT_BGR24), 	CV_BGRA2BGR);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGBA,	AV_PIX_FMT_RGB24), 	CV_RGBA2RGB);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR24,	AV_PIX_FMT_RGBA),	CV_BGR2RGBA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGB24, 	AV_PIX_FMT_BGRA), 	CV_RGB2BGRA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGBA, 	AV_PIX_FMT_BGR24), 	CV_RGBA2BGR);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGRA, 	AV_PIX_FMT_RGB24), 	CV_BGRA2RGB);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR24, 	AV_PIX_FMT_RGB24), 	CV_BGR2RGB);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGB24, 	AV_PIX_FMT_BGR24), 	CV_RGB2BGR);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGRA, 	AV_PIX_FMT_RGBA), 	CV_BGRA2RGBA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGBA, 	AV_PIX_FMT_BGRA), 	CV_RGBA2BGRA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR24, 	AV_PIX_FMT_GRAY8), 	CV_BGR2GRAY);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGB24, 	AV_PIX_FMT_GRAY8), 	CV_RGB2GRAY);
		conversionMap.put(new Pair<>(AV_PIX_FMT_GRAY8, 	AV_PIX_FMT_BGR24), 	CV_GRAY2BGR);
		conversionMap.put(new Pair<>(AV_PIX_FMT_GRAY8, 	AV_PIX_FMT_RGB24), 	CV_GRAY2RGB);
		conversionMap.put(new Pair<>(AV_PIX_FMT_GRAY8, 	AV_PIX_FMT_BGRA), 	CV_GRAY2BGRA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_GRAY8, 	AV_PIX_FMT_RGBA), 	CV_GRAY2RGBA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGRA, 	AV_PIX_FMT_GRAY8), 	CV_BGRA2GRAY);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGBA, 	AV_PIX_FMT_GRAY8), 	CV_RGBA2GRAY);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR24, 	AV_PIX_FMT_BGR565), CV_BGR2BGR565);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGB24, 	AV_PIX_FMT_BGR565), CV_RGB2BGR565);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR565, AV_PIX_FMT_BGR24), 	CV_BGR5652BGR);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR565, AV_PIX_FMT_RGB24), 	CV_BGR5652RGB);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGRA, 	AV_PIX_FMT_BGR565), CV_BGRA2BGR565);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGBA, 	AV_PIX_FMT_BGR565), CV_RGBA2BGR565);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR565, AV_PIX_FMT_BGRA), 	CV_BGR5652BGRA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR565, AV_PIX_FMT_RGBA), 	CV_BGR5652RGBA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_GRAY8, 	AV_PIX_FMT_BGR565), CV_GRAY2BGR565);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR565,	AV_PIX_FMT_GRAY8), 	CV_BGR5652GRAY);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR24,	AV_PIX_FMT_BGR555), CV_BGR2BGR555);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGB24,	AV_PIX_FMT_BGR555),	CV_RGB2BGR555);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR555,	AV_PIX_FMT_BGR24),	CV_BGR5552BGR);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR555,	AV_PIX_FMT_RGB24),	CV_BGR5552RGB);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGRA,	AV_PIX_FMT_BGR555),	CV_BGRA2BGR555);
		conversionMap.put(new Pair<>(AV_PIX_FMT_RGBA,	AV_PIX_FMT_BGR555),	CV_RGBA2BGR555);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR555,	AV_PIX_FMT_BGRA),	CV_BGR5552BGRA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR555,	AV_PIX_FMT_RGBA),	CV_BGR5552RGBA);
		conversionMap.put(new Pair<>(AV_PIX_FMT_GRAY8,	AV_PIX_FMT_BGR555),	CV_GRAY2BGR555);
		conversionMap.put(new Pair<>(AV_PIX_FMT_BGR555,	AV_PIX_FMT_GRAY8),	CV_BGR5552GRAY);
		// TODO: Add these to conversion map
				/*CV_BGR2XYZ = 32
				CV_RGB2XYZ = 33
				CV_XYZ2BGR = 34
				CV_XYZ2RGB = 35
				CV_BGR2YCrCb = 36
				CV_RGB2YCrCb = 37
				CV_YCrCb2BGR = 38
				CV_YCrCb2RGB = 39
				CV_BGR2HSV = 40
				CV_RGB2HSV = 41
				CV_BGR2Lab = 44
				CV_RGB2Lab = 45
				CV_BayerBG2BGR = 46
				CV_BayerGB2BGR = 47
				CV_BayerRG2BGR = 48
				CV_BayerGR2BGR = 49
				CV_BayerBG2RGB = 48
				CV_BayerGB2RGB = 49
				CV_BayerRG2RGB = 46
				CV_BayerGR2RGB = 47
				CV_BGR2Luv = 50
				CV_RGB2Luv = 51
				CV_BGR2HLS = 52
				CV_RGB2HLS = 53
				CV_HSV2BGR = 54
				CV_HSV2RGB = 55
				CV_Lab2BGR = 56
				CV_Lab2RGB = 57
				CV_Luv2BGR = 58
				CV_Luv2RGB = 59
				CV_HLS2BGR = 60
				CV_HLS2RGB = 61
				CV_BayerBG2BGR_VNG = 62
				CV_BayerGB2BGR_VNG = 63
				CV_BayerRG2BGR_VNG = 64
				CV_BayerGR2BGR_VNG = 65
				CV_BayerBG2RGB_VNG = 64
				CV_BayerGB2RGB_VNG = 65
				CV_BayerRG2RGB_VNG = 62
				CV_BayerGR2RGB_VNG = 63
				CV_BGR2HSV_FULL = 66
				CV_RGB2HSV_FULL = 67
				CV_BGR2HLS_FULL = 68
				CV_RGB2HLS_FULL = 69
				CV_HSV2BGR_FULL = 70
				CV_HSV2RGB_FULL = 71
				CV_HLS2BGR_FULL = 72
				CV_HLS2RGB_FULL = 73
				CV_LBGR2Lab = 74
				CV_LRGB2Lab = 75
				CV_LBGR2Luv = 76
				CV_LRGB2Luv = 77
				CV_Lab2LBGR = 78
				CV_Lab2LRGB = 79
				CV_Luv2LBGR = 80
				CV_Luv2LRGB = 81
				CV_BGR2YUV = 82
				CV_RGB2YUV = 83
				CV_YUV2BGR = 84
				CV_YUV2RGB = 85
				CV_BayerBG2GRAY = 86
				CV_BayerGB2GRAY = 87
				CV_BayerRG2GRAY = 88
				CV_BayerGR2GRAY = 89
				CV_YUV2RGB_NV12 = 90
				CV_YUV2BGR_NV12 = 91
				CV_YUV2RGB_NV21 = 92
				CV_YUV2BGR_NV21 = 93
				CV_YUV420sp2RGB = 92
				CV_YUV420sp2BGR = 93
				CV_YUV2RGBA_NV12 = 94
				CV_YUV2BGRA_NV12 = 95
				CV_YUV2RGBA_NV21 = 96
				CV_YUV2BGRA_NV21 = 97
				CV_YUV420sp2RGBA = 96
				CV_YUV420sp2BGRA = 97
				CV_YUV2RGB_YV12 = 98
				CV_YUV2BGR_YV12 = 99
				CV_YUV2RGB_IYUV = 100
				CV_YUV2BGR_IYUV = 101
				CV_YUV2RGB_I420 = 100
				CV_YUV2BGR_I420 = 101
				CV_YUV420p2RGB = 98
				CV_YUV420p2BGR = 99
				CV_YUV2RGBA_YV12 = 102
				CV_YUV2BGRA_YV12 = 103
				CV_YUV2RGBA_IYUV = 104
				CV_YUV2BGRA_IYUV = 105
				CV_YUV2RGBA_I420 = 104
				CV_YUV2BGRA_I420 = 105
				CV_YUV420p2RGBA = 102
				CV_YUV420p2BGRA = 103
				CV_YUV2GRAY_420 = 106
				CV_YUV2GRAY_NV21 = 106
				CV_YUV2GRAY_NV12 = 106
				CV_YUV2GRAY_YV12 = 106
				CV_YUV2GRAY_IYUV = 106
				CV_YUV2GRAY_I420 = 106
				CV_YUV420sp2GRAY = 106
				CV_YUV420p2GRAY = 106
				CV_YUV2RGB_UYVY = 107
				CV_YUV2BGR_UYVY = 108
				CV_YUV2RGB_Y422 = 107
				CV_YUV2BGR_Y422 = 108
				CV_YUV2RGB_UYNV = 107
				CV_YUV2BGR_UYNV = 108
				CV_YUV2RGBA_UYVY = 111
				CV_YUV2BGRA_UYVY = 112
				CV_YUV2RGBA_Y422 = 111
				CV_YUV2BGRA_Y422 = 112
				CV_YUV2RGBA_UYNV = 111
				CV_YUV2BGRA_UYNV = 112
				CV_YUV2RGB_YUY2 = 115
				CV_YUV2BGR_YUY2 = 116
				CV_YUV2RGB_YVYU = 117
				CV_YUV2BGR_YVYU = 118
				CV_YUV2RGB_YUYV = 115
				CV_YUV2BGR_YUYV = 116
				CV_YUV2RGB_YUNV = 115
				CV_YUV2BGR_YUNV = 116
				CV_YUV2RGBA_YUY2 = 119
				CV_YUV2BGRA_YUY2 = 120
				CV_YUV2RGBA_YVYU = 121
				CV_YUV2BGRA_YVYU = 122
				CV_YUV2RGBA_YUYV = 119
				CV_YUV2BGRA_YUYV = 120
				CV_YUV2RGBA_YUNV = 119
				CV_YUV2BGRA_YUNV = 120
				CV_YUV2GRAY_UYVY = 123
				CV_YUV2GRAY_YUY2 = 124
				CV_YUV2GRAY_Y422 = 123
				CV_YUV2GRAY_UYNV = 123
				CV_YUV2GRAY_YVYU = 124
				CV_YUV2GRAY_YUYV = 124
				CV_YUV2GRAY_YUNV = 124
				CV_RGBA2mRGBA = 125
				CV_mRGBA2RGBA = 126
				CV_RGB2YUV_I420 = 127
				CV_BGR2YUV_I420 = 128
				CV_RGB2YUV_IYUV = 127
				CV_BGR2YUV_IYUV = 128
				CV_RGBA2YUV_I420 = 129
				CV_BGRA2YUV_I420 = 130
				CV_RGBA2YUV_IYUV = 129
				CV_BGRA2YUV_IYUV = 130
				CV_RGB2YUV_YV12 = 131
				CV_BGR2YUV_YV12 = 132
				CV_RGBA2YUV_YV12 = 133
				CV_BGRA2YUV_YV12 = 134
				CV_BayerBG2BGR_EA = 135
				CV_BayerGB2BGR_EA = 136
				CV_BayerRG2BGR_EA = 137
				CV_BayerGR2BGR_EA = 138
				CV_BayerBG2RGB_EA = 137
				CV_BayerGB2RGB_EA = 138
				CV_BayerRG2RGB_EA = 135
				CV_BayerGR2RGB_EA = 136 */		
	}
	
	public static ImageJCV convertTo(ImageJCV image, int newPixelFormat) 
	{
		if (image.getPixelFormat() == newPixelFormat) return image;
		
		Integer conversionCode = conversionMap.get(new Pair<Integer, Integer>(image.getPixelFormat(), newPixelFormat));
		if (conversionCode == null) { 
			throw new UnsupportedOperationException("No conversion specified from pixel format " + image.getPixelFormat() + 
													" to " + newPixelFormat);
		}
		
		ImageJCV converted = ImageJCV.createCompatible(image);			
		cvCvtColor(image.getImage(), converted.getImage(), conversionCode);			
		return converted;
	}		
}
