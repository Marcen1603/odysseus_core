package de.uniol.inf.is.odysseus.broker.console;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.console.views.OutputContentProvider;
import de.uniol.inf.is.odysseus.broker.console.views.OutputView;

public class ViewController {

	private static ViewController instance;

	private List<OutputContentProvider> contentProvider = new ArrayList<OutputContentProvider>();
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
		return contentProvider.get(port);
	}

	public void refreshAll() {

		for (OutputView view : this.views) {
			view.refreshViewer();
		}
	}

	public int createNewView(String[] attributes) {
		int newport = this.contentProvider.size();
		this.contentProvider.add(new OutputContentProvider());
		for(OutputView view : this.views){
			view.addTab(newport, attributes);
		}
		return newport;
	}

	public void clearAll() {				
		this.contentProvider.clear();		
	}

}
