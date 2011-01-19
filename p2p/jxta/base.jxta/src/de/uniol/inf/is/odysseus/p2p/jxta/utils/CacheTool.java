package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.File;
import java.io.IOException;

public class CacheTool {
	
	public static void checkForExistingConfigurationDeletion(String Name,
			File ConfigurationFile) throws IOException {
		recursiveDelete(ConfigurationFile);
	}

	public static void recursiveDelete(File TheFile) {
		File[] SubFiles = TheFile.listFiles();
		if (SubFiles != null) {
			for (int i = 0; i < SubFiles.length; i++) {
				if (SubFiles[i].isDirectory()) {
					recursiveDelete(SubFiles[i]);
				}
				SubFiles[i].delete();
			}
			TheFile.delete();
		}
	}

}
