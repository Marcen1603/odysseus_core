package de.uniol.inf.is.odysseus.cep.cepviewer.event;
import java.util.EventListener;

public interface ICEPViewListener extends EventListener {

	public void cepEventOccurred(CEPViewEvent event);

}
