package de.uniol.inf.is.odysseus.core.infoservice;

public interface IInfoServiceListener {
	void newInfo(InfoType infoType, String message, Throwable throwable, String source);
}
