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
	
	public static void info(String message, String source){
		fire(InfoType.INFORMATION, message, null, source);
	}

	public static void warning(String message, String source){
		fire(InfoType.WARNING, message, null, source);
	}

	public static void warning(String message, Throwable t, String source){
		fire(InfoType.WARNING, message, t, source);
	}

	
	public static void error(String message, Throwable t, String source){
		fire(InfoType.ERROR, message,t, source);
	}

	private static void fire(InfoType infoType, String message, Throwable t, String source) {
		for (IInfoServiceListener l: infoServiceListener){
			l.newInfo(infoType, message,t, source);
		}
	}
}
