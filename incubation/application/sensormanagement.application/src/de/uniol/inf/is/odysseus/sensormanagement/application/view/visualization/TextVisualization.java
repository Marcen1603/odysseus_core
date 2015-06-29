package de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization;

import java.awt.Color;
import java.awt.Graphics;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackSession;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public class TextVisualization extends Visualization
{
	private static final long serialVersionUID = 1L;
	private Event event;
		
	public TextVisualization(Session session, String displayName)
	{
		super(session, displayName);
	}
	
    @Override
    public void paint(final Graphics graphics) 
    {
        graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
    	
		if (event == null || event.getEventObject() == null) return;
    	if (event.getSecondsValid() != 0.0)
    	{
    		if (event.getTimeStamp() > session.getNow()) return;
    		if (event.getTimeStamp() + event.getSecondsValid() < session.getNow())
    			return;
    	}
		
    	String data = (String)event.getEventObject();
    	
        graphics.setColor(Color.BLACK);
        graphics.fillRect(8, 2, 155, 32);        	
        	
	    graphics.setColor(Color.WHITE);
	    double timeStart = 0.0f;
	    if (session instanceof PlaybackSession)
	    	timeStart = ((PlaybackSession) session).getPlayback().getStartTime();
	    graphics.drawString(String.format("Time: %f", event.getTimeStamp()), 10, 15);
	    graphics.drawString(String.format("Time since start: %.3fs", event.getTimeStamp() - timeStart), 10, 30);
	    
	    String[] lines = data.split(",");
	    
	    int y=45;
	    for (String l : lines)
	    {
	    	graphics.drawString(l, 10, y);
	    	y+=15;
	    }
    }	
	
	@Override
	public void sensorDataReceived(SensorModel source, Event event) 
	{
   		this.event = event;
        repaint();
	}
	
	@Override public void listeningStarted(Receiver receiver) {}
	@Override public void listeningStopped(Receiver receiver) {}
}
