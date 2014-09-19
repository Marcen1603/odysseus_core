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
package de.uniol.inf.is.odysseus.wrapper.cameras;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.cameras.datatype.BaseImage;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class BaseImageDataHandler extends AbstractDataHandler<BaseImage> {
	static protected List<String> types = new ArrayList<String>();
	static 
	{
		BaseImageDataHandler.types.add("BaseImage");
	}

	@Override
	public IDataHandler<BaseImage> getInstance(final SDFSchema schema) {
		return new BaseImageDataHandler();
	}

	public BaseImageDataHandler() {
		super();
	}

	@Override
	public BaseImage readData(final ObjectInputStream inputStream) throws IOException 
	{
		return null;
		
/*		int width = inputStream.readInt();
		int height = inputStream.readInt();
		BaseImage image = new BaseImage(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.set(x, y, inputStream.readInt());
			}
		}
		return image;*/
	}

	@Override
	public BaseImage readData(final String string) 
	{
		return this.readData(ByteBuffer.wrap(string.getBytes()));
	}

	@Override
	public BaseImage readData(InputStream inputStream) throws IOException 
	{
		return null;
		
/*		BufferedImage bImage = ImageIO.read(inputStream);
		int width = bImage.getWidth();
		int height = bImage.getHeight();
		BaseImage image = new BaseImage(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.set(x, y, bImage.getRGB(x, y));
			}
		}
		return image;
*/
	}

	@Override
	public BaseImage readData(final ByteBuffer buffer) 
	{
		return null;
		
/*		int width = buffer.getInt();
		int height = buffer.getInt();
		BaseImage image = new BaseImage(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.set(x, y, buffer.getInt());
			}
		}
		return image;*/
	}

	@Override
	public void writeData(final ByteBuffer buffer, final Object data) 
	{
		final BaseImage value = (BaseImage) data;
		buffer.putInt(value.getWidth());
		buffer.putInt(value.getHeight());
		for (int x = 0; x < value.getWidth(); x++) {
			for (int y = 0; y < value.getHeight(); y++) {
				buffer.putInt(value.get(x, y));
			}
		}
	}

	@Override
	final public List<String> getSupportedDataTypes() {
		return BaseImageDataHandler.types;
	}

	@Override
	public int memSize(final Object attribute) {
		final BaseImage image = (BaseImage) attribute;
		return (2 * Integer.SIZE + image.getWidth() * image.getHeight()
				* Double.SIZE) / 8;
	}

	@Override
	public Class<?> createsType() {
		return BaseImage.class;
	}

}
