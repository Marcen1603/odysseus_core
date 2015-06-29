package de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackSession;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Sample;

public class Lms1xxVisualization extends Visualization implements KeyListener
{
	private static final long serialVersionUID = 1L;
		
	private final Map<String, Event> events = new ConcurrentHashMap<String, Event>();
	private final Map<String, Double[]> settings = new ConcurrentHashMap<String, Double[]>();
	private int zoom = 300;

	public Lms1xxVisualization(Session session, String displayName)
	{
		super(session, displayName);
		
		if (session instanceof PlaybackSession)
			zoom = 30;
		else
			zoom = 30;
		
        setFocusable(true);
        addKeyListener(this);		
	}

	public static void drawMeasurement(Graphics graphics, Measurement measurement, int translationX, int translationY, double rotation, int zoom)
	{
		drawMeasurement(graphics, measurement, translationX, translationY, rotation, zoom, Color.GREEN, Color.RED, Color.RED);
	}
	
	public static void drawMeasurement(Graphics graphics, Measurement measurement, int translationX, int translationY, double rotation, int zoom, Color polygonColor, Color pointColor, Color lineColor)
	{
		if (zoom == 0.0) return;
		
	    Sample[] samples = measurement.get16BitSamples();
	
	    List<Integer> xPoints = new ArrayList<Integer>();
	    List<Integer> yPoints = new ArrayList<Integer>();
	    
	    double rad45 = Math.toRadians(45);
	    
	    for (int i = 0; i < samples.length; i++) 
	    {
	    	final double angle = samples[i].getAngle();
	    	Point2D vector = new Point2D.Double(samples[i].getDist1() * Math.cos(angle), samples[i].getDist1() * Math.sin(angle));
	            
//	    	final Point2D vector = samples[i].getDist1Vector();
	        final double x = vector.getX();
	        final double y = vector.getY();	        
	        vector.setLocation((x * Math.cos(rotation - rad45)) - (y * Math.sin(rotation - rad45)), 
	        				   (x * Math.sin(rotation - rad45)) + (y * Math.cos(rotation - rad45)));
	        vector.setLocation(vector.getX(), vector.getY());
	        xPoints.add((int) vector.getX() / zoom);
	        yPoints.add((int) vector.getY() / zoom);
	    }
	    final int[] tmpXPoints = new int[xPoints.size() + 2];
	    tmpXPoints[0] = translationX;
	    tmpXPoints[xPoints.size() + 1] = translationX;
	    for (int index = 0; index < xPoints.size(); index++) 
	    {
	    	tmpXPoints[index + 1] = translationX + xPoints.get(index);
	    }
	    final int[] tmpYPoints = new int[yPoints.size() + 2];
	    tmpYPoints[0] = translationY;
	    tmpYPoints[yPoints.size() + 1] = translationY;
	    for (int index = 0; index < yPoints.size(); index++) 
	    {
	        tmpYPoints[index + 1] = translationY - yPoints.get(index);
	    }
	    
	    if (polygonColor != null)
	    {
	    	graphics.setColor(polygonColor);
	    	graphics.fillPolygon(tmpXPoints, tmpYPoints, tmpXPoints.length);
	    }

	    if (pointColor != null)
	    {
	    	graphics.setColor(pointColor);
	    	for (int i = 1; i < tmpXPoints.length - 1; i++) 
	    		graphics.drawRect(tmpXPoints[i], tmpYPoints[i], 1, 1);
	    }
        
	    if (lineColor != null)
	    {
	    	graphics.setColor(lineColor);
	    	graphics.drawPolyline(tmpXPoints, tmpYPoints, tmpXPoints.length);
	    }
	}
	
    @Override
    public void paint(final Graphics graphics) 
    {
    	int w = getWidth();
    	int h = getHeight();
    	
    	int mx = w/2;
    	int my = 2*h/3;
    	
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, w, h);
        graphics.setColor(Color.WHITE);
        graphics.drawLine(mx, 0,  mx, h);
        graphics.drawLine(0,  my, w,  my);

        if (events.size() > 0) 
        {
            for (String key : events.keySet()) 
            {
            	double rotation = Math.toRadians(settings.get(key)[2]);
            	
//              final int serial = measurement.getSerial().hashCode();
//              final Color color = new Color(((serial) % 255) & 0xFF, ((serial << 2) % 255) & 0xFF, ((serial << 3) % 255) & 0xFF);
//              graphics.setColor(color);
        	    graphics.setColor(Color.WHITE);            	
            	
            	Event event = events.get(key);
            	if (event.getSecondsValid() != 0.0)
            	{
            		if (event.getTimeStamp() > session.getNow()) continue;
            		if (event.getTimeStamp() + event.getSecondsValid() < session.getNow())
            			continue;
            	}
            	
                Measurement measurement = (Measurement) event.getEventObject();
                drawMeasurement(graphics, measurement, mx, my, rotation, zoom);
                
        	    graphics.setColor(Color.WHITE);
        	    double timeStart = 0.0f;
        	    if (session instanceof PlaybackSession)
        	    	timeStart = ((PlaybackSession) session).getPlayback().getStartTime();
        	    
        	    graphics.drawString(String.format("Time: %f", event.getTimeStamp()), 10, 15);
        	    graphics.drawString(String.format("Time since start: %.3fs", event.getTimeStamp() - timeStart), 10, 30);
        	    
        		/*                final int index = devices.indexOf(key);
      	      final double delay = measurement.getDelay();
      	      graphics.drawString("Key: " + key + " Delay: " + delay + "ms", 5, (index * 35) + 10);
      	      graphics.drawString("Off: " + settings.get(key)[0] + "mm/" + this.settings.get(key)[1] + "mm", 5, (index * 35) + 20);
      	      graphics.drawString("Angle: " + settings.get(key)[2], 5, (index * 35) + 30);
      	
      	      final double distance = Math.floor(Math.sqrt(Math.pow(this.mouseX, 2) + Math.pow(this.mouseY, 2)));
      	      if (distance > 0.0) {
      	          graphics.drawString(this.mouseX + "mm/" + this.mouseY + "mm | " + distance + "mm", 5, (index * 35) + 40);
      	      }*/        	    
            }
        }
    }	
	
	@Override public void sensorDataReceived(SensorModel source, Event event) 
	{
		if (event == null || event.getEventObject() == null)
		{
			events.clear();
		}
		else
		{
			Measurement measurement = (Measurement) event.getEventObject();
	    	
            String id = measurement.getSerial();            
            events.put(id, event);
            
            if (!settings.containsKey(id)) 
            {
//	            devices.add(id);
                settings.put(id, new Double[] { 0.0, 0.0, 0.0 });
            }            
    	}
    	
    	repaint();
	}
	
    @Override
    public void keyPressed(final KeyEvent event) 
    {
        switch (event.getKeyCode()) 
        {
        	case KeyEvent.VK_PAGE_UP:
        		if (this.zoom > 1) 
                	this.zoom--;
        		break;
        		
            case KeyEvent.VK_PAGE_DOWN:
            	this.zoom++;
            	break;
        }
    }

	@Override public void keyReleased(KeyEvent arg0) {}
	@Override public void keyTyped(KeyEvent arg0) {}

	@Override public void listeningStopped(Receiver sender) {}
	@Override public void listeningStarted(Receiver sender) {}
}
