package de.uniol.inf.is.odysseus.imagejcv.common.datatype;

import org.bytedeco.javacpp.opencv_core.IplImage;

import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;

/**
 * @author Kristian Bruns
 */
public class ImageJCV implements IClone, Cloneable {
	private IplImage image;
	
	public ImageJCV() {
	}
	
	public ImageJCV(IplImage image) {
		this.image = image;
	}
	
	public ImageJCV(ByteBuffer buffer) {
		this.image = (IplImage) ObjectByteConverter.bytesToObject(buffer.array());
	}
	
	public ImageJCV(ImageJCV other) {
		this.image = other.image.clone();
	}
	
	public ImageJCV(int width, int height){
		this.image = new IplImage();
		this.image.width(width);
		this.image.height(height);
	}
	
	public ImageJCV(double[][] data) {
		this.image = new IplImage();
		this.image.width(data[0].length);
		this.image.height(data.length);
		ByteBuffer buffer = image.getByteBuffer();
		for (int i=0; i < data.length; i++) {
			buffer.putDouble(i, data[i][0]);
		}
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
	public ImageJCV clone() {
		final ImageJCV image = new ImageJCV(this);
		return image;
	}
	
	@Override
	public String toString() {
		return "{Width: " + this.getWidth() + " Height: " + this.getHeight() + "}";
	}
	
	public void fill(int value) {
		throw new UnsupportedOperationException("Currently not implemented");
	}
}
