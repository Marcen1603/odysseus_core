package de.uniol.inf.is.odysseus.broker.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.broker.console.views.OutputContentProvider;
import de.uniol.inf.is.odysseus.broker.console.views.OutputView;

public class ViewController {

	private static ViewController instance;
	private int viewCounter = 0;
	
	
	private Map<Integer, OutputContentProvider> contentProvider = new HashMap<Integer, OutputContentProvider>();
	private List<OutputView> views = new ArrayList<OutputView>();

	private ViewController() {

	}

	public static synchronized ViewController getInstance() {
		if (instance == null) {
			instance = new ViewController();
		}
		return instance;
	}

	public List<OutputView> getViews() {
		return this.views;
	}

	public void addView(OutputView view) {
		this.views.add(view);
	}

	public OutputContentProvider getContentProvider(int port) {
		return contentProvider.get(Integer.valueOf(port));
	}

	public void refreshAll() {
		for (OutputView view : this.views) {
			view.refreshViewer();
		}
	}

	public synchronized int createNewView(String[] attributes) {
		int newport = this.viewCounter;
		this.contentProvider.put(Integer.valueOf(newport),new OutputContentProvider());
		for(OutputView view : this.views){
			view.addTab(newport, attributes);			
		}
		this.viewCounter++;
		return newport;
	}

	public void clearAll() {				
		this.contentProvider.clear();		
	}
		
	public void removeView(OutputView outputView) {
		this.views.remove(outputView);		
	}
	
	public boolean isQueryViewTabOpen(int port){
		return (this.contentProvider.containsKey(Integer.valueOf(port)));
	}

}
