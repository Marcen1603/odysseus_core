package de.uniol.inf.is.odysseus.imagejcv.util;

import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_BGR24;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_BGRA;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_GRAY16;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_RGBA;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_16S;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_16U;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8S;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGRA2RGBA;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

import java.nio.ByteBuffer;

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
		if (newImage.getImage().widthStep() == input.getImage().widthStep())
		{
			newBuffer.put(oldBuffer);
		}
		else
			throw new UnsupportedOperationException("Not implemented yet!");

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

				double value = Short.reverseBytes(oldBuffer.getShort(oldIndex));

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
}
