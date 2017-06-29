/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.image.function.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.image.common.sdf.schema.SDFImageDatatype;
import cc.kuka.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FaceDectionFunction extends AbstractFunction<BufferedImage> {
    /**
     *
     */
    private static final long serialVersionUID = 9163862847734335878L;
    private static final Logger LOG = LoggerFactory.getLogger(FaceDectionFunction.class);
    private static final String FACE_CASCADE_FILE = "lbpcascades/lbpcascade_frontalface.xml";
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE } };

    private CascadeClassifier faceClassifier;

    /**
     * Class constructor.
     *
     */
    public FaceDectionFunction() {
        super("faceDetection", 1, FaceDectionFunction.ACC_TYPES, SDFImageDatatype.IMAGE);

        {// Load face cascade file
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(FaceDectionFunction.FACE_CASCADE_FILE)) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), FaceDectionFunction.FACE_CASCADE_FILE.replace("/", "_"));
                tmp.deleteOnExit();
                try (FileOutputStream os = new FileOutputStream(tmp)) {
                    final byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                this.faceClassifier = new CascadeClassifier(tmp.getAbsolutePath());

            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        if ((this.faceClassifier == null) || (this.faceClassifier.empty())) {
            FaceDectionFunction.LOG.error("Unable to load face classifier: {}", FaceDectionFunction.FACE_CASCADE_FILE);
        }
    }

    @Override
    public BufferedImage getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        Objects.requireNonNull(image);
        final Mat mat = OpenCVUtil.imageToIplImage(image);
        final Mat grayImage = new Mat(mat.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(mat, grayImage, Imgproc.COLOR_BGR2GRAY);
        final MatOfRect faces = new MatOfRect();
        this.faceClassifier.detectMultiScale(grayImage, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(10, 10), new Size());

        grayImage.release();
        final Rect[] facesArray = faces.toArray();
        BufferedImage faceImage = null;
        if (facesArray.length > 0) {
            final Rect face = facesArray[0];
            final Mat mROI = mat.submat(face);
            faceImage = OpenCVUtil.iplImageToImage(mROI);
        }
        mat.release();

        return faceImage;
    }
}
