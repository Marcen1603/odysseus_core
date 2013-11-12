package de.uniol.inf.is.odysseus.wrapper.kinect.datatype;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.ONE_HALF;

import org.openni.Point3D;

import de.uniol.inf.is.odysseus.wrapper.kinect.openNI.ContextHandler;

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
    public static Point3D unproject(KinectPointCloud pointCloud, float x, float y, float z) {
        Point3D res = new Point3D();

        float angleHor = (x / pointCloud.width - ONE_HALF) * pointCloud.fovH;
        float angleVer = (-y / pointCloud.height + ONE_HALF) * pointCloud.fovV;
        float tanHor = (float) Math.tan(angleHor);
        float tanVer = (float) Math.tan(angleVer);

        float resX = tanHor * z;
        float resY = tanVer * z;
        float resZ = -z;
        res.setPoint(resX, resY, resZ);

        return res;
    }

	public KinectColorMap getColorMap() {
		return colorMap;
	}

	public void setColorMap(KinectColorMap colorMap) {
		this.colorMap = colorMap;
	}

	public KinectDepthMap getDepthMap() {
		return depthMap;
	}

	public void setDepthMap(KinectDepthMap depthMap) {
		this.depthMap = depthMap;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getFovH() {
		return fovH;
	}

	public void setFovH(float fovH) {
		this.fovH = fovH;
	}

	public float getFovV() {
		return fovV;
	}

	public void setFovV(float fovV) {
		this.fovV = fovV;
	}
    
    
}
