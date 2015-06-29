package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.CallbackListener;

public abstract class Visualization extends JPanel
{	
	private static final long serialVersionUID = 1L;
	
	private Object displayConstraints = null;
	
	protected String displayName;
	protected Session session;
	private CallbackListener<Receiver, Event> sensorDataListener;

	public Object getDisplayConstraints() { return displayConstraints; }
	public CallbackListener<Receiver, Event> getSensorDataListener() { return sensorDataListener; }
	
	public Visualization(Session session, String displayName)
	{
		this.session = session;
		this.displayName = displayName;
		
		Dimension d = new Dimension(32767, 32767);
		
		setPreferredSize(d);
		
		sensorDataListener = new CallbackListener<Receiver, Event>()
		{
			@Override public void listeningStarted(Receiver sender) 
			{
				Visualization.this.listeningStarted(sender);
			}
			
			@Override public void listeningStopped(Receiver sender) 
			{
				Visualization.this.listeningStopped(sender);
			}
			
			@Override public void raise(Receiver sender, Event event) 
			{
				sensorDataReceived(event.getSource(), event);
			}
		};
	}
		
	public void remove() 
	{
		session.removeVisualization(this);
	}
	
	public void setDisplayConstraints(Object displayConstraints) 
	{
		this.displayConstraints = displayConstraints;
		session.updateVisualizationConstraints(this);
	}
	
	abstract public void sensorDataReceived(SensorModel source, Event event);
	abstract public void listeningStopped(Receiver sender);
	abstract public void listeningStarted(Receiver sender);	
}
