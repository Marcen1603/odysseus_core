package de.uniol.inf.is.odysseus.core.infoservice;

import java.util.LinkedList;
import java.util.List;

public class InfoService {
	
	static final List<IInfoServiceListener> infoServiceListener = new LinkedList<>();
	
	final String source;
	
	InfoService(String source) {
		this.source = source;
	}
	
	public static void addInfoServiceListener(IInfoServiceListener listener){
		infoServiceListener.add(listener);
	}

	public static void removeInfoSeriveListener(IInfoServiceListener listener){
		infoServiceListener.remove(listener);
	}
	
	public void info(String message){
		fire(InfoType.INFORMATION, message, null, source);
	}

	public void warning(String message){
		fire(InfoType.WARNING, message, null, source);
	}

	public void warning(String message, Throwable t){
		fire(InfoType.WARNING, message, t, source);
	}

	public void error(String message, Throwable t){
		fire(InfoType.ERROR, message,t, source);
	}

	private void fire(InfoType infoType, String message, Throwable t, String source) {
		for (IInfoServiceListener l: infoServiceListener){
			l.newInfo(infoType, message,t, source);
		}
	}
}
