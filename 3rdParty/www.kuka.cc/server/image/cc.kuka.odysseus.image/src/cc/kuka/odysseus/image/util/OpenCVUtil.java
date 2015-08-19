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
package cc.kuka.odysseus.image.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class OpenCVUtil {
    private OpenCVUtil() {
    }

    public static Mat imageToIplImage(final BufferedImage image) {
        final Mat mat;
        if ((image.getType() == BufferedImage.TYPE_INT_RGB) || (image.getType() == BufferedImage.TYPE_INT_ARGB)) {
            mat = new Mat(new Size(image.getWidth(), image.getHeight()), CvType.CV_8UC3);
            final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, pixels);
        }
        else if ((image.getType() == BufferedImage.TYPE_INT_BGR) || (image.getType() == BufferedImage.TYPE_3BYTE_BGR)) {
            mat = new Mat(new Size(image.getWidth(), image.getHeight()), CvType.CV_8UC3);
            final byte[] pixels = new byte[image.getWidth() * image.getHeight() * (int) mat.elemSize()];
            final int[] buffer = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            for (int i = 0; i < buffer.length; i++) {
                pixels[(i * 3) + 2] = (byte) ((buffer[i] >> 16) & 0xFF);
                pixels[(i * 3) + 1] = (byte) ((buffer[i] >> 8) & 0xFF);
                pixels[i * 3] = (byte) ((buffer[i] >> 0) & 0xFF);
            }
            mat.put(0, 0, pixels);
        }
        else {
            mat = new Mat(new Size(image.getWidth(), image.getHeight()), CvType.CV_8UC1);
            final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            mat.put(0, 0, pixels);
        }
        return mat;
    }

    public static BufferedImage iplImageToImage(final Mat mat) {
        int type;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }
        else {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        final BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        mat.release();
        return image;
    }

}
