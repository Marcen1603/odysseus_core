package de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.ochafik.lang.jnaerator.runtime.MangledFunctionMapper;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;
/**
 * JNA Wrapper for library <b>de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces</b><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class ISenseLib implements Library {
	public static final String JNA_LIBRARY_NAME = "isense"; //LibraryExtractor.getLibraryPath("isense", true, ISenseLib.class);
	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(ISenseLib.JNA_LIBRARY_NAME, MangledFunctionMapper.DEFAULT_OPTIONS);
	static {
		Native.register(ISenseLib.JNA_LIBRARY_NAME);
	}
	/** enum values */
	public static interface ISD_SYSTEM_TYPE {
		/**
		 * Not found, or unable to identify<br>
		 * <i>native declaration : line 95</i>
		 */
		public static final int ISD_NONE = 0;
		/**
		 * InertiaCubes, NavChip, IS-300, IS-600, IS-900 and IS-1200<br>
		 * <i>native declaration : line 96</i>
		 */
		public static final int ISD_PRECISION_SERIES = 1;
		/**
		 * InterTrax<br>
		 * <i>native declaration : line 97</i>
		 */
		public static final int ISD_INTERTRAX_SERIES = 2;
	};
	/** enum values */
	public static interface ISD_SYSTEM_MODEL {
		/** <i>native declaration : line 105</i> */
		public static final int ISD_UNKNOWN = 0;
		/**
		 * 3DOF system (unsupported)<br>
		 * <i>native declaration : line 106</i>
		 */
		public static final int ISD_IS300 = 1;
		/**
		 * 6DOF system (unsupported)<br>
		 * <i>native declaration : line 107</i>
		 */
		public static final int ISD_IS600 = 2;
		/**
		 * 6DOF system<br>
		 * <i>native declaration : line 108</i>
		 */
		public static final int ISD_IS900 = 3;
		/**
		 * InterTrax (Serial) (unsupported)<br>
		 * <i>native declaration : line 109</i>
		 */
		public static final int ISD_INTERTRAX = 4;
		/**
		 * InterTrax (USB) (unsupported)<br>
		 * <i>native declaration : line 110</i>
		 */
		public static final int ISD_INTERTRAX_2 = 5;
		/**
		 * InterTraxLS, verification required (unsupported)<br>
		 * <i>native declaration : line 111</i>
		 */
		public static final int ISD_INTERTRAX_LS = 6;
		/**
		 * InterTraxLC (unsupported)<br>
		 * <i>native declaration : line 112</i>
		 */
		public static final int ISD_INTERTRAX_LC = 7;
		/**
		 * InertiaCube2<br>
		 * <i>native declaration : line 113</i>
		 */
		public static final int ISD_ICUBE2 = 8;
		/**
		 * InertiaCube2 Pro<br>
		 * <i>native declaration : line 114</i>
		 */
		public static final int ISD_ICUBE2_PRO = 9;
		/**
		 * 6DOF system<br>
		 * <i>native declaration : line 115</i>
		 */
		public static final int ISD_IS1200 = 10;
		/**
		 * InertiaCube3<br>
		 * <i>native declaration : line 116</i>
		 */
		public static final int ISD_ICUBE3 = 11;
		/**
		 * NavChip<br>
		 * <i>native declaration : line 117</i>
		 */
		public static final int ISD_NAVCHIP = 12;
		/**
		 * InterTrax3 (unsupported)<br>
		 * <i>native declaration : line 118</i>
		 */
		public static final int ISD_INTERTRAX_3 = 13;
		/**
		 * K-Sensor<br>
		 * <i>native declaration : line 119</i>
		 */
		public static final int ISD_IMUK = 14;
		/**
		 * InertiaCube2B Pro<br>
		 * <i>native declaration : line 120</i>
		 */
		public static final int ISD_ICUBE2B_PRO = 15;
		/**
		 * InertiaCube2 Plus<br>
		 * <i>native declaration : line 121</i>
		 */
		public static final int ISD_ICUBE2_PLUS = 16;
		/**
		 * InertiaCube BT<br>
		 * <i>native declaration : line 122</i>
		 */
		public static final int ISD_ICUBE_BT = 17;
		/**
		 * InertiaCube4<br>
		 * <i>native declaration : line 123</i>
		 */
		public static final int ISD_ICUBE4 = 18;
	};
	/** enum values */
	public static interface ISD_INTERFACE_TYPE {
		/** <i>native declaration : line 130</i> */
		public static final int ISD_INTERFACE_UNKNOWN = 0;
		/** <i>native declaration : line 131</i> */
		public static final int ISD_INTERFACE_SERIAL = 1;
		/** <i>native declaration : line 132</i> */
		public static final int ISD_INTERFACE_USB = 2;
		/** <i>native declaration : line 133</i> */
		public static final int ISD_INTERFACE_ETHERNET_UDP = 3;
		/** <i>native declaration : line 134</i> */
		public static final int ISD_INTERFACE_ETHERNET_TCP = 4;
		/** <i>native declaration : line 135</i> */
		public static final int ISD_INTERFACE_IOCARD = 5;
		/** <i>native declaration : line 136</i> */
		public static final int ISD_INTERFACE_PCMCIA = 6;
		/** <i>native declaration : line 137</i> */
		public static final int ISD_INTERFACE_FILE = 7;
		/** <i>native declaration : line 138</i> */
		public static final int ISD_INTERFACE_PIPE = 8;
	};
	/** enum values */
	public static interface ISD_AUX_SYSTEM_TYPE {
		/** <i>native declaration : line 462</i> */
		public static final int ISD_AUX_SYSTEM_NONE = 0;
		/** <i>native declaration : line 463</i> */
		public static final int ISD_AUX_SYSTEM_ULTRASONIC = 1;
		/** <i>native declaration : line 464</i> */
		public static final int ISD_AUX_SYSTEM_OPTICAL = 2;
		/** <i>native declaration : line 465</i> */
		public static final int ISD_AUX_SYSTEM_MAGNETIC = 3;
		/** <i>native declaration : line 466</i> */
		public static final int ISD_AUX_SYSTEM_RF = 4;
		/** <i>native declaration : line 467</i> */
		public static final int ISD_AUX_SYSTEM_GPS = 5;
	};
	public static final int ISD_MAX_CHANNELS = 10;
	public static final int ISD_MAX_BUTTONS = 8;
	public static final int ISD_DEFAULT_FRAME = 1;
	public static final int ISD_MAX_AUX_OUTPUTS = 4;
	public static final int TRUE = 1;
	public static final int ISD_VSET_FRAME = 2;
	public static final int ISD_MAX_STATIONS = 8;
	public static final int ISD_MAX_TRACKERS = 32;
	public static final int ISD_QUATERNION = 2;
	public static final String ISLIB_VERSION = "4.2381";
	public static final int ISD_MAX_AUX_INPUTS = 4;
	public static final int FALSE = 0;
	public static final int ISD_EULER = 1;
	public interface DLL_EP extends Callback {
		void apply();
	};
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>ISD_TRACKER_HANDLE ISD_OpenTracker(Hwnd, DWORD, Bool, Bool)</code><br>
	 * <i>native declaration : line 650</i>
	 */
	public static native int ISD_OpenTracker(ISenseLib.Hwnd hParent, int commPort, ISenseLib.Bool infoScreen, ISenseLib.Bool verbose);
	/**
	 * trackers, and should point to an ISD_TRACKER_HANDLE array of size ISD_MAX_TRACKERS<br>
	 * Original signature : <code>DWORD ISD_OpenAllTrackers(Hwnd, ISD_TRACKER_HANDLE*, Bool, Bool)</code><br>
	 * <i>native declaration : line 660</i><br>
	 * @deprecated use the safer methods {@link #ISD_OpenAllTrackers(de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Hwnd, java.nio.IntBuffer, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool)} and {@link #ISD_OpenAllTrackers(de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Hwnd, com.sun.jna.ptr.IntByReference, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool)} instead
	 */
	@Deprecated 
	public static native int ISD_OpenAllTrackers(ISenseLib.Hwnd hParent, IntByReference handle, ISenseLib.Bool infoScreen, ISenseLib.Bool verbose);
	/**
	 * trackers, and should point to an ISD_TRACKER_HANDLE array of size ISD_MAX_TRACKERS<br>
	 * Original signature : <code>DWORD ISD_OpenAllTrackers(Hwnd, ISD_TRACKER_HANDLE*, Bool, Bool)</code><br>
	 * <i>native declaration : line 660</i>
	 */
	public static native int ISD_OpenAllTrackers(ISenseLib.Hwnd hParent, IntBuffer handle, ISenseLib.Bool infoScreen, ISenseLib.Bool verbose);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_CloseTracker(ISD_TRACKER_HANDLE)</code><br>
	 * <i>native declaration : line 671</i>
	 */
	public static native ISenseLib.Bool ISD_CloseTracker(int handle);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetTrackerConfig(ISD_TRACKER_HANDLE, ISD_TRACKER_INFO_TYPE*, Bool)</code><br>
	 * <i>native declaration : line 678</i>
	 */
	public static native ISenseLib.Bool ISD_GetTrackerConfig(int handle, ISD_TRACKER_INFO_TYPE Tracker, ISenseLib.Bool verbose);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_SetTrackerConfig(ISD_TRACKER_HANDLE, ISD_TRACKER_INFO_TYPE*, Bool)</code><br>
	 * <i>native declaration : line 689</i>
	 */
	public static native ISenseLib.Bool ISD_SetTrackerConfig(int handle, ISD_TRACKER_INFO_TYPE Tracker, ISenseLib.Bool verbose);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetCommInfo(ISD_TRACKER_HANDLE, ISD_TRACKER_INFO_TYPE*)</code><br>
	 * <i>native declaration : line 700</i>
	 */
	public static native ISenseLib.Bool ISD_GetCommInfo(int handle, ISD_TRACKER_INFO_TYPE Tracker);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_SetStationConfig(ISD_TRACKER_HANDLE, ISD_STATION_INFO_TYPE*, WORD, Bool)</code><br>
	 * <i>native declaration : line 711</i>
	 */
	public static native ISenseLib.Bool ISD_SetStationConfig(int handle, ISD_STATION_INFO_TYPE Station, short stationID, ISenseLib.Bool verbose);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetStationConfig(ISD_TRACKER_HANDLE, ISD_STATION_INFO_TYPE*, WORD, Bool)</code><br>
	 * <i>native declaration : line 726</i>
	 */
	public static native ISenseLib.Bool ISD_GetStationConfig(int handle, ISD_STATION_INFO_TYPE Station, short stationID, ISenseLib.Bool verbose);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_ConfigureFromFile(ISD_TRACKER_HANDLE, char*, Bool)</code><br>
	 * <i>native declaration : line 739</i><br>
	 * @deprecated use the safer methods {@link #ISD_ConfigureFromFile(int, java.nio.ByteBuffer, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool)} and {@link #ISD_ConfigureFromFile(int, com.sun.jna.Pointer, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool)} instead
	 */
	@Deprecated 
	public static native ISenseLib.Bool ISD_ConfigureFromFile(int handle, Pointer path, ISenseLib.Bool verbose);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_ConfigureFromFile(ISD_TRACKER_HANDLE, char*, Bool)</code><br>
	 * <i>native declaration : line 739</i>
	 */
	public static native ISenseLib.Bool ISD_ConfigureFromFile(int handle, ByteBuffer path, ISenseLib.Bool verbose);
	/**
	 * Original signature : <code>Bool ISD_ConfigSave(ISD_TRACKER_HANDLE)</code><br>
	 * <i>native declaration : line 752</i>
	 */
	public static native ISenseLib.Bool ISD_ConfigSave(int handle);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetTrackingData(ISD_TRACKER_HANDLE, ISD_TRACKING_DATA_TYPE*)</code><br>
	 * <i>native declaration : line 758</i>
	 */
	public static native ISenseLib.Bool ISD_GetTrackingData(int handle, ISD_TRACKING_DATA_TYPE Data);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetTrackingDataAtTime(ISD_TRACKER_HANDLE, ISD_TRACKING_DATA_TYPE*, double, double)</code><br>
	 * <i>native declaration : line 767</i>
	 */
	public static native ISenseLib.Bool ISD_GetTrackingDataAtTime(int handle, ISD_TRACKING_DATA_TYPE Data, double atTime, double maxSyncWait);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetCameraData(ISD_TRACKER_HANDLE, ISD_CAMERA_DATA_TYPE*)</code><br>
	 * <i>native declaration : line 778</i>
	 */
	public static native ISenseLib.Bool ISD_GetCameraData(int handle, ISD_CAMERA_DATA_TYPE Data);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_RingBufferSetup(ISD_TRACKER_HANDLE, WORD, ISD_STATION_DATA_TYPE*, DWORD)</code><br>
	 * <i>native declaration : line 798</i>
	 */
	public static native ISenseLib.Bool ISD_RingBufferSetup(int handle, short stationID, ISD_STATION_DATA_TYPE dataBuffer, int samples);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_RingBufferStart(ISD_TRACKER_HANDLE, WORD)</code><br>
	 * <i>native declaration : line 810</i>
	 */
	public static native ISenseLib.Bool ISD_RingBufferStart(int handle, short stationID);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_RingBufferStop(ISD_TRACKER_HANDLE, WORD)</code><br>
	 * <i>native declaration : line 818</i>
	 */
	public static native ISenseLib.Bool ISD_RingBufferStop(int handle, short stationID);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_RingBufferQuery(ISD_TRACKER_HANDLE, WORD, ISD_STATION_DATA_TYPE*, DWORD*, DWORD*)</code><br>
	 * <i>native declaration : line 828</i><br>
	 * @deprecated use the safer methods {@link #ISD_RingBufferQuery(int, short, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISD_STATION_DATA_TYPE, java.nio.IntBuffer, java.nio.IntBuffer)} and {@link #ISD_RingBufferQuery(int, short, de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISD_STATION_DATA_TYPE, com.sun.jna.ptr.IntByReference, com.sun.jna.ptr.IntByReference)} instead
	 */
	@Deprecated 
	public static native ISenseLib.Bool ISD_RingBufferQuery(int handle, short stationID, ISD_STATION_DATA_TYPE currentData, IntByReference head, IntByReference tail);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_RingBufferQuery(ISD_TRACKER_HANDLE, WORD, ISD_STATION_DATA_TYPE*, DWORD*, DWORD*)</code><br>
	 * <i>native declaration : line 828</i>
	 */
	public static native ISenseLib.Bool ISD_RingBufferQuery(int handle, short stationID, ISD_STATION_DATA_TYPE currentData, IntBuffer head, IntBuffer tail);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_ResetHeading(ISD_TRACKER_HANDLE, WORD)</code><br>
	 * <i>native declaration : line 838</i>
	 */
	public static native ISenseLib.Bool ISD_ResetHeading(int handle, short stationID);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_BoresightReferenced(ISD_TRACKER_HANDLE, WORD, float, float, float)</code><br>
	 * <i>native declaration : line 851</i>
	 */
	public static native ISenseLib.Bool ISD_BoresightReferenced(int handle, short stationID, float yaw, float pitch, float roll);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_Boresight(ISD_TRACKER_HANDLE, WORD, Bool)</code><br>
	 * <i>native declaration : line 870</i>
	 */
	public static native ISenseLib.Bool ISD_Boresight(int handle, short stationID, ISenseLib.Bool set);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_SendScript(ISD_TRACKER_HANDLE, char*)</code><br>
	 * <i>native declaration : line 885</i><br>
	 * @deprecated use the safer methods {@link #ISD_SendScript(int, java.nio.ByteBuffer)} and {@link #ISD_SendScript(int, com.sun.jna.Pointer)} instead
	 */
	@Deprecated 
	public static native ISenseLib.Bool ISD_SendScript(int handle, Pointer script);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_SendScript(ISD_TRACKER_HANDLE, char*)</code><br>
	 * <i>native declaration : line 885</i>
	 */
	public static native ISenseLib.Bool ISD_SendScript(int handle, ByteBuffer script);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_AuxOutput(ISD_TRACKER_HANDLE, WORD, BYTE*, WORD)</code><br>
	 * <i>native declaration : line 896</i><br>
	 * @deprecated use the safer methods {@link #ISD_AuxOutput(int, short, java.nio.ByteBuffer, short)} and {@link #ISD_AuxOutput(int, short, com.sun.jna.Pointer, short)} instead
	 */
	@Deprecated 
	public static native ISenseLib.Bool ISD_AuxOutput(int handle, short stationID, Pointer AuxOutput, short length);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_AuxOutput(ISD_TRACKER_HANDLE, WORD, BYTE*, WORD)</code><br>
	 * <i>native declaration : line 896</i>
	 */
	public static native ISenseLib.Bool ISD_AuxOutput(int handle, short stationID, ByteBuffer AuxOutput, short length);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_NumOpenTrackers(WORD*)</code><br>
	 * <i>native declaration : line 906</i><br>
	 * @deprecated use the safer methods {@link #ISD_NumOpenTrackers(java.nio.ShortBuffer)} and {@link #ISD_NumOpenTrackers(com.sun.jna.ptr.ShortByReference)} instead
	 */
	@Deprecated 
	public static native ISenseLib.Bool ISD_NumOpenTrackers(ShortByReference num);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_NumOpenTrackers(WORD*)</code><br>
	 * <i>native declaration : line 906</i>
	 */
	public static native ISenseLib.Bool ISD_NumOpenTrackers(ShortBuffer num);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>float ISD_GetTime()</code><br>
	 * <i>native declaration : line 911</i>
	 */
	public static native float ISD_GetTime();
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_UdpDataBroadcast(ISD_TRACKER_HANDLE, DWORD, ISD_TRACKING_DATA_TYPE*, ISD_CAMERA_DATA_TYPE*)</code><br>
	 * <i>native declaration : line 916</i>
	 */
	public static native ISenseLib.Bool ISD_UdpDataBroadcast(int handle, int port, ISD_TRACKING_DATA_TYPE trackingData, ISD_CAMERA_DATA_TYPE cameraData);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetSystemHardwareInfo(ISD_TRACKER_HANDLE, ISD_HARDWARE_INFO_TYPE*)</code><br>
	 * <i>native declaration : line 925</i>
	 */
	public static native boolean ISD_GetSystemHardwareInfo(int handle, ISD_HARDWARE_INFO_TYPE hwInfo);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetStationHardwareInfo(ISD_TRACKER_HANDLE, ISD_STATION_HARDWARE_INFO_TYPE*, WORD)</code><br>
	 * <i>native declaration : line 933</i>
	 */
	public static native ISenseLib.Bool ISD_GetStationHardwareInfo(int handle, ISD_STATION_HARDWARE_INFO_TYPE info, short stationID);
	/**
	 * Original signature : <code>Bool ISD_EnterHeading(ISD_TRACKER_HANDLE, WORD, float)</code><br>
	 * <i>native declaration : line 939</i>
	 */
	public static native ISenseLib.Bool ISD_EnterHeading(int handle, short stationID, float yaw);
	/**
	 * ----------------------------------------------------------------------------<br>
	 * Original signature : <code>Bool ISD_GetPortWirelessInfo(ISD_TRACKER_HANDLE, WORD, ISD_PORT_WIRELESS_INFO_TYPE*)</code><br>
	 * <i>native declaration : line 944</i>
	 */
	public static native ISenseLib.Bool ISD_GetPortWirelessInfo(int handle, short port, ISD_PORT_WIRELESS_INFO_TYPE info);
	public static class Bool extends PointerType {
		public Bool(Pointer address) {
			super(address);
		}
		public Bool() {
			super();
		}
	};
	public static class Hwnd extends PointerType {
		public Hwnd(Pointer address) {
			super(address);
		}
		public Hwnd() {
			super();
		}
	};
}
