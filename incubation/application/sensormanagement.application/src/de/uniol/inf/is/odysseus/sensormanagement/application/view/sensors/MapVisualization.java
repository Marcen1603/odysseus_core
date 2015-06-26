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
import de.uniol.inf.is.odysseus.sensormanagement.application.view.scene.DisplayMap;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.AbsolutePosition;
import de.uniol.inf.is.odysseus.wrapper.lms1xx.model.Measurement;

public class MapVisualization extends Visualization
{
	private static final long serialVersionUID = 3745022811236756800L;

	private Map<SensorModel, Event> currentEvents = new ConcurrentHashMap<>();
	private final BufferedImage mapImage;
	private DisplayMap map;
	
	public MapVisualization(Session session, DisplayMap map) throws IOException 
	{
		super(session, "Map: " + map.name);

		this.map = map;
    	mapImage = ImageIO.read(new File(session.getScene().getPath() + map.imageFile));
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

    	for (Entry<SensorModel, Event> entry : currentEvents.entrySet())
    	{
    		SensorModel sensor = entry.getKey();
    		Event event  = entry.getValue();    		
    	
    		if (event.getTimeStamp() > session.getNow()) continue;
        	if (event.getTimeStamp() + event.getSecondsValid() < session.getNow()) 
        		continue;    		
    		
        	if (!(sensor.position instanceof AbsolutePosition)) continue;
        	AbsolutePosition pos = (AbsolutePosition) sensor.position;
        	
    		double x = mapX + 		 (pos.longitude - map.leftPosition)   / (map.rightPosition - map.leftPosition)   * mapW;
    		double y = mapY + mapH - (pos.latitude  - map.bottomPosition) / (map.topPosition   - map.bottomPosition) * mapH;    		
    		double rot = pos.orientation + 90.0;	// pos.orientation has 0° north, while rot is mathematics orientation with 0° east
    		
    		int angle = 0; 
    		double radius = 0.0;
    		double zoom = 0.0;
    		
    		switch (sensor.id)
    		{
    		// Aufnahmen aus BHV
				case "LMS1xx_2039536063":	// LMS 100
				{
					// LMS 100
/*					x = mapX + 190*scale;
					y = mapY + 482*scale;
					rot = Math.toRadians(17.0);*/
					zoom = 550.0 / scale;
					break;
				}
					
				case "LMS1xx_1232438181":	// LMS 151
	    		{
/*	    			x = mapX + 172*scale;
	    			y = mapY + 484*scale;
	    			rot = Math.toRadians(40.0);*/
	    			zoom = 700.0 / scale;
	    			break;
	    		}
    		
    		
	    		case "IntegratedCamera_808930381":	// Handy
	    		{
/*	    			x = mapX + 188*scale;
	    			y = mapY + 487*scale;
	    			rot = Math.toRadians(10.0);*/
	    			angle = 60;
	    			radius = 40.0 * scale;
	    			break;    				
	    		}
	
	    		case "OptrisCamera_1405033410":	// IR
	    		{
/*	    			x = mapX + 780*scale;
	    			y = mapY + 290*scale;
	    			rot = Math.toRadians(150.0);*/
	    			angle = 70;
	    			radius = 40.0 * scale;
	    			break;
	    		}
	
	    		case "BaslerCamera_790104739":	// Basler AWI
	    		{
/*	    			x = mapX + 780*scale;
	    			y = mapY + 290*scale;
	    			rot = Math.toRadians(140.0);*/
	    			angle = 90;
	    			radius = 80.0 * scale;
	    			break;
	    		}    			    			
	
	    		case "BaslerCamera_80823122":	// Basler Mole
	    		{
/*	    			x = mapX + 178*scale;
	    			y = mapY + 490*scale;
	    			rot = Math.toRadians(65.0);*/
	    			radius = 40.0 * scale;
	    			angle = 58;
	    			break;
	    		}    		
    		}
    		    		
			switch (sensor.type)
			{
				case "LMS1xx":
				{
					final BasicStroke solid = new BasicStroke();
					graphics.setStroke(solid);
					Lms1xxVisualization.drawMeasurement(graphics, (Measurement) event.getEventObject(), (int)x, (int)y, Math.toRadians(rot), (int)(zoom), null, null, scanColor);
					break;
				}				
				
				case "IntegratedCamera":
				case "OptrisCamera":
				case "BaslerCamera":
				{
					x -= radius;
					y -= radius;
					int startAngle = (int)rot - angle/2;
					
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
