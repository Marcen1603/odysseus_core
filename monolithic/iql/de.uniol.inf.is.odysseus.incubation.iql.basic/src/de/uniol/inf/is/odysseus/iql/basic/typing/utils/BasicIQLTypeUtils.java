package de.uniol.inf.is.odysseus.iql.basic.typing.utils;


import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

public class BasicIQLTypeUtils extends AbstractIQLTypeUtils {
	public static final String IQL_DIR = "iql";

	
	public static String getIQLOutputPath() {
		return OdysseusConfiguration.instance.getHomeDir()+IQL_DIR;
	}


}
