package de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization;

import java.awt.Graphics2D;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;

public abstract class AbstractMapRenderer 
{
	public static class Data
	{
		Graphics2D graphics;
		int mapX, mapY, mapW, mapH;
		double sensorX, sensorY, sensorRot;
		Event event;
		public double pixelPerMeter;
	}

	private Session session;

	public Session getSession() { return session; }
	
	public AbstractMapRenderer(Session session) 
	{
		this.session = session;
	}

	public abstract void render(Data data); 
}
