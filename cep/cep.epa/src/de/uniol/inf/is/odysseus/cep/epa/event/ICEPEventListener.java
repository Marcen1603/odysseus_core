package de.uniol.inf.is.odysseus.cep.epa.event;
import java.util.EventListener;

public interface ICEPEventListener extends EventListener {

	public void cepEventOccurred(CEPEvent event);

}
