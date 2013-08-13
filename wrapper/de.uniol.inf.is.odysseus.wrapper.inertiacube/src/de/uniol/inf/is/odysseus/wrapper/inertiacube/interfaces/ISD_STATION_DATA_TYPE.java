package de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces;
import com.ochafik.lang.jnaerator.runtime.Structure;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool;
import java.util.Arrays;
import java.util.List;
/**
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ISD_STATION_DATA_TYPE extends Structure<ISD_STATION_DATA_TYPE, ISD_STATION_DATA_TYPE.ByValue, ISD_STATION_DATA_TYPE.ByReference > {
	/** Tracking status, represents "Tracking Quality" (0-255; 0 if lost) */
	public byte TrackingStatus;
	/** TRUE if data changed since last call to ISD_GetTrackingData */
	public byte NewData;
	/** Communication integrity (percentage of packets received from tracker, 0-100) */
	public byte CommIntegrity;
	/** Wireless devices only 0=N/A, 1=Low, 2=OK */
	public byte BatteryState;
	/**
	 * Orientation in Euler angle format (Yaw, Pitch, Roll)<br>
	 * C type : float[3]
	 */
	public float[] Euler = new float[3];
	/**
	 * Orientation in Quaternion format (W,X,Y,Z)<br>
	 * C type : float[4]
	 */
	public float[] Quaternion = new float[4];
	/**
	 * Always in meters<br>
	 * C type : float[3]
	 */
	public float[] Position = new float[3];
	/** Timestamp in seconds, reported only if requested */
	public float TimeStamp;
	/** InertiaCube and PC-Tracker products only, whether sensor is still */
	public float StillTime;
	/** Battery voltage, if available */
	public float BatteryLevel;
	/** Magnetometer heading, computed based on current orientation. */
	public float CompassYaw;
	/**
	 * Only if requested<br>
	 * C type : Bool[8]
	 */
	public Bool[] ButtonState = new Bool[8];
	/**
	 * Only if requested<br>
	 * C type : short[10]
	 */
	public short[] AnalogData = new short[10];
	/** C type : BYTE[4] */
	public byte[] AuxInputs = new byte[4];
	/**
	 * rad/sec, in sensor body coordinate frame.<br>
	 * C type : float[3]
	 */
	public float[] AngularVelBodyFrame = new float[3];
	/**
	 * rad/sec, in world coordinate frame, with boresight and other<br>
	 * C type : float[3]
	 */
	public float[] AngularVelNavFrame = new float[3];
	/**
	 * meter/sec^2, in sensor body coordinate frame. These are<br>
	 * C type : float[3]
	 */
	public float[] AccelBodyFrame = new float[3];
	/**
	 * meters/sec^2, in the navigation (earth) coordinate frame.<br>
	 * C type : float[3]
	 */
	public float[] AccelNavFrame = new float[3];
	/**
	 * meters/sec, 6-DOF systems only.<br>
	 * C type : float[3]
	 */
	public float[] VelocityNavFrame = new float[3];
	/**
	 * Raw gyro output, only factory calibration is applied.<br>
	 * C type : float[3]
	 */
	public float[] AngularVelRaw = new float[3];
	/** Ultrasonic Measurement Quality (IS-900 only, firmware >= 4.26) */
	public byte MeasQuality;
	public byte bReserved2;
	public byte bReserved3;
	public byte bReserved4;
	/** Time Stamp in whole seconds. */
	public int TimeStampSeconds;
	/** Fractional part of the Time Stamp in micro-seconds. */
	public int TimeStampMicroSec;
	/** Data record arrival time stamp based on OS time, */
	public int OSTimeStampSeconds;
	/** reserved for future use, not implemented. */
	public int OSTimeStampMicroSec;
	/** C type : float[55] */
	public float[] Reserved = new float[55];
	/** Station temperature in degrees C (3DOF sensors only) */
	public float Temperature;
	/**
	 * 3DOF sensors only<br>
	 * C type : float[3]
	 */
	public float[] MagBodyFrame = new float[3];
	public ISD_STATION_DATA_TYPE() {
		super();
	}
	protected List<? > getFieldOrder() {
		return Arrays.asList("TrackingStatus", "NewData", "CommIntegrity", "BatteryState", "Euler", "Quaternion", "Position", "TimeStamp", "StillTime", "BatteryLevel", "CompassYaw", "ButtonState", "AnalogData", "AuxInputs", "AngularVelBodyFrame", "AngularVelNavFrame", "AccelBodyFrame", "AccelNavFrame", "VelocityNavFrame", "AngularVelRaw", "MeasQuality", "bReserved2", "bReserved3", "bReserved4", "TimeStampSeconds", "TimeStampMicroSec", "OSTimeStampSeconds", "OSTimeStampMicroSec", "Reserved", "Temperature", "MagBodyFrame");
	}
	protected ByReference newByReference() { return new ByReference(); }
	protected ByValue newByValue() { return new ByValue(); }
	protected ISD_STATION_DATA_TYPE newInstance() { return new ISD_STATION_DATA_TYPE(); }
	public static ISD_STATION_DATA_TYPE[] newArray(int arrayLength) {
		return Structure.newArray(ISD_STATION_DATA_TYPE.class, arrayLength);
	}
	public static class ByReference extends ISD_STATION_DATA_TYPE implements Structure.ByReference {
		
	};
	public static class ByValue extends ISD_STATION_DATA_TYPE implements Structure.ByValue {
		
	};
}
