package de.uniol.inf.is.odysseus.core.infoservice;

import java.util.LinkedList;
import java.util.List;

public class InfoService {
	
	static final List<IInfoServiceListener> infoServiceListener = new LinkedList<>();
	
	private InfoService() {
	}
	
	public static void addInfoServiceListener(IInfoServiceListener listener){
		infoServiceListener.add(listener);
	}

	public static void removeInfoSeriveListener(IInfoServiceListener listener){
		infoServiceListener.remove(listener);
	}
	
	public static void info(String message){
		fire(InfoType.INFORMATION, message);
	}

	public static void warning(String message){
		fire(InfoType.WARNING, message);
	}
	
	public static void error(String message){
		fire(InfoType.ERROR, message);
	}

	private static void fire(InfoType infoType, String message) {
		for (IInfoServiceListener l: infoServiceListener){
			l.newInfo(infoType, message);
		}
	}
}
