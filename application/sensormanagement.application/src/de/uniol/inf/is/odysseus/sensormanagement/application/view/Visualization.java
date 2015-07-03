package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.CallbackListener;

public abstract class Visualization extends JPanel
{	
	private static final long serialVersionUID = 1L;
	
	private Object displayConstraints = null;	
	private String displayName;	
	private Session session;
	private CallbackListener<AbstractSensor, Event> sensorDataListener;

	public Object getDisplayConstraints() { return displayConstraints; }
	public CallbackListener<AbstractSensor, Event> getSensorDataListener() { return sensorDataListener; }
	public Session getSession() { return session; }
	public String getDisplayName() { return displayName; }
	
	public Visualization(Session session, String displayName)
	{
		this.session = session;
		this.displayName = displayName;
		
		Dimension d = new Dimension(32767, 32767);
		
		setPreferredSize(d);
		
		sensorDataListener = new CallbackListener<AbstractSensor, Event>()
		{
			@Override public void listeningStarted(AbstractSensor sender) 
			{
				Visualization.this.listeningStarted(sender);
			}
			
			@Override public void listeningStopped(AbstractSensor sender) 
			{
				Visualization.this.listeningStopped(sender);
			}
			
			@Override public void raise(AbstractSensor sender, Event event) 
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
	
	abstract protected void sensorDataReceived(SensorModel source, Event event);
	abstract protected void listeningStopped(AbstractSensor sender);
	abstract protected void listeningStarted(AbstractSensor sender);	
}
