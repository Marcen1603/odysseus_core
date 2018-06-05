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
package cc.kuka.odysseus.image.physicaloperator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kuka.odysseus.image.logicaloperator.EyeTrackerAO;
import cc.kuka.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * Eye tracker operator to estimate the relative position of eyes in a face.
 *
 * Code based on the tutorial given by Roman Hošek
 * (http://romanhosek.cz/android-eye-detection-and-tracking-with-opencv/)
 *
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class EyeTrackerPO<T extends Tuple<?>> extends AbstractPipe<T, T> {
    private static final Logger LOG = LoggerFactory.getLogger(EyeTrackerPO.class);

    // private static final String FACE_CASCADE_FILE =
    // "haarcascades/haarcascade_frontalface_alt.xml";
    private static final String FACE_CASCADE_FILE = "lbpcascades/lbpcascade_frontalface.xml";
    private static final String LEFT_EYE_CASCADE_FILE = "haarcascades/haarcascade_lefteye_2splits.xml";
    private static final String RIGHT_EYE_CASCADE_FILE = "haarcascades/haarcascade_righteye_2splits.xml";

    private final int pos;
    private CascadeClassifier faceClassifier;
    private CascadeClassifier leftEyeClassifier;
    private CascadeClassifier rightEyeClassifier;
    private Mat rightTemplate;
    private Mat leftTemplate;

    private static final int TM_SQDIFF = 0;
    private static final int TM_SQDIFF_NORMED = 1;
    private static final int TM_CCOEFF = 2;
    private static final int TM_CCOEFF_NORMED = 3;
    private static final int TM_CCORR = 4;
    private static final int TM_CCORR_NORMED = 5;

    /**
     * Class constructor.
     *
     */
    public EyeTrackerPO(final EyeTrackerAO operator) {
        this.pos = operator.getInputSchema(0).indexOf(operator.getAttribute());
        {// Load face cascade file
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(EyeTrackerPO.FACE_CASCADE_FILE)) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), EyeTrackerPO.FACE_CASCADE_FILE.replace("/", "_"));
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
        {// Load right eye cascade file
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(EyeTrackerPO.RIGHT_EYE_CASCADE_FILE)) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), EyeTrackerPO.RIGHT_EYE_CASCADE_FILE.replace("/", "_"));
                tmp.deleteOnExit();
                try (FileOutputStream os = new FileOutputStream(tmp)) {
                    final byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                this.rightEyeClassifier = new CascadeClassifier(tmp.getAbsolutePath());

            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        {// Load left eye cascade file
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(EyeTrackerPO.LEFT_EYE_CASCADE_FILE)) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), EyeTrackerPO.LEFT_EYE_CASCADE_FILE.replace("/", "_"));
                tmp.deleteOnExit();
                try (FileOutputStream os = new FileOutputStream(tmp)) {
                    final byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
                this.leftEyeClassifier = new CascadeClassifier(tmp.getAbsolutePath());

            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        if ((this.faceClassifier == null) || (this.faceClassifier.empty())) {
            EyeTrackerPO.LOG.error("Unable to load face classifier: {}", EyeTrackerPO.FACE_CASCADE_FILE);
        }
        if ((this.rightEyeClassifier == null) || (this.rightEyeClassifier.empty())) {
            EyeTrackerPO.LOG.error("Unable to load right eye classifier: {}", EyeTrackerPO.RIGHT_EYE_CASCADE_FILE);
        }
        if ((this.leftEyeClassifier == null) || (this.leftEyeClassifier.empty())) {
            EyeTrackerPO.LOG.error("Unable to load left eye classifier: {}", EyeTrackerPO.LEFT_EYE_CASCADE_FILE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPunctuation(final IPunctuation punctuation, final int port) {
        this.sendPunctuation(punctuation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_next(final T object, final int port) {
        final Mat image = OpenCVUtil.imageToIplImage((BufferedImage) object.getAttribute(this.pos));
        final Mat grayImage = new Mat(image.size(), CvType.CV_8UC1);
        final List<Double> positions = new ArrayList<>();

        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        final MatOfRect faces = new MatOfRect();
        this.faceClassifier.detectMultiScale(grayImage, faces, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(10, 10), new Size());
        final Rect[] facesArray = faces.toArray();
        if (facesArray.length > 0) {
            final Rect face = facesArray[0];
            final Rect rightEye = new Rect(face.x + (face.width / 16), (int) (face.y + (face.height / 4.5)), (face.width - ((2 * face.width) / 16)) / 2, (int) (face.height / 3.0));
            final Rect leftEye = new Rect(face.x + (face.width / 16) + ((face.width - ((2 * face.width) / 16)) / 2), (int) (face.y + (face.height / 4.5)), (face.width - ((2 * face.width) / 16)) / 2,
                    (int) (face.height / 3.0));

            if ((this.rightTemplate == null) || (this.leftTemplate == null) || (port == 1)) {
                this.rightTemplate = EyeTrackerPO.getTemplate(grayImage, this.rightEyeClassifier, rightEye, 24);
                this.leftTemplate = EyeTrackerPO.getTemplate(grayImage, this.leftEyeClassifier, leftEye, 24);
            }
            if (port == 0) {
                if ((rightEye.width >= this.rightTemplate.cols()) && (rightEye.height >= this.rightTemplate.rows()) && (leftEye.width >= this.leftTemplate.cols())
                        && (leftEye.height >= this.leftTemplate.rows())) {
                    final Point right = EyeTrackerPO.matchEye(grayImage, rightEye, this.rightTemplate, EyeTrackerPO.TM_SQDIFF);
                    final Point left = EyeTrackerPO.matchEye(grayImage, leftEye, this.leftTemplate, EyeTrackerPO.TM_SQDIFF);
                    if ((right != null) && (left != null)) {
                        final double relRightPositionX = (right.x - rightEye.x) / rightEye.width;
                        final double relRightPositionY = (right.y - rightEye.y) / rightEye.height;
                        final double relLeftPositionX = (left.x - leftEye.x) / leftEye.width;
                        final double relLeftPositionY = (left.y - leftEye.y) / leftEye.height;
                        positions.add(new Double(relRightPositionX));
                        positions.add(new Double(relRightPositionY));
                        positions.add(new Double(relLeftPositionX));
                        positions.add(new Double(relLeftPositionY));
                    }
                }
            }
        }
        grayImage.release();
        if (port == 0) {
            object.setAttribute(this.pos, positions);
            this.transfer(object);
        }
    }

    private static Point matchEye(final Mat image, final Rect area, final Mat template, final int type) {
        Point matchLoc;
        final Mat mROI = image.submat(area);
        final int result_cols = (image.cols() - template.cols()) + 1;
        final int result_rows = (image.rows() - template.rows()) + 1;
        if ((template.cols() == 0) || (template.rows() == 0)) {
            return null;
        }
        final Mat result = new Mat(result_cols, result_rows, CvType.CV_8U);

        switch (type) {
            case TM_SQDIFF:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_SQDIFF);
                break;
            case TM_SQDIFF_NORMED:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_SQDIFF_NORMED);
                break;
            case TM_CCOEFF:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_CCOEFF);
                break;
            case TM_CCOEFF_NORMED:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_CCOEFF_NORMED);
                break;
            case TM_CCORR:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_CCORR);
                break;
            case TM_CCORR_NORMED:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_CCORR_NORMED);
                break;
            default:
                Imgproc.matchTemplate(mROI, template, result, Imgproc.TM_SQDIFF);
                break;
        }

        final Core.MinMaxLocResult minMaxLoc = Core.minMaxLoc(result);

        if ((type == EyeTrackerPO.TM_SQDIFF) || (type == EyeTrackerPO.TM_SQDIFF_NORMED)) {
            matchLoc = minMaxLoc.minLoc;
        }
        else {
            matchLoc = minMaxLoc.maxLoc;
        }
        return new Point((matchLoc.x + (template.cols() / 2.0) + area.x), (matchLoc.y + (template.rows() / 2.0) + area.y));

    }

    private static Mat getTemplate(final Mat image, final CascadeClassifier clasificator, final Rect area, final int size) {
        Mat template = new Mat();
        Mat mROI = image.submat(area);
        final MatOfRect eyes = new MatOfRect();
        final Point iris = new Point();
        Rect eye_template = new Rect();
        clasificator.detectMultiScale(mROI, eyes, 1.15, 2, Objdetect.CASCADE_FIND_BIGGEST_OBJECT | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size());

        final Rect[] eyesArray = eyes.toArray();
        if (eyesArray.length > 0) {
            final Rect e = eyesArray[0];
            e.x = area.x + e.x;
            e.y = area.y + e.y;
            final Rect eye_only_rectangle = new Rect((int) e.tl().x, (int) (e.tl().y + (e.height * 0.4)), e.width, (int) (e.height * 0.6));
            mROI = image.submat(eye_only_rectangle);
            final Core.MinMaxLocResult mmG = Core.minMaxLoc(mROI);
            iris.x = mmG.minLoc.x + eye_only_rectangle.x;
            iris.y = mmG.minLoc.y + eye_only_rectangle.y;
            eye_template = new Rect((int) iris.x - (size / 2), (int) iris.y - (size / 2), size, size);
            template = (image.submat(eye_template)).clone();
        }
        return template;
    }

}
