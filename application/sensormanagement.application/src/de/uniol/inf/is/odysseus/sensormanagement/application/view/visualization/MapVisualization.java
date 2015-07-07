package de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.DisplayMap;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.AbsolutePosition;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.RelativePosition;

public class MapVisualization extends Visualization
{
	private static final long serialVersionUID = 3745022811236756800L;

	private Map<AbstractSensor, Event> currentEvents = new IdentityHashMap<>();
	private final BufferedImage mapImage;
	private DisplayMap map;
	
	public MapVisualization(Session session, DisplayMap map) throws IOException 
	{
		super(session, "Map: " + map.name);

		this.map = map;
    	mapImage = ImageIO.read(new File(session.getSensorManager().getScene().getPath() + map.imageFile));
	}

	double distanceInMeter(double lat1, double lon1, double lat2, double lon2)
	{
		double l1,l2,g1,g2,dg;
		l1 = Math.toRadians(lat1);
		l2 = Math.toRadians(lat2);
		g1 = Math.toRadians(lon1);
		g2 = Math.toRadians(lon2);
		dg = g2-g1;
		return 1852.0f * 60.0f * Math.toDegrees(Math.acos(Math.sin(l1)*Math.sin(l2)+Math.cos(l1)*Math.cos(l2)*Math.cos(dg)));
	}	
	
    @Override
    public void paint(final Graphics g) 
    {
    	Graphics2D graphics = (Graphics2D)g;
    	graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    	
    	
       	int w = getWidth();
    	int h = getHeight();
    	
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, w, h);    	

		int mapWidthInPixel = mapImage.getWidth();
		int mapHeightInPixel = mapImage.getHeight();
		double ratio = (double)mapWidthInPixel / mapHeightInPixel;

		mapWidthInPixel = getWidth();
		mapHeightInPixel = (int)(w / ratio);

		if (mapHeightInPixel > getHeight())
		{
			mapHeightInPixel = getHeight();
			mapWidthInPixel = (int)(ratio * mapHeightInPixel);
		}
		
		int mapX = (getWidth()  - mapWidthInPixel) / 2;
		int mapY = (getHeight() - mapHeightInPixel) / 2;
                
		graphics.drawImage(mapImage, mapX, mapY, mapWidthInPixel, mapHeightInPixel, this);

		double pixelPerMeter;
		double mapWidth  = map.rightPosition - map.leftPosition;
		double mapHeight = map.topPosition   - map.bottomPosition;		
		
		if (map.instanceName == null)
		{
			// Absolute map. Map coordinates given in Long/Lat
			double distWE = distanceInMeter(map.topPosition, map.rightPosition, map.topPosition, map.leftPosition);
			double distNS = distanceInMeter(map.topPosition, map.rightPosition, map.bottomPosition, map.rightPosition);
    	
			pixelPerMeter = (mapWidthInPixel / distWE + mapHeightInPixel / distNS) / 2;
		}
		else
		{
			// Relative map. Map coordinates given as meters
			double ppmW = mapWidthInPixel / mapWidth;
			double ppmH = mapHeightInPixel / mapHeight;
			pixelPerMeter = (ppmW + ppmH) / 2;
		}
    	
    	synchronized (this)
    	{
	    	for (Entry<AbstractSensor, Event> entry : currentEvents.entrySet())
	    	{
	    		AbstractSensor sensor = entry.getKey();
	    		SensorModel model = sensor.getSensorModel();
	    		Event event  = entry.getValue();    		
	    	
	    		AbstractMapRenderer mapRenderer = getSession().getViewSensor(model.id).getMapRenderer();
	    		
	    		if (mapRenderer == null) continue;
	    		if (event.getTimeStamp() > getSession().getNow()) continue;
	        	if (event.getTimeStamp() + event.getSecondsValid() < getSession().getNow()) continue;    		        	
	        	        	        	
	        	AbstractMapRenderer.Data data = new AbstractMapRenderer.Data();
	        	data.graphics = graphics;
	        	data.event = event;
	        	data.mapX = mapX;
	        	data.mapY = mapY;
	        	data.mapW = mapWidthInPixel;
	        	data.mapH = mapHeightInPixel;
	        	
	        	if (model.position instanceof AbsolutePosition)
	        	{
	        		AbsolutePosition pos = (AbsolutePosition) model.position;
	        		data.sensorX = mapX + (pos.longitude - map.leftPosition)   / mapWidth  * mapWidthInPixel;
	        		data.sensorY = mapY - (pos.latitude  - map.bottomPosition) / mapHeight * mapHeightInPixel + mapHeightInPixel;
	        		data.sensorRot = pos.orientation;
	        	}
	        	else
	        	if (model.position instanceof RelativePosition)
	        	{
	        		RelativePosition pos = (RelativePosition) model.position;
	        		data.sensorX = mapX + (pos.x - map.leftPosition)   / mapWidth  * mapWidthInPixel;
	        		data.sensorY = mapY - (pos.y - map.bottomPosition) / mapHeight * mapHeightInPixel + mapHeightInPixel;
	        		data.sensorRot = pos.orientation;
	        	}
	        	
	        	data.pixelPerMeter = pixelPerMeter;
	       		mapRenderer.render(data);
	    	}
	    }
    }
	
	@Override public void sensorDataReceived(AbstractSensor source, Event event) 
	{
		synchronized (this)
		{
			if (event != null)
				currentEvents.put(source, event);
			else
				currentEvents.remove(source);
		}
		
		repaint();
	}
	
	@Override public void listeningStarted(AbstractSensor receiver) {}
	@Override public void listeningStopped(AbstractSensor receiver) 
	{
		synchronized (this)
		{
			currentEvents.remove(receiver.getSensorModel());
		}
		repaint();
	}	
}
