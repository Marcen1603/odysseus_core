package de.uniol.inf.is.odysseus.p2p.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;


/**
 * Abstraktion der grafischen Darstellung der Peer GUIs
 * 
 * @author Mart Köhler
 *
 */
public abstract class AbstractMainWindow extends JFrame implements ActionListener{
	

	
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
	
	
	//Thin-Peer spezifisch
	public abstract void addResult(String queryId, Object o);	
	public abstract void addTab(String queryId, String queryAsString);
	public abstract void actionPerformed(ActionEvent e);
	public abstract void addAdminPeer(String queryId, String adminPeer);

}
