package de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Arc2D;
import java.util.Map;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.PlaybackSensorManager;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackScene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackSession;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.Utilities;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.AbsolutePosition;

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
    		if (event.getTimeStamp() > getSession().getNow()) return;
    		if (event.getTimeStamp() + event.getSecondsValid() < getSession().getNow())
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
	        if (getSession() instanceof PlaybackSession)
	        	timeStart = ((PlaybackSensorManager) getSession().getSensorManager()).getStartTime();
	        graphics.drawString(String.format("Time: %f", event.getTimeStamp()), 10, 15);
	        graphics.drawString(String.format("Time since start: %.3fs", event.getTimeStamp() - timeStart), 10, 30);
        }
       }	
	
	@Override
	public void sensorDataReceived(SensorModel source, Event event) 
	{
   		this.event = event;
        repaint();
	}
	
	@Override public void listeningStarted(AbstractSensor receiver) {}
	@Override public void listeningStopped(AbstractSensor receiver) {}
	
	public static class MapRenderer extends AbstractMapRenderer
	{
		public MapRenderer(Session session)
		{
			super(session);
		}
		
		@Override public void render(Data data) 
		{
        	if (!(data.event.getSource().position instanceof AbsolutePosition)) return;
        	AbsolutePosition pos = (AbsolutePosition) data.event.getSource().position;
			
        	Color cameraColor = Color.GRAY;
        	
			double angle = 90;
			double radius = 80.0;        	
        	
        	Scene scene = getSession().getSensorManager().getScene(); 
        	if (scene instanceof PlaybackScene)
        	{
        		Map<String, String> infoMap = ((PlaybackScene) scene).getDisplayInfoMap().get(data.event.getSource().id);
        		if (infoMap != null)
        		{        		
        			angle = Double.parseDouble(infoMap.get("angle"));
        			radius = Double.parseDouble(infoMap.get("radius"));
        		}
        	}
        	        	
        	radius *= data.pixelPerMeter;
        	
			double rot = pos.orientation + 90.0;			
			double x = data.sensorX - radius;
			double y = data.sensorY - radius;
			int startAngle = (int)(rot - angle/2);
			
			Arc2D arc = new Arc2D.Double(x, y, 2*radius, 2*radius, startAngle, angle, Arc2D.PIE);
			
			final float dash1[] = {1.0f};
		    final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		    data.graphics.setStroke(dashed);				
		    data.graphics.setColor(cameraColor);				
		    data.graphics.draw(arc);
		}
	}	
}
