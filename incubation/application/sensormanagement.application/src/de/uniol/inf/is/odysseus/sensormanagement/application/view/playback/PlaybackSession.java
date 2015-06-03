package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.swing.Timer;

import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.model.ITimeBarChangeListener;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory.SensorFactoryEntry;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.scene.PlaybackScene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.scene.TimeInterval;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.Utilities;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.TextLogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.VideoLogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;

public class PlaybackSession extends Session
{
	private static final long serialVersionUID = 1L;
	
	private List<PlaybackSensor> playbackSensors = new ArrayList<>();

	private Timer playbackTimer = null;
	private double startTime = -1;
	private double endTime = -1;
	private double previousNow;
	private double now;

	private Object doStepSynchronization = new Object();
	
	private PlaybackTimeBar 	timeBar;
	private TimeBarMarkerImpl 	currentTimeMarker;
	private boolean				currentTimeMarkerDragged;
	
	public double getStartTime() 	{ return startTime; }
	public double getEndTime()		{ return endTime;	}
	
	@Override public double getNow() 
	{ 
		return now;
	}
		
	public static void printTimeInMillis(int y, int m, int d, int h, int min, int s)
	{
		Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c2.set(y, m, d, h, min, s);
		System.out.println(Long.toString(c2.getTimeInMillis()));		
	}
	
	private void setupTimeBar()
	{
		timeBar = new PlaybackTimeBar();
		timeBar.setRowHeight(25);
		
		timeBar.setPreferredSize(new Dimension(1000, 220));
		
		timeBar.addTimeBarChangeListener(	
				new ITimeBarChangeListener()
				{
					@Override public void markerDragStarted(TimeBarMarker marker) 
					{
						currentTimeMarkerDragged = true;
					}
					
					@Override public void markerDragStopped(TimeBarMarker marker) 
					{						
						currentTimeMarkerDragged = false;
						
						try 
						{
							jumpToTime(Utilities.doubleTimeFromDate(marker.getDate().getDate()));						
						} 
						catch (IOException e) 
						{
							Application.showException(e);						
						}						
					}
					
					@Override public void intervalChangeStarted(TimeBarRow row,Interval interval) { }
					@Override public void intervalIntermediateChange(TimeBarRow row, Interval interval, JaretDate oldBegin, JaretDate oldEnd) { }
					@Override public void intervalChanged(TimeBarRow row, Interval interval, JaretDate oldBegin, JaretDate oldEnd) { }
					@Override public void intervalChangeCancelled(TimeBarRow row, Interval interval) { }
				});
		
		timeBar.addMouseListener(
            	new MouseAdapter() 
            	{
                    @Override public void mouseClicked(MouseEvent ev) 
                    {
//                        if (e.getClickCount() == 2) 
                        {
                            Point origin = ev.getPoint();
                            Rectangle rect = timeBar.getDelegate().getXAxisRect();
                            if (origin.x >= rect.x && origin.x <= rect.width)
                            {
                            	try 
                            	{
                            		JaretDate date = timeBar.getDelegate().dateForCoord(ev.getX(), ev.getY());
									jumpToTime(Utilities.doubleTimeFromDate(date.getDate()));
									System.out.println("Date at cursor: " + date.toDisplayString() );
								} 
                            	catch (IOException e) 
                            	{
                            		Application.showException(e);
                            	}
                            }
                        }
                    }	
            	});
		
        // add a marker
		timeBar.setMarkerRenderer(new MyMarkerRenderer());
		
        currentTimeMarker = new TimeBarMarkerImpl(true, timeBar.getModel().getMinDate().copy());
        currentTimeMarker.setDescription("Current time");
        currentTimeMarkerDragged = false;
        
        timeBar.addMarker(currentTimeMarker);
        getPresentationPanel().add(timeBar, BorderLayout.SOUTH);		
	}
	
	public PlaybackSession(PlaybackScene scene) throws IOException
	{
		super(scene);
		
		setupTimeBar();
		
/*		JumpMarker für erste BHV-Aufnahme
        // Segelschiff
		jumpMarkerList.add(new JumpMarker(1.4023903534281876E9,   // 10.06.2014 10:52:33.428
										  1.4023904082014453E9)); // 10.06.2014 10:53:28.201
		
		// Kutter
		jumpMarkerList.add(new JumpMarker(1.402391038327892E9,    // 10.06.2014 11:03:58.327
										  1.402391073876045E9));  // 10.06.2014 11:04:33.876
		
		// Frachter
		jumpMarkerList.add(new JumpMarker(1.4023937973471797E9,	  // 10.06.2014 11:49:57.347
										  1.402393866163085E9));  // 10.06.2014 11:51:06.163
		
		// Fähre
		jumpMarkerList.add(new JumpMarker(1.4023958638730824E9,   // 10.06.2014 12:24:23.873
										  1.4023959430889094E9)); // 10.06.2014 12:25:43.088
		
		// Fähre + Handy
		jumpMarkerList.add(new JumpMarker(1.4023964914624376E9,   // 10.06.2014 12:34:51.462
										  1.4023966022596202E9)); // 10.06.2014 12:36:42.259
						*/

		for (String file : scene.getFileList())
		{
			// TODO: Get list of all LogMetaData classes			
			@SuppressWarnings("rawtypes")
			Class[] logClasses = new Class[]{LogMetaData.class, VideoLogMetaData.class, TextLogMetaData.class};
			LogMetaData logMetaData = (LogMetaData) XmlMarshalHelper.fromXmlFile(new File(scene.getPath() + file), logClasses);
			
			SensorFactoryEntry sensorEntry = SensorFactory.getInstance().getSensorType(logMetaData.sensor.type);
			
			PlaybackReceiver recv = sensorEntry.createPlayback(logMetaData);
		
			PlaybackSensor ps = getPlaybackSensor(logMetaData.sensor);
			if (ps == null)
			{
				ps = new PlaybackSensor(this, logMetaData.sensor);
				playbackSensors.add(ps);
			}
			
			ps.addReceiver(recv);
			timeBar.addSensorRecording(ps.getSensorInfo(), recv);
			
			if (startTime == -1 || startTime > recv.getStartTime()) startTime = recv.getStartTime();
			if (endTime   == -1 || endTime   < recv.getEndTime())   endTime   = recv.getEndTime();			
		}
				
		for (TimeInterval interval : scene.getTimeIntervalList())
		{
			TimeBarMarkerImpl m;
			
	        m = new JumpMarker(interval.startTime);
	        m.setDescription("Start");
	        timeBar.addMarker(m);
	        
	        m = new JumpMarker(interval.endTime);
	        m.setDescription("End");
	        timeBar.addMarker(m);
		}
		
		getTreeModel().nodeStructureChanged(getTreeRoot());
		goToStart();
	}
	
	private PlaybackSensor getPlaybackSensor(SensorModel2 sensorInfo)
	{
		for (PlaybackSensor ps : playbackSensors)
		{
			SensorModel2 listSensor = ps.getSensorInfo();
			if (listSensor.type.equals(sensorInfo.type) && listSensor.id.equals(sensorInfo.id))
				return ps;
		}
		
		return null;
	}
	
	public void toggleFullscreen(boolean fullscreen)
	{
		super.toggleFullscreen(fullscreen);
		
		timeBar.setVisible(!fullscreen);		
	}
	
	public void goToStart() throws IOException
	{
		jumpToTime(startTime);
	}

	@Override public void remove() 
	{
		if (playbackTimer != null)
		{
			playbackTimer.stop();
			playbackTimer = null;	
		}
		
		for (PlaybackSensor ps : playbackSensors)
			ps.stopVisualization();
	}
	
	public void jumpToTime(double time) throws IOException
	{
		synchronized( doStepSynchronization )
		{
			previousNow = time;
			now = time;
			for (PlaybackSensor ps : playbackSensors)
				ps.nowChanged();
			
			if (!currentTimeMarkerDragged)
				currentTimeMarker.setDate(new JaretDate(Utilities.dateFromDoubleTime(now)));
			
			doStep(0.0);
		}
	}	
	
	@Override
	public void debugBtn(int code) 
	{
		try
		{
			for (PlaybackSensor ps : playbackSensors)
			{
				// TODO: For the presentation, do not show LMS visualizations
	//			if (!ps.getSensorInfo().getType().equals("LMS1xx"))
					ps.startVisualization();
			}
			
			switch (code)
			{
				case 0:
				{
					playPauseButton();
					break;
				}
				
				case 1:
				{
					jumpToTime(startTime);
					break;
				}
				
				case 2:
				{
					double timeStep = 0.02;
					
					doStep(timeStep);
					previousNow = now;
					now += timeStep;
					break;
				}			
			}
		}
		catch (IOException e)
		{
			Application.showException(e);
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
			      						
			      						if (!currentTimeMarkerDragged)
			      							currentTimeMarker.setDate(new JaretDate(Utilities.dateFromDoubleTime(now)));
			      						
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
	
	private void doStep(double timePassed) throws IOException 
	{
		synchronized( doStepSynchronization )
		{
			// TODO: Better implementation of jump markers, maybe only keep track of next jumpmarker
			// and refresh after every marker / jumpToTime-call ?
			List<TimeInterval> list = ((PlaybackScene) getScene()).getTimeIntervalList();
			Iterator<TimeInterval> iter = list.iterator();
			while (iter.hasNext())
			{
				TimeInterval m = iter.next();
				
				// passed over a jumpTimer-end
				if (previousNow < m.endTime && m.endTime < now)
				{
					if (iter.hasNext())
						jumpToTime(iter.next().startTime);
					else
						jumpToTime(list.get(0).startTime);
					break;
				}
			}
			
			
	//		System.out.println("Enter doStep at time " + (long)(now*1000.0));
			
			for (PlaybackSensor ps : playbackSensors)
				ps.doEvent();
			
			if (Application.getMainFrame().getCurrentSession() == this)
			{
				double fps = 0.0;
				if (timePassed != 0.0)
					fps = 1 / timePassed;
				
				String timeString = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(Utilities.dateFromDoubleTime(now));
				Application.getMainFrame().setTitle(String.format("%s (Time since start = %.3fs) @ %.2f FPS", timeString, now - startTime, fps));
			}
		}
	}	

	// TODO: Show line between two jumpMarkers in TimeBar
	// TODO: Create whole new type LoopMarker which does all this 
	private static class JumpMarker extends TimeBarMarkerImpl
	{
		public JumpMarker(double timeStamp) 
		{
			super(false, new JaretDate(Utilities.dateFromDoubleTime(timeStamp)));
		}
		
		public int getY()
		{
			return 39;
		}
		
		public int getHeight()
		{
			return 10;
		}
	}
}
