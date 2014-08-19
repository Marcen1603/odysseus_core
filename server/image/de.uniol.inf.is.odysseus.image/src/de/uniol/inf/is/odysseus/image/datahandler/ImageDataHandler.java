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
package de.uniol.inf.is.odysseus.image.datahandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ImageDataHandler extends AbstractDataHandler<Image> {
	static protected List<String> types = new ArrayList<String>();
	static {
		ImageDataHandler.types.add("Image");
	}

	@Override
	public IDataHandler<Image> getInstance(final SDFSchema schema) {
		return new ImageDataHandler();
	}

	public ImageDataHandler() {
		super();
	}

	@Override
	public Image readData(final ObjectInputStream inputStream)
			throws IOException {
		int width = inputStream.readInt();
		int height = inputStream.readInt();
		Image image = new Image(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.set(x, y, inputStream.readDouble());
			}
		}
		return image;
	}

	@Override
	public Image readData(final String string) {
		return this.readData(ByteBuffer.wrap(string.getBytes()));
	}

	@Override
	public Image readData(InputStream inputStream) throws IOException {
		BufferedImage bImage = ImageIO.read(inputStream);
		int width = bImage.getWidth();
		int height = bImage.getHeight();
		Image image = new Image(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.set(x, y, bImage.getRGB(x, y));
			}
		}
		return image;

	}

	@Override
	public Image readData(final ByteBuffer buffer) {
		int width = buffer.getInt();
		int height = buffer.getInt();
		Image image = new Image(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.set(x, y, buffer.getDouble());
			}
		}
		return image;
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) {
		final Image value = (Image) data;
		buffer.putInt(value.getWidth());
		buffer.putInt(value.getHeight());
		for (int x = 0; x < value.getWidth(); x++) {
			for (int y = 0; y < value.getHeight(); y++) {
				buffer.putDouble(value.get(x, y));
			}
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return ImageDataHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		final Image image = (Image) attribute;
		return (2 * Integer.SIZE + image.getWidth() * image.getHeight()
				* Double.SIZE) / 8;
	}

	@Override
	public Class<?> createsType() {
		return Image.class;
	}

}
