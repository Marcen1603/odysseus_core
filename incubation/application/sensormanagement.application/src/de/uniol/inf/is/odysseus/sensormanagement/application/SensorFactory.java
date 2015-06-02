package de.uniol.inf.is.odysseus.sensormanagement.application;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.DummyPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.DummyReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.GPSPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.GPSReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.Lms1xxPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.Lms1xxReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.Lms1xxVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.TextVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.VideoPlayback;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.VideoReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors.VideoVisualization;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;


public class SensorFactory 
{
	static private SensorFactory instance = null;
	static public  SensorFactory getInstance() 
	{
		if (instance == null)
		{
			instance = new SensorFactory();
			instance.addSensorType("LMS1xx",  new SensorFactoryEntry(Lms1xxPlayback.class, Lms1xxReceiver.class, Lms1xxVisualization.class));
			instance.addSensorType("IntegratedCamera", new SensorFactoryEntry(VideoPlayback.class,  VideoReceiver.class,  VideoVisualization.class));
			instance.addSensorType("BaslerCamera", new SensorFactoryEntry(VideoPlayback.class,  VideoReceiver.class,  VideoVisualization.class));			
			instance.addSensorType("OptrisCamera", new SensorFactoryEntry(VideoPlayback.class,  VideoReceiver.class,  VideoVisualization.class));
			instance.addSensorType("GPS", new SensorFactoryEntry(GPSPlayback.class,  GPSReceiver.class,  TextVisualization.class));
			instance.addSensorType("Dummy", new SensorFactoryEntry(DummyPlayback.class,  DummyReceiver.class,  TextVisualization.class));
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
		private Class<? extends PlaybackReceiver> 	playbackClass;
		private Class<? extends Receiver> 			receiverClass;
		private Class<? extends Visualization> 		visualizationClass;

		public SensorFactoryEntry(Class<? extends PlaybackReceiver> playbackClass, 
								  Class<? extends Receiver> receiverClass, 
								  Class<? extends Visualization> visualizationClass) 
		{
			this.playbackClass = playbackClass;
			this.receiverClass = receiverClass;
			this.visualizationClass = visualizationClass;
		}
		
		public PlaybackReceiver createPlayback(LogMetaData logMetaData)		
		{
			try 
			{
				return playbackClass.getDeclaredConstructor(LogMetaData.class).newInstance(logMetaData);
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) 
			{
				throw new RuntimeException(e);
			}
		}
		
		public Receiver createReceiver(RemoteSensor sensor)		
		{
			try 
			{
				return receiverClass.getDeclaredConstructor(RemoteSensor.class).newInstance(sensor);
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
	}
}
