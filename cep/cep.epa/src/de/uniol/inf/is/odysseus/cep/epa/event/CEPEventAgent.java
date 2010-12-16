package de.uniol.inf.is.odysseus.cep.epa.event;
import javax.swing.event.EventListenerList;

public class CEPEventAgent {
	
	private static CEPEventAgent instance;
	
	private EventListenerList listenerList = new EventListenerList();
	
	private CEPEventAgent() {
		
	}
	
	public static CEPEventAgent getInstance() {
		if(CEPEventAgent.instance == null) {
			CEPEventAgent.instance = new CEPEventAgent();
		}
		return CEPEventAgent.instance;
	}

	public void addCEPEventListener(ICEPEventListener listener) {
		this.listenerList.add(ICEPEventListener.class, listener);
	}
	
	public void removeCEPEventListener(ICEPEventListener listener) {
		this.listenerList.remove(ICEPEventListener.class, listener);
	}
	
	public void fireCEPEvent(int type, Object content) {
		 Object[] listeners = listenerList.getListenerList();
		 ICEPEventListener listener = null;
		 for (int i=0; i<listeners.length; i+=2) {
			 if (listeners[i]==ICEPEventListener.class){
				 listener = (ICEPEventListener)listeners[i+1];
				 CEPEvent event = new CEPEvent(listener, type, content);
				 listener.cepEventOccurred(event);
			 };
		 };
	}

}
