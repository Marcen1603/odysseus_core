package de.uniol.inf.is.odysseus.cep.cepviewer.event;
import javax.swing.event.EventListenerList;

public class CEPViewAgent {
	
	private static CEPViewAgent instance;
	
	private EventListenerList listenerList = new EventListenerList();
	
	private CEPViewAgent() {
		
	}
	
	public static CEPViewAgent getInstance() {
		if(CEPViewAgent.instance == null) {
			CEPViewAgent.instance = new CEPViewAgent();
		}
		return CEPViewAgent.instance;
	}

	public void addCEPEventListener(ICEPViewListener listener) {
		this.listenerList.add(ICEPViewListener.class, listener);
	}
	
	public void removeCEPEventListener(ICEPViewListener listener) {
		this.listenerList.remove(ICEPViewListener.class, listener);
	}
	
	public void fireCEPEvent(int type, Object content) {
		 Object[] listeners = listenerList.getListenerList();
		 ICEPViewListener listener = null;
		 for (int i=0; i<listeners.length; i+=2) {
			 if (listeners[i]==ICEPViewListener.class){
				 listener = (ICEPViewListener)listeners[i+1];
				 CEPViewEvent event = new CEPViewEvent(listener, type, content);
				 listener.cepEventOccurred(event);
			 };
		 };
	}

}
