package de.uniol.inf.is.odysseus.broker.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.broker.console.views.OutputContentProvider;

public class ViewController {

	private static ViewController instance;
	private int viewCounter = 0;

	private Map<Integer, OutputContentProvider> contentProvider = new HashMap<Integer, OutputContentProvider>();	
	private List<IContentListener> listeners = new ArrayList<IContentListener>();	

	private ViewController() {		
		
	}

	public static synchronized ViewController getInstance() {
		if (instance == null) {
			instance = new ViewController();
		}
		return instance;
	}

	
	public void addOutputListener(IContentListener listener){
		if(!this.listeners.contains(listener)){
			this.listeners.add(listener);
		}
	}
	
	public void removeContentListener(IContentListener listener){		
		if(this.listeners.contains(listener)){			
			this.listeners.remove(listener);			
		}
	}
	
	public void newQueryEvent(int port){
		for(IContentListener listener : this.listeners){
			listener.newQueryEvent(port);
		}
	}
	
	private void newTupleEvent(int port) {
		for(IContentListener listener : this.listeners){
			listener.inputChanged(port);
		}
		
	}	
	
	
	public OutputContentProvider getContentProvider(int port) {
		return contentProvider.get(Integer.valueOf(port));
	}

	

	public synchronized int createNewView(String[] attributes) {
		int newport = this.viewCounter;
		this.contentProvider.put(Integer.valueOf(newport),
				new OutputContentProvider(attributes));		
		this.newQueryEvent(newport);	
		this.viewCounter++;
		return newport;
	}	

	public void clearAll() {
		this.contentProvider.clear();
	}

	public void sendTuple(int port, String[] values) {
		Integer key = Integer.valueOf(port);
		if(this.contentProvider.containsKey(key)){
			this.contentProvider.get(key).addValue(values);
		}
		this.newTupleEvent(port);
		
	}
	
	public Set<Integer> getPorts(){
		return this.contentProvider.keySet();
	}

	
}
