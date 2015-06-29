package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import java.io.IOException;

import javax.swing.Icon;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.PlaybackSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.CallbackListener;

public class ViewSensor extends ViewEntity
{
	private PlaybackSensor playbackSensor;
	private CallbackListener<Receiver, Event> listener;
	private Visualization visualization;
	private String constraintString = null;	
	
	public Visualization getVisualization() { return visualization; }
	public String getConstraintString() { return constraintString; }
	public Receiver getPlaybackSensor() { return playbackSensor; }
	
	@Override public String getDisplayName() 
	{ 
		return playbackSensor != null && playbackSensor.getSensorModel() != null ? playbackSensor.getSensorModel().displayName : ""; 	
	}

	@Override public Icon getIcon() 
	{
		return playbackSensor != null && playbackSensor.isPlaying() ? TreeCellRenderer.sensorIconPlay : TreeCellRenderer.sensorIconPause;
	}
	
	public ViewSensor(Session session, PlaybackSensor playbackSensor) throws IOException 
	{
		super(session, session.getTreeRoot());
		
		this.playbackSensor = playbackSensor;
		
		listener = new CallbackListener<Receiver, Event>()
		{
			@Override public void raise(Receiver source, Event event) 
			{
				onSensorDataReceived(source, event);
			}

			@Override public void listeningStarted(Receiver receiver) {}
			@Override public void listeningStopped(Receiver receiver) {}			
		};
		
		playbackSensor.sensorDataReceived.addListener(listener);
	}
	
	@Override public void startLogging() { throw new ViewException("Cannot log a playback sensor"); }
	@Override public void stopLogging()  { throw new ViewException("Cannot log a playback sensor"); }

	protected void onSensorDataReceived(Receiver source, Event event) 
	{
		if (visualization != null) visualization.sensorDataReceived(event.getSource(), event);
		
		if (getSession().getMap() != null)
			getSession().getMap().sensorDataReceived(event.getSource(), event);
	}	
	
	@Override public void startVisualization() 
	{
		if (visualization != null) return;
		if ("disable".equals(constraintString)) return;

		visualization = playbackSensor.getSensorEntry().createVisualization(getSession(), playbackSensor.getSensorModel().displayName);
		visualization.setDisplayConstraints(constraintString);
		getSession().addVisualization(visualization);
		getSession().getTreeModel().nodeStructureChanged(getNode());
	}
	
	@Override public void stopVisualization()  
	{ 
		if (visualization == null) return;

		visualization.remove();
		visualization = null;
		getSession().getTreeModel().nodeStructureChanged(getNode());
	}
	
	public void setConstraintString(String constraintString)
	{
		this.constraintString = constraintString;
		if (visualization != null)
			visualization.setDisplayConstraints(constraintString);
	}
	
	@Override public void treeDblClick()
	{
	}
}
