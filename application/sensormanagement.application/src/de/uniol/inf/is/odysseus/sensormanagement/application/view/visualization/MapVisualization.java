package de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.DisplayMap;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.position.AbsolutePosition;

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
                
		graphics.drawImage(mapImage, mapX, mapY, mapW, mapH, this);

    	double mapLongitudeDelta = map.rightPosition - map.leftPosition;
    	double mapLatitudeDelta  = map.topPosition   - map.bottomPosition;		
		
    	double distWE = distanceInMeter(map.topPosition, map.rightPosition, map.topPosition, map.leftPosition);
    	double distNS = distanceInMeter(map.topPosition, map.rightPosition, map.bottomPosition, map.rightPosition);
    	
    	double pixelPerMeter = (mapW / distWE + mapH / distNS) / 2;
    	
    	for (Entry<SensorModel, Event> entry : currentEvents.entrySet())
    	{
    		SensorModel sensor = entry.getKey();
    		Event event  = entry.getValue();    		
    	
    		AbstractMapRenderer mapRenderer = getSession().getViewSensor(sensor.id).getMapRenderer();
    		
    		if (mapRenderer == null) return;
    		if (event.getTimeStamp() > getSession().getNow()) continue;
        	if (event.getTimeStamp() + event.getSecondsValid() < getSession().getNow()) continue;    		        	
        
        	if (!(sensor.position instanceof AbsolutePosition)) continue;
        	AbsolutePosition pos = (AbsolutePosition) sensor.position;
        	        	
        	AbstractMapRenderer.Data data = new AbstractMapRenderer.Data();
        	data.graphics = graphics;
        	data.event = event;
        	data.mapX = mapX;
        	data.mapY = mapY;
        	data.mapW = mapW;
        	data.mapH = mapH;
        	data.sensorX = mapX + 		 (pos.longitude - map.leftPosition)   / mapLongitudeDelta * mapW;
        	data.sensorY = mapY + mapH - (pos.latitude  - map.bottomPosition) / mapLatitudeDelta  * mapH;
        	data.pixelPerMeter = pixelPerMeter;
       		mapRenderer.render(data);
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
	
	@Override public void listeningStarted(AbstractSensor receiver) {}
	@Override public void listeningStopped(AbstractSensor receiver) 
	{
		currentEvents.remove(receiver.getSensorModel());
		repaint();
	}	
}
