package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import java.awt.Color;
import java.awt.Graphics;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackSession;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.Utilities;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;

public class VideoVisualization extends Visualization
{
	private static final long serialVersionUID = 1L;
	private Event event;
		
	public VideoVisualization(Session session, String displayName)
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
		
    	IplImage image = (IplImage) event.getEventObject();
		Utilities.drawImageRatio(graphics, getWidth(), getHeight(), image, this);
    		
        boolean displayTimestamp = false;
        
        if (displayTimestamp)
        {
            graphics.setColor(Color.BLACK);
            graphics.fillRect(8, 2, 155, 32);        	
        	
	        graphics.setColor(Color.WHITE);
	        double timeStart = 0.0f;
	        if (session instanceof PlaybackSession)
	        	timeStart = ((PlaybackSession) session).getStartTime();
	        graphics.drawString(String.format("Time: %f", event.getTimeStamp()), 10, 15);
	        graphics.drawString(String.format("Time since start: %.3fs", event.getTimeStamp() - timeStart), 10, 30);
        }
       }	
	
	@Override
	public void sensorDataReceived(SensorModel2 source, Event event) 
	{
   		this.event = event;
        repaint();
	}
	
	@Override public void listeningStarted(Receiver receiver) {}
	@Override public void listeningStopped(Receiver receiver) {}
}
