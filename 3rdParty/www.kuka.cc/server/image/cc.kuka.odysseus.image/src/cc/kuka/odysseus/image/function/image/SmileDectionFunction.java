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
public class SmileDectionFunction extends AbstractFunction<Boolean> {
    /**
     *
     */
    private static final long serialVersionUID = 8555998855883960894L;
    private static final Logger LOG = LoggerFactory.getLogger(SmileDectionFunction.class);
    private static final String SMILE_CASCADE_FILE = "haarcascades/haarcascade_smile.xml";
    private static final String FACE_CASCADE_FILE = "lbpcascades/lbpcascade_frontalface.xml";
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { { SDFImageDatatype.IMAGE } };

    private CascadeClassifier faceClassifier;
    private CascadeClassifier smileClassifier;

    /**
     * Class constructor.
     *
     */
    public SmileDectionFunction() {
        super("smileDetection", 1, SmileDectionFunction.ACC_TYPES, SDFDatatype.BOOLEAN);
        {// Load face cascade file
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(SmileDectionFunction.FACE_CASCADE_FILE)) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), SmileDectionFunction.FACE_CASCADE_FILE.replace("/", "_"));
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
        {// Load smile cascade file
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(SmileDectionFunction.SMILE_CASCADE_FILE)) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), SmileDectionFunction.SMILE_CASCADE_FILE.replace("/", "_"));
                tmp.deleteOnExit();
                try (FileOutputStream os = new FileOutputStream(tmp)) {
                    final byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                this.smileClassifier = new CascadeClassifier(tmp.getAbsolutePath());

            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        if ((this.faceClassifier == null) || (this.faceClassifier.empty())) {
            SmileDectionFunction.LOG.error("Unable to load face classifier: {}", SmileDectionFunction.FACE_CASCADE_FILE);
        }
        if ((this.smileClassifier == null) || (this.smileClassifier.empty())) {
            SmileDectionFunction.LOG.error("Unable to load smile classifier: {}", SmileDectionFunction.SMILE_CASCADE_FILE);
        }
    }

    @Override
    public Boolean getValue() {
        final BufferedImage image = (BufferedImage) this.getInputValue(0);
        Objects.requireNonNull(image);
        boolean smiling = false;
        final Mat mat = OpenCVUtil.imageToIplImage(image);
        final Mat grayImage = new Mat(mat.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(mat, grayImage, Imgproc.COLOR_BGR2GRAY);
        mat.release();
        final MatOfRect faces = new MatOfRect();
        this.faceClassifier.detectMultiScale(grayImage, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(10, 10), new Size());
        final Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            final Rect face = facesArray[0];
            final Mat mROI = grayImage.submat(face);
            final MatOfRect smilingFace = new MatOfRect();
            this.smileClassifier.detectMultiScale(mROI, smilingFace, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(10, 10), new Size());
            final Rect[] smilingFaceArray = smilingFace.toArray();
            if (smilingFaceArray.length > 0) {
                smiling = true;
            }
            mROI.release();
        }
        faces.release();
        grayImage.release();
        return new Boolean(smiling);
    }
    
    
}
