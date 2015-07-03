package de.uniol.inf.is.odysseus.sensormanagement.application;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors.DummyReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors.GPSReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors.Lms1xxReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors.VideoReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.PlaybackReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.sensors.DummyPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.sensors.GPSPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.sensors.Lms1xxPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.sensors.VideoPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization.AbstractMapRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization.Lms1xxVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization.TextVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization.VideoVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;


public class SensorFactory 
{
	static private SensorFactory instance = null;
	static public  SensorFactory getInstance() 
	{
		if (instance == null)
		{
			instance = new SensorFactory();
			instance.addSensorType("LMS1xx",  new SensorFactoryEntry(Lms1xxPlayback.class, Lms1xxReceiver.class, Lms1xxVisualization.class, Lms1xxVisualization.MapRenderer.class));
			instance.addSensorType("IntegratedCamera", new SensorFactoryEntry(VideoPlayback.class,  VideoReceiver.class,  VideoVisualization.class, VideoVisualization.MapRenderer.class));
			instance.addSensorType("BaslerCamera", new SensorFactoryEntry(VideoPlayback.class,  VideoReceiver.class,  VideoVisualization.class, VideoVisualization.MapRenderer.class));			
			instance.addSensorType("OptrisCamera", new SensorFactoryEntry(VideoPlayback.class,  VideoReceiver.class,  VideoVisualization.class, VideoVisualization.MapRenderer.class));
			instance.addSensorType("GPS", new SensorFactoryEntry(GPSPlayback.class,  GPSReceiver.class,  TextVisualization.class, null));
			instance.addSensorType("Dummy", new SensorFactoryEntry(DummyPlayback.class,  DummyReceiver.class,  TextVisualization.class, null));
		}				
		
		return instance;
	}
	
	private SensorFactory() {}
	
	// *************************************************************************************
	
	private Map<String, SensorFactoryEntry> entries = new HashMap<>(); 
	
	public void addSensorType(String name, SensorFactoryEntry e)
	{
		entries.put(name.toLowerCase(), e);
	}
	
	public SensorFactoryEntry getSensorType(String name)
	{
		return entries.get(name.toLowerCase());
	}

	public static class SensorFactoryEntry
	{
		private Class<? extends PlaybackReceiver> playbackClass;
		private Class<? extends AbstractSensor> sensorClass;
		private Class<? extends Visualization> visualizationClass;
		private Class<? extends AbstractMapRenderer> mapRendererClass;

		public SensorFactoryEntry(Class<? extends PlaybackReceiver> playbackClass, 
								  Class<? extends AbstractSensor> sensorClass, 
								  Class<? extends Visualization> visualizationClass,
								  Class<? extends AbstractMapRenderer> mapRendererClass) 
		{
			this.playbackClass = playbackClass;
			this.sensorClass = sensorClass;
			this.visualizationClass = visualizationClass;
			this.mapRendererClass = mapRendererClass;
		}
		
		public PlaybackReceiver createPlayback(SensorModel sensorModel, LogMetaData logMetaData)		
		{
			try 
			{
				return playbackClass.getDeclaredConstructor(SensorModel.class, LogMetaData.class).newInstance(sensorModel, logMetaData);
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
			{
				throw new RuntimeException(e);
			}
		}
		
		public AbstractSensor createSensor(RemoteSensor sensor)		
		{
			try 
			{
				return sensorClass.getDeclaredConstructor(RemoteSensor.class).newInstance(sensor);
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
			{
				throw new RuntimeException(e);
			}
		}		
		
		public Visualization createVisualization(Session session, String displayName)		
		{
			try 
			{
				return visualizationClass.getDeclaredConstructor(Session.class, String.class).newInstance(session, displayName);
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
			{
				throw new RuntimeException(e);
			}
		}

		public AbstractMapRenderer createMapRenderer(Session session) 
		{
			try 
			{
				return mapRendererClass == null ? null : mapRendererClass.getDeclaredConstructor(Session.class).newInstance(session);
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
			{
				throw new RuntimeException(e);
			}
		}			
	}
}
