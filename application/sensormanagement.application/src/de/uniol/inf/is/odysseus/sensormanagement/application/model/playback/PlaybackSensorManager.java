package de.uniol.inf.is.odysseus.sensormanagement.application.model.playback;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Timer;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory.SensorFactoryEntry;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensorManager;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.TextLogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.VideoLogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.ServerInstance;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.Callback;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;

public class PlaybackSensorManager extends AbstractSensorManager
{
	private Object doStepSynchronization = new Object();
	
	private double startTime = -1;
	private double endTime = -1;
	private double previousNow;
	private double now;
	
	private Timer playbackTimer = null;	
	
	public class BeforeStepEventData
	{
		public BeforeStepEventData(double now, double previousNow, double timePassed) 
		{
			this.now = now;
			this.timePassed = timePassed;
			this.previousNow = previousNow;
		}

		public double now, timePassed, previousNow;
	}
	
	public Callback<PlaybackSensorManager, BeforeStepEventData> beforeStepEvent = new Callback<>();
	public Callback<PlaybackSensor, Object> onPlayStateChanged = new Callback<>();
	
	public double getStartTime() { return startTime; }
	public double getEndTime() { return endTime; }
	public double getNow() { return now; }
	
	public PlaybackSensorManager(Scene scene)
	{
		super(scene);
	}
	
	@Override public void start() throws IOException
	{
		Scene scene = getScene();
		
		for (String file : scene.getInstanceFileList())
		{
			File serverInstanceFile = new File(scene.getPath() + file);
			ServerInstance serverInstance = XmlMarshalHelper.fromXmlFile(serverInstanceFile, ServerInstance.class);
			
			AbstractInstance instance = new AbstractInstance(serverInstance.name, serverInstance.ethernetAddr);
			getInstances().add(instance);
			onInstanceAdded.raise(this, instance);
			
			for (SensorModel sensor : serverInstance.sensors)
			{
				PlaybackSensor ps = new PlaybackSensor(this, sensor);
				instance.getSensors().add(ps);
				onSensorAdded.raise(this, ps);
			}
			
			System.out.println(XmlMarshalHelper.toXml(serverInstance));
			
			File dir = serverInstanceFile.getParentFile();
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null)
			{
				for (File child : directoryListing) 
				{
					String fileName = child.getName();
					if (!child.isFile() || !fileName.endsWith("cfg")) 
						continue;

					@SuppressWarnings("rawtypes")
					Class[] logClasses = new Class[]{LogMetaData.class, VideoLogMetaData.class, TextLogMetaData.class};
					LogMetaData logMetaData = (LogMetaData) XmlMarshalHelper.fromXmlFile(child, logClasses);
					
					PlaybackSensor ps = (PlaybackSensor) getSensorById(logMetaData.sensorId);
					if (ps == null)
						throw new RuntimeException("Unknown sensor " + logMetaData.sensorId);
					
					SensorFactoryEntry sensorEntry = SensorFactory.getInstance().getSensorType(ps.getSensorModel().type);					
					PlaybackReceiver recv = sensorEntry.createPlayback(ps.getSensorModel(), logMetaData);
										
					ps.addReceiver(recv);
					
					if (startTime == -1 || startTime > recv.getStartTime()) startTime = recv.getStartTime();
					if (endTime   == -1 || endTime   < recv.getEndTime())   endTime   = recv.getEndTime();								
				}
			}			
		}	
	}
	
	public void goToStart() throws IOException
	{
		jumpToTime(startTime);
	}	
	
	public void jumpToTime(double time) throws IOException
	{
		synchronized( doStepSynchronization )
		{
			previousNow = time;
			now = time;
			for (AbstractInstance instance : getInstances())
				for (AbstractSensor sensor : instance.getSensors())
					((PlaybackSensor) sensor).nowChanged();
			
			doStep(0.0);
		}
	}	
	
	public void playPauseButton() 	
	{
		if (playbackTimer != null)
		{
			playbackTimer.stop();
			playbackTimer = null;
		}
		else
		{		
			double timeDelta = 0.01; //1.0 / 60.0;
		    playbackTimer = new Timer((int) (timeDelta * 1000.0), 
		    					new ActionListener() 
		    					{
		    						private long lastTime = System.nanoTime();
		    	
			      					@Override public void actionPerformed(ActionEvent evt) 
			      					{
			      						long timePassedInNano = System.nanoTime() - lastTime;
			      						double timePassed = timePassedInNano / 1.0e9;
//			      						double realTimePassed = timePassed;
			      						if (timePassed > 0.1)
			      							timePassed = 0.1;
			      						
			      						lastTime = System.nanoTime();
			      						previousNow = now;
			      						now += timePassed;
			      						
/*			      						String display = "Time since start = " + (now - startTime) + "\t(delta = " + timePassed * 1000.0 + "ms)";
			      						if (realTimePassed > timePassed)
			      							display += " CAP! real time passed = " + realTimePassed; 
			      						
			      						System.out.println(display);*/

			      						try 
			      						{
											doStep(timePassed);
										} 
			      						catch (IOException e) 
			      						{
			      							Application.showException(e);
			      							playbackTimer.stop();
			      							playbackTimer = null;
										}
			      					}
		    					});
		    playbackTimer.start();
		    
		    try
		    {
		    	doStep(timeDelta);
		    }
		    catch (Exception e)
		    {
				playbackTimer.stop();
				playbackTimer = null;
				Application.showException(e);
		    }
		}		
	}
	
	public void stopButton()
	{
		if (playbackTimer != null)
		{
			playbackTimer.stop();
			playbackTimer = null;			
			try 
			{
				goToStart();
			} 
			catch (IOException e) 
			{
				Application.showException(e);			
			}
		}
	}
	
	public void step(double timeStep) throws IOException 
	{
		doStep(timeStep);
		previousNow = now;
		now += timeStep;
	}				
	
	private void doStep(double timePassed) throws IOException 
	{
		synchronized( doStepSynchronization )
		{
			beforeStepEvent.raise(this, new BeforeStepEventData(now, previousNow, timePassed));
			
	//		System.out.println("Enter doStep at time " + (long)(now*1000.0));
			
			for (AbstractInstance instance : getInstances())
				for (AbstractSensor sensor : instance.getSensors())
					((PlaybackSensor) sensor).doEvent();			
		}
	}
	
	public void stop() 
	{
		if (playbackTimer != null)
		{
			playbackTimer.stop();
			playbackTimer = null;	
		}		
	}
}
