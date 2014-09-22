package de.uniol.inf.is.odysseus.wrapper.kinect.utils;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.BLUE_PATTERN;

import java.io.FileOutputStream;
import java.io.InputStream;

public class Helper {
    /**
     * Helper method to convert a byte (color) to a openGL conform float (color).
     * @param b
     * Color byte.
     * @return
     * OpenGL color float.
     */
    public static float byteToFloat(byte b) {
        float res = (BLUE_PATTERN & b);
        res /= BLUE_PATTERN;
        return res;
    }
    
	public static String getFileToResource(InputStream resource, String tmpName) {
		String tmpPath = System.getProperty("java.io.tmpdir");
		if (tmpPath == null || tmpPath.length() == 0) {
			tmpPath = System.getProperty("user.dir");
		}

		tmpPath += "/"+tmpName;
		try (FileOutputStream out = new FileOutputStream(tmpPath)) {
			byte[] buffer = new byte[1024];

			int readBytes = 0;
			while ((readBytes = resource.read(buffer)) != -1) {
				out.write(buffer, 0, readBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tmpPath;
	}
}
