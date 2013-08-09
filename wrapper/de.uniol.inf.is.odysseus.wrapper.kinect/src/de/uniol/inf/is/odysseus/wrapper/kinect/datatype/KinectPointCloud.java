package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import javax.media.opengl.GL2;

import org.OpenNI.Point3D;

import de.uniol.inf.is.odysseus.wrapper.kinect.openNI.ContextHandler;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.ONE_HALF;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.MILLIMETER_TO_METER_FACTOR;
import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.BLUE_PATTERN;

/**
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectPointCloud {
    /** Stores the color map. */
    private KinectColorMap colorMap;

    /** Stores the depth map. */
    private KinectDepthMap depthMap;

    /** Width of the image. */
    private int width;

    /** Height of the image. */
    private int height;
    
    /** Horizontal field of view of the depth camera. */
    private float fovH;
    
    /** Vertical field of view of the depth camera. */
    private float fovV;

    /**
     * Point cloud from a {@link KinectColorMap} and a {@link KinectDepthMap}.
     * @param cMap
     * {@link KinectColorMap}.
     * @param dMap
     * {@link KinectDepthMap}.
     */
    public KinectPointCloud(KinectColorMap cMap, KinectDepthMap dMap) {
        this.colorMap = cMap;
        this.depthMap = dMap;
        this.width = cMap.getWidth();
        this.height = cMap.getHeight();
        this.fovH = ContextHandler.getInstance().getFovH();
        this.fovV = ContextHandler.getInstance().getFovV();
    }

    /**
     * Helper method to unproject a given point from an image with a given distance z to a
     * 3D coordinate in the world space.
     * @param x
     * x coordinate in the image.
     * @param y
     * y coordinate in the image.
     * @param z
     * depth of the pixel (range from camera).
     * @return 3D representation in world space of the given pixel.
     */
    private Point3D unproject(float x, float y, float z) {
        Point3D res = new Point3D();

        float angleHor = (x / width - ONE_HALF) * fovH;
        float angleVer = (-y / height + ONE_HALF) * fovV;
        float tanHor = (float) Math.tan(angleHor);
        float tanVer = (float) Math.tan(angleVer);

        float resX = tanHor * z;
        float resY = tanVer * z;
        float resZ = -z;
        res.setPoint(resX, resY, resZ);

        return res;
    }

    /**
     * Renders the point cloud to the given GL object.
     * @param gl
     * GL object to render on.
     */
    public void render(GL2 gl) {
        byte[] colorBytes = colorMap.getData();
        short[] depthShorts = depthMap.getData();
        gl.glBegin(GL2.GL_POINTS);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                float z = depthShorts[index] * MILLIMETER_TO_METER_FACTOR;
                if (z < MILLIMETER_TO_METER_FACTOR) {
                    continue;
                }

                Point3D p = unproject(x, y, z);

                gl.glColor3f(byteToFloat(colorBytes[index * KinectColorMap.DEPTH]),
                        byteToFloat(colorBytes[index * KinectColorMap.DEPTH + 1]),
                        byteToFloat(colorBytes[index * KinectColorMap.DEPTH + 2]));
                gl.glVertex3f(p.getX(), p.getY(), p.getZ());
            }
        }
        gl.glEnd();
    }

    /**
     * Helper method to convert a byte (color) to a openGL conform float (color).
     * @param b
     * Color byte.
     * @return
     * OpenGL color float.
     */
    private float byteToFloat(byte b) {
        float res = (BLUE_PATTERN & b);
        res /= (float) BLUE_PATTERN;
        return res;
    }
}
