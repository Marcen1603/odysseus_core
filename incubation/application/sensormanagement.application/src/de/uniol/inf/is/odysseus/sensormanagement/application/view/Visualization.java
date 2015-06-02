package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class Visualization extends JPanel implements ReceiverListener
{	
	private static final long serialVersionUID = 1L;
	
	private Object displayConstraints = null;
	
	protected String displayName;
	protected Session session;

	public Object getDisplayConstraints() { return displayConstraints; }
	
	public void setDisplayConstraints(Object displayConstraints) 
	{
		this.displayConstraints = displayConstraints;
	}

	public Visualization(Session session, String displayName)
	{
		this.session = session;
		this.displayName = displayName;
		
		Dimension d = new Dimension(32767, 32767);
		
//		this.setMinimumSize(d);
		this.setPreferredSize(d);
	}
	
	public void remove() 
	{
		session.removeVisualization(this);
	}
}
