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
package cc.kuka.odysseus.image.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SmileDetection {
    private static final String SMILE_CASCADE_FILE = "haarcascades/haarcascade_smile.xml";
    private static final String FACE_CASCADE_FILE = "lbpcascades/lbpcascade_frontalface.xml";
    private CascadeClassifier faceClassifier;
    private CascadeClassifier smileClassifier;
    private VideoCapture capture;
    private DatagramSocket socket;

    private String host = "172.31.1.147";
    private int port = 30004;

    public void load() {

        {// Load face cascade file
            try (InputStream is = new FileInputStream(new File(SmileDetection.FACE_CASCADE_FILE))) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), SmileDetection.FACE_CASCADE_FILE.replace("/", "_"));
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
            try (InputStream is = new FileInputStream(new File(SmileDetection.SMILE_CASCADE_FILE))) {
                final File tmp = File.createTempFile(OdysseusConfiguration.instance.getHomeDir(), SmileDetection.SMILE_CASCADE_FILE.replace("/", "_"));
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
        this.capture = new VideoCapture();
        this.capture.open(0);

        try {
            socket = new DatagramSocket();
        }
        catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void run() {
        final Mat mat = new Mat();

        while ((this.capture != null) && (this.capture.isOpened())) {
            if (this.capture.read(mat)) {
                boolean smiling = false;
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

                try {
                    long timestamp = System.currentTimeMillis();

                    try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
                        try (DataOutputStream out = new DataOutputStream(byteOut)) {
                            out.writeLong(timestamp);
                            System.out.println("Smiling " + smiling);
                            if (smiling) {
                                out.writeByte(1);
                            }
                            else {
                                out.writeByte(0);
                            }
                            out.flush();
                            DatagramPacket packet = new DatagramPacket(byteOut.toByteArray(), byteOut.size(), InetAddress.getByName(host), port);
                            this.socket.send(packet);
Thread.sleep(500);
                        }
                    }
                }
                catch (Throwable e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        SmileDetection detection = new SmileDetection();
        detection.load();
        detection.run();
    }
}
