package de.uniol.inf.is.odysseus.wrapper.kinect.utils;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.BLUE_PATTERN;

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
        res /= (float) BLUE_PATTERN;
        return res;
    }

}
