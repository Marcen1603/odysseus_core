/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.image.datahandler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ImageDataHandler extends AbstractDataHandler<BufferedImage> {
    static protected List<String> types = new ArrayList<>();
    static {
        ImageDataHandler.types.add("Image");
    }

    @Override
    public IDataHandler<BufferedImage> getInstance(final SDFSchema schema) {
        return new ImageDataHandler();
    }

    public ImageDataHandler() {
        super();
    }

    @Override
    public BufferedImage readData(final String string) {
        return this.readData(ByteBuffer.wrap(string.getBytes()));
    }

    @Override
    public BufferedImage readData(final ByteBuffer buffer) {
        try {
            return ImageIO.read(new ByteArrayInputStream(buffer.array()));
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BufferedImage readData(final InputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

    @Override
    public void writeData(final ByteBuffer buffer, final Object data) {
        final BufferedImage value = (BufferedImage) data;
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(value, "png", stream);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        buffer.put(stream.toByteArray());
    }

    @Override
    final public List<String> getSupportedDataTypes() {
        return ImageDataHandler.types;
    }

    @Override
    public int memSize(final Object attribute) {
        final BufferedImage image = (BufferedImage) attribute;
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return (stream.size());
    }

    @Override
    public Class<?> createsType() {
        return BufferedImage.class;
    }

}
