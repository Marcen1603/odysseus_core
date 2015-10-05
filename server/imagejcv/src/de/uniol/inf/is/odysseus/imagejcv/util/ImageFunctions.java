package de.uniol.inf.is.odysseus.imagejcv.util;

import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.nio.ByteBuffer;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

public class ImageFunctions {

	public static ImageJCV convertEncodedRGBTo16Bit(ImageJCV input) 
	{
		if (((input.getDepth() != IPL_DEPTH_8S) && (input.getDepth() != IPL_DEPTH_8U)) || (input.getNumChannels() != 4))
			throw new UnsupportedOperationException("convertEncodedRGBTo16BitCV requires a 4-channel RGBA image");

		if (input.getPixelFormat() != AV_PIX_FMT_RGBA)
		{
			// Image needs to be converted
			int conversion;
			switch (input.getPixelFormat())
			{
			case AV_PIX_FMT_BGRA: conversion = CV_BGRA2RGBA; break;
			default: throw new UnsupportedOperationException("No conversion specified from pixel format " + input.getPixelFormat() + " to RGBA");
			}
						
			ImageJCV converted = ImageJCV.createCompatible(input);			
			cvCvtColor(input.getImage(), converted.getImage(), conversion);			
			input = converted;
		}

		ImageJCV newImage = new ImageJCV(input.getWidth()*2, input.getHeight(), IPL_DEPTH_16U, 1, AV_PIX_FMT_GRAY16);

		ByteBuffer oldBuffer = input.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();

		if (newBuffer.limit() != oldBuffer.limit())
			throw new UnsupportedOperationException("Image buffers are not the same size...?");		

		// Bytes can be copied directly
		// One pass when width steps are the same, otherwise line by line
		if (newImage.getWidthStep() == input.getWidthStep())
		{
			newBuffer.put(oldBuffer);
		}
		else
			throw new UnsupportedOperationException("Not implemented yet!");

		return newImage;
	}
	
	public static ImageJCV reinterpret(ImageJCV input, int width, int height, int depth, int channels, int pixelFormat)
	{
		ImageJCV newImage = new ImageJCV(width, height, depth, channels, pixelFormat);

		ByteBuffer oldBuffer = input.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();

		if (newBuffer.limit() != oldBuffer.limit())
			throw new IllegalArgumentException("Buffer size of reinterpreted image must not change");		

		// Bytes can be copied directly
		// One pass when width steps are the same, otherwise line by line
//		if (newImage.getImage().widthStep() == input.getImage().widthStep())
		{
			newBuffer.put(oldBuffer);
		}
/*		else
			throw new UnsupportedOperationException("Unequal width steps not implemented yet");*/

		return newImage;		
	}

	public static ImageJCV stretchContrast(ImageJCV input, int oldMin, int oldMax, int newMin, int newMax)
	{
		if ((input.getDepth() != IPL_DEPTH_16S) && (input.getDepth() != IPL_DEPTH_16U))
			throw new UnsupportedOperationException("StretchContrast not implemented for images with depth != 16!");

		ImageJCV newImage = new ImageJCV(input.getWidth(), input.getHeight(), IPL_DEPTH_8U, 3, AV_PIX_FMT_BGR24);		

		//			long lastTime = System.nanoTime();

		ByteBuffer oldBuffer = input.getImageData();
		ByteBuffer newBuffer = newImage.getImageData();

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++) 
			{
				int oldIndex = y * input.getWidthStep() + x * input.getNumChannels()*2;
				int newIndex = y * newImage.getWidthStep() + x * newImage.getNumChannels();

				double value = oldBuffer.getShort(oldIndex);

				int newVal = (int)( (double)(value - oldMin) / (oldMax - oldMin) * (newMax - newMin) + newMin);
				if (newVal < 0) newVal = 0;
				if (newVal > 255) newVal = 255;

				// Sets the pixel to a value (RGB, stored in BGR order).
				newBuffer.put(newIndex, 		(byte) (newVal));
				newBuffer.put(newIndex + 1, 	(byte) (newVal));
				newBuffer.put(newIndex + 2, 	(byte) (newVal));
			}		

		/*		long curTime = System.nanoTime();
			System.out.println("dt = " + (curTime - lastTime) / 1.0e6 + "ms");*/		

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
		
/*		int bytesPerPixel = Math.abs(newImage.getDepth() & 0xFF) / 8 * newImage.getNumChannels();
		ByteBuffer newBuffer = newImage.getImageData();
		ByteBuffer oldBuffer = image.getImageData();

		newBuffer.rewind();
		oldBuffer.rewind();
		
		if (newWidth == image.getWidth())
		{
			// Only some lines have been added to the end. 
			// Since width and widthStep is the same, the whole memory block of the old image can be copied						 
			newBuffer.put(oldBuffer);												
		}
		else
		{
			// Since the width has changed, each line has to be copied seperately
			for (int y=0;y<image.getHeight();y++)
			{
				int oldLineStart = y*image.getWidthStep();
				int newLineStart = y*newImage.getWidthStep();
				
				oldBuffer.position(oldLineStart);
				newBuffer.position(newLineStart);
				
				// Copy whole line
				for (int x = 0; x < image.getWidth()*bytesPerPixel; x++)
					newBuffer.put(oldBuffer.get());
				
				// Fill remaining empty pixel
				int remaining = newWidth - image.getWidth();
				for (int x = 0; x < remaining * bytesPerPixel; x++)
					newBuffer.put((byte)0);
			}
		}
		
		// If new lines have been added at the bottom, fill them with 0
		if (newHeight != image.getHeight())
		{
			for (int y=image.getHeight(); y<newHeight; y++)
			{
				newBuffer.position(y*newImage.getWidthStep());
				for (int x = 0; x < newWidth*bytesPerPixel; x++)
					newBuffer.put((byte)0);
			}
		}		
		
		return newImage;*/
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
}
