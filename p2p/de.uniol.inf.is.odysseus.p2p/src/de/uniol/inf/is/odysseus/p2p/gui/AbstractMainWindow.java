package de.uniol.inf.is.odysseus.p2p.gui;

import javax.swing.JFrame;


/**
 * Abstraktion der grafischen Darstellung der Peer GUIs
 * 
 * @author Mart Köhler
 *
 */
public abstract class AbstractMainWindow extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public AbstractMainWindow(String title) {
		super(title);
	}

	public abstract void addTab(String queryId);

	public abstract void addAction(String queryId, String action);

	public abstract void addEvent(String queryId, String event);

	public abstract void addSubplans(String queryId, int s);

	public abstract void addStatus(String queryId, String s);

	public abstract void addBids(String queryId, String s);

	public abstract void addSplitting(String queryId, String s);

	public abstract void removeQuery(String queryId);
	
	public abstract void addScheduler(String queryId, String scheduler);
	
	public abstract void addSchedulerStrategy(String queryId, String strategy);
	
	public abstract void addOperation(String queryId, String operation);
	
	public abstract boolean isQuery(String queryId);
	
}
