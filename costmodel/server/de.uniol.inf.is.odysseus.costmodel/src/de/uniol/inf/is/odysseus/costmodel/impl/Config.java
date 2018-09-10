package de.uniol.inf.is.odysseus.costmodel.impl;

import java.io.File;

import com.google.common.base.Strings;

class Config {

	private static final String ODYSSEUS_HOME_ENV = "ODYSSEUS_HOME";
	private static final String ODYSSEUS_DEFAULT_HOME_DIR = determineOdysseusDefaultHome();
	private static final String ODYSSEUS_HOME_DIR = determineOdysseusHome();
	
	public static final String COSTMODEL_BASE_PATH = ODYSSEUS_HOME_DIR + "costmodel" + File.separator;
	public static final String SAMPLING_FILES_PATH = COSTMODEL_BASE_PATH;
	public static final String DATARATE_FILE_NAME = COSTMODEL_BASE_PATH + "datarates.conf";
	public static final String CPUTIME_FILE_NAME = COSTMODEL_BASE_PATH + "cputimes.conf";
	
	static {
		File f = new File(COSTMODEL_BASE_PATH);
		f.mkdirs();
	}

	private static String determineOdysseusHome() {
		try {
			String homeDir = System.getenv(ODYSSEUS_HOME_ENV);
			if (Strings.isNullOrEmpty(homeDir)) {
				return ODYSSEUS_DEFAULT_HOME_DIR;
			}
			return homeDir;
		} catch (Exception e) {
			return ODYSSEUS_DEFAULT_HOME_DIR;
		}
	}
	
	private static String determineOdysseusDefaultHome() {
		return String.format("%s" + File.separator + "%sodysseus" + File.separator, System.getProperty("user.home"), getDot(System.getProperty("os.name")));
	}

	private static String getDot(String os) {
		os = os.toLowerCase();
		if ((os.indexOf("win") >= 0)) {
			return "";
		} else if ((os.indexOf("mac") >= 0)) {
			return ".";
		} else if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			return ".";
		} else {
			return "";
		}
	}
}
