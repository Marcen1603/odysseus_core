package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;

public class MapVisualization extends Visualization
{
	private static final long serialVersionUID = 3745022811236756800L;

	private Map<SensorModel, Event> currentEvents = new ConcurrentHashMap<>();
	private final BufferedImage mapImage;
	
	public MapVisualization(Session session, String displayName, String mapImageFileName) throws IOException 
	{
		super(session, displayName);
		
    	mapImage = ImageIO.read(new File(mapImageFileName));
	}

    @Override
    public void paint(final Graphics g) 
    {
    	Graphics2D graphics = (Graphics2D)g;
    	graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    	
    	
    	Color scanColor = Color.BLACK;
    	Color cameraColor = Color.GRAY;
    	
       	int w = getWidth();
    	int h = getHeight();
    	
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, w, h);    	

		int mapW = mapImage.getWidth();
		int mapH = mapImage.getHeight();
		double ratio = (double)mapW / mapH;

		mapW = getWidth();
		mapH = (int)(w / ratio);

		if (mapH > getHeight())
		{
			mapH = getHeight();
			mapW = (int)(ratio * mapH);
		}
		
		int mapX = (getWidth()  - mapW) / 2;
		int mapY = (getHeight() - mapH) / 2;
		double scale = (double)mapW / mapImage.getWidth();
                
		graphics.drawImage(mapImage, mapX, mapY, mapW, mapH, this);

    	// Quit this function w/o having to comment everything to avoid warnings...
    	// TODO: Do correct implementation of Map
//    	if (Math.abs(1+2)==3) return;        		
		
    	for (Entry<SensorModel, Event> entry : currentEvents.entrySet())
    	{
    		SensorModel sensor = entry.getKey();
    		Event event  = entry.getValue();    		
    	
    		if (event.getTimeStamp() > session.getNow()) continue;
        	if (event.getTimeStamp() + event.getSecondsValid() < session.getNow()) 
        		continue;    		
    		
    		String[] position = "0 0".split(" ");//sensor.getPosition().split(" ");
    		
    		double x = 0;
    		double y = 0;
    		double rot = 0.0;
    		int angle = 0; 
    		double radius = 0.0;
    		double zoom = 0.0;
    		
    		if (position.length >= 1)
    			x = mapX + Integer.parseInt(position[0]) * scale;

    		if (position.length >= 2)
    			y = mapY + Integer.parseInt(position[1]) * scale;
    		
    		
    		String ethAddr = ""; // sensor.getEthernetAddr()
    		
    		switch (sensor.id)
    		{
    		// Aufnahmen aus BHV
				case "LMS1xx_2039536063":	// LMS 100
				{
					// LMS 100
					x = mapX + 190*scale;
					y = mapY + 482*scale;
					rot = Math.toRadians(17.0);
					zoom = 550.0 / scale;
					break;
				}
					
				case "LMS1xx_1232438181":	// LMS 151
	    		{
	    			x = mapX + 172*scale;
	    			y = mapY + 484*scale;
	    			rot = Math.toRadians(40.0);
	    			zoom = 700.0 / scale;
	    			break;
	    		}
    		
    		
	    		case "192.168.1.30":	// Handy
	    		{
	    			x = mapX + 188*scale;
	    			y = mapY + 487*scale;
	    			rot = Math.toRadians(10.0);
	    			angle = 60;
	    			radius = 40.0 * scale;
	    			break;    				
	    		}
	
	    		case "192.168.1.31":	// IR
	    		{
	    			x = mapX + 780*scale;
	    			y = mapY + 290*scale;
	    			rot = Math.toRadians(150.0);
	    			angle = 70;
	    			radius = 40.0 * scale;
	    			break;
	    		}
	
	    		case "192.168.1.79":	// Basler AWI
	    		{
	    			x = mapX + 780*scale;
	    			y = mapY + 290*scale;
	    			rot = Math.toRadians(140.0);
	    			angle = 90;
	    			radius = 80.0 * scale;
	    			break;
	    		}    			    			
	
	    		case "192.168.1.78":	// Basler Mole
	    		{
	    			x = mapX + 178*scale;
	    			y = mapY + 490*scale;
	    			rot = Math.toRadians(65.0);
	    			radius = 40.0 * scale;
	    			angle = 58;
	    			break;
	    		}    		
    		
	    		// Vmtl Aufnahmen vom Offis-Tag Raum
/*    			case "192.168.1.11":	// LMS 100
    			{
    				// LMS 100
    				x = mapX + 270*scale;
    				y = mapY + 55*scale;
    				rot = Math.toRadians(180.0);
    				zoom = 15.0 / scale;
    				break;
    			}
    				
    			case "192.168.1.10":	// LMS 151
	    		{
	    			x = mapX + 4*scale;
	    			y = mapY + 222*scale;
	    			rot = Math.toRadians(270.0);
	    			zoom = 15.0 / scale;
	    			break;
	    		}

    			case "192.168.1.12":	// LMS 100 2
	    		{
	    			x = mapX + 401*scale;
	    			y = mapY + 197*scale;
	    			rot = Math.toRadians(90.0);
	    			zoom = 15.0 / scale;
	    			break;
	    		}*/	    		
    		}
    		
    		if (position.length >= 3)
    			rot = Double.parseDouble(position[2]);    		
    		
			switch (sensor.type)
			{
			case "LMS1xx":
//				if (sensor.getEthernetAddr().equals("192.168.1.11"))
				{
					final BasicStroke solid = new BasicStroke();
					graphics.setStroke(solid);
					Lms1xxVisualization.drawMeasurement(graphics, (Measurement) event.getEventObject(), (int)x, (int)y, rot, (int)(zoom), null, null, scanColor);
				}
				
				break;
				
			case "IPVideo":
				x -= radius;
				y -= radius;
				int startAngle = (int)Math.toDegrees(rot);
				
				Arc2D arc = new Arc2D.Double(x, y, 2*radius, 2*radius, startAngle, angle, Arc2D.PIE);
				
				final float dash1[] = {1.0f};
			    final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
			    graphics.setStroke(dashed);				
				graphics.setColor(cameraColor);				
				graphics.draw(arc);
				break;
			}
    	}
    }
	
	@Override public void sensorDataReceived(SensorModel source, Event event) 
	{
		if (event != null)
			currentEvents.put(source, event);
		else
			currentEvents.remove(source);
		
		repaint();
	}
	
	@Override public void listeningStarted(Receiver receiver) {}
	@Override public void listeningStopped(Receiver receiver) 
	{
		currentEvents.remove(receiver.getSensorModel());
		repaint();
	}	
}
