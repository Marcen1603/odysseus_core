package de.uniol.inf.is.odysseus.wrapper.sick;

public final class SICKConstants {
	public static final String START_SCAN = "sEN LMDscandata 1";
	public static final String STOP_SCAN = "sEN LMDscandata 0";
	public static final byte START = (byte) 0x02;
	public static final byte END = (byte) 0x03;
	public static final String SRA = "sRA";
	public static final String SEA = "sEA";
	public static final String SSN = "sSN";
	public static final String LCM_STATE = "LCMstate";

	public static final String LMD_SCANDATA = "LMDscandata";
	public static final String DIST1 = "DIST1";
	public static final String DIST2 = "DIST2";
	public static final String RSSI1 = "RSSI1";
	public static final String RSSI2 = "RSSI2";

	private SICKConstants() {
	}
}
