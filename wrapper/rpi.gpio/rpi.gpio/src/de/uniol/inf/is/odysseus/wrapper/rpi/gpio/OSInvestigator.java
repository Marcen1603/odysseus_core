package de.uniol.inf.is.odysseus.wrapper.rpi.gpio;

import java.util.Locale;

public class OSInvestigator {
	public enum OS{
		MAC, NUX, WIN, UNKNOWN;
	}
	
	public static OS getCurrentOS() {
		String OS = System.getProperty("os.name", "generic").toLowerCase(
				Locale.ENGLISH);
		if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
			return OSInvestigator.OS.MAC;
		} else if (OS.indexOf("win") >= 0) {
			return OSInvestigator.OS.WIN;
		} else if (OS.indexOf("nux") >= 0) {
			return OSInvestigator.OS.NUX;
		} else {
			return OSInvestigator.OS.UNKNOWN;
		}
	}
}
