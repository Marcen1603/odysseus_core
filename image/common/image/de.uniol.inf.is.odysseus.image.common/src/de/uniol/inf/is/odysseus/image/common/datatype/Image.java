/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.image.common.datatype;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @author Marco Grawunder
 * 
 */
public class Image implements IClone, Cloneable {

	private BufferedImage image;

	public Image() {
	}
	
	public Image(BufferedImage image) {
		this.image = image;
	}
	
	public Image(ByteBuffer buffer){
		this.image = (BufferedImage) ObjectByteConverter.bytesToObject(buffer.array());
	}
	
	public Image(Image other) {
		this.image = deepCopy(other.image);
	}

	public Image(int width, int height){
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);		
	}
	
	public Image(double[][] data) {
		image = new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		for (int i = 0; i < data.length; i++) {
            raster.setPixels(i, 0, data[i].length, 1, data[i]);
		}
	}
	
	public double[][] getMatrix() {
		throw new UnsupportedOperationException("Currenlty not implemented");
		
//		WritableRaster raster = image.getRaster();
//		raster.getPixels(x, y, w, h, iArray);
//		return null;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null)
				.getSubimage(0, 0, bi.getWidth(), bi.getHeight());
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return this.image.getWidth();
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return this.image.getHeight();
	}

	public int get(final int x, final int y) {
		return this.image.getRGB(x, y);
	}

	public void set(int x, int y, int value) {
		this.image.setRGB(x, y, value);
	}

	public void writeData(ByteBuffer buffer){
		byte[] bytes = ObjectByteConverter.objectToBytes(this.image);
		buffer.put(bytes);
	}	
	
	@Override
	public Image clone() {
		final Image image = new Image(this);
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
