package de.uniol.inf.is.odysseus.wrapper.kinect.utils;

/**
 * Some global constants.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public final class Constants {
    /**
     * Hide standard constructor.
     */
    private Constants() {
    }

    /** Defines 1000 ms in one second. */
    public static final int MILLISECONDS_IN_SECOND = 1000;
    
    /** Factor to multiply a mm value, to get meter. */
    public static final float MILLIMETER_TO_METER_FACTOR = 0.001f; 

    /** Size of integer in bytes. */
    public static final int INTEGER_BYTES_SIZE = Integer.SIZE / Byte.SIZE;

    /** Size of short in bytes. */
    public static final int SHORT_BYTES_SIZE = Short.SIZE / Byte.SIZE;

    /** Defines a red pattern. */
    public static final int RED_PATTERN = 0xff0000;

    /** Defines a green pattern. */
    public static final int GREEN_PATTERN = 0xff00;

    /** Defines a blue pattern. */
    public static final int BLUE_PATTERN = 0xff;
    
    /** Defines simple 0.5 constant. */
    public static final float ONE_HALF = 0.5f;
    
    /** Marker byte for a color map. */
    public static final byte COLORMAP_MARKER = 0x1;
    
    /** Marker byte for a depth map. */
    public static final byte DEPTHMAP_MARKER = 0x2;
    
    /** Marker byte for a skeleton map. */
    public static final byte SKELETON_MARKER = 0x3;
    
    /** Count of joints in one skeletal data. */
    public static final int COUNT_JOINTS = 15;
}
