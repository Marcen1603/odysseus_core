package de.uniol.inf.is.odysseus.imagejcv.common.datatype;

import static org.bytedeco.javacpp.opencv_core.*;

import org.bytedeco.javacpp.opencv_core.IplImage;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;

/**
 * @author Kristian Bruns
 */
public class ImageJCV implements IClone, Cloneable 
{
	private IplImage image;
	
	public ImageJCV() {
	}
	
	public ImageJCV(IplImage image) {
		this.image = image;
	}
	
	public ImageJCV(ByteBuffer buffer) {
		this.image = (IplImage) ObjectByteConverter.bytesToObject(buffer.array());
	}
	
	public ImageJCV(ImageJCV other) 
	{
		image = cvCreateImage(cvSize(other.image.width(), other.image.height()), other.image.depth(), other.image.nChannels());
		image.getByteBuffer().put(other.image.getByteBuffer());
	}
	
	public ImageJCV(int width, int height)
	{
		image = cvCreateImage(cvSize(width, height), IPL_DEPTH_8U, 4);
	}
	
	public ImageJCV(double[][] data) 
	{
		image = cvCreateImage(cvSize(data[0].length, data.length), IPL_DEPTH_8U, 4);
		ByteBuffer buffer = image.getByteBuffer();
		for (int i=0; i < data.length; i++) {
			buffer.putDouble(i, data[i][0]);
		}
	}
	
	@Override public ImageJCV clone()
	{
		return new ImageJCV(this);
	}
	
	@Override
	protected void finalize()
	{		
		release();
	}
	
	public void release()
	{
		if (image != null)
		{
			System.out.println("Release image");
			cvReleaseImage(image);
			image = null;
		}
	}
	
	public int getNumChannels()
	{
		return image.nChannels();
	}
	
	public int getChannelDepth()
	{
		return image.depth();
	}
	
	public double[][] getMatrix() {
		throw new UnsupportedOperationException("Currenlty not implemented");
	}
	
	public IplImage getImage() {
		return image;
	}
	
	public void setImage(IplImage image) {
		this.image = image;
	}
	
	public int getWidth() {
		return this.image.width();
	}
	
	public int getHeight() {
		return this.image.height();
	}
	
	public int get(final int index) {
		ByteBuffer buffer = this.image.getByteBuffer();
		int value = buffer.getInt(index);
		return value;
	}
	
	public void set(int index, int value) {
		ByteBuffer buffer = this.image.getByteBuffer();
		buffer.putInt(index, value);
	}
	
	public void writeData(ByteBuffer buffer) {
		byte[] bytes = ObjectByteConverter.objectToBytes(this.image);
		buffer.put(bytes);
	}
	
	@Override
	public String toString() {
		return "{Width: " + this.getWidth() + " Height: " + this.getHeight() + "}";
	}
	
	public void fill(int value) {
		throw new UnsupportedOperationException("Currently not implemented");
	}
}
