package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.model.ITimeBarChangeListener;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.Playback;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.PlaybackReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.PlaybackSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.Utilities;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleCallbackListener;

public class PlaybackSession extends Session
{
	private static final long serialVersionUID = 1L;
	
	private Playback playback;
		
	private PlaybackTimeBar 	timeBar;
	private TimeBarMarkerImpl 	currentTimeMarker;
	private boolean				currentTimeMarkerDragged;
	private List<ViewSensor>	viewSensors;
			
	@Override public double getNow() { return playback.getNow(); }
	public Playback getPlayback() { return playback; }
		
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
							playback.jumpToTime(Utilities.doubleTimeFromDate(marker.getDate().getDate()));						
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
									playback.jumpToTime(Utilities.doubleTimeFromDate(date.getDate()));
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
	
	public PlaybackSession(Scene scene) throws IOException
	{
		super(scene);
		
		setupTimeBar();
		
		playback = new Playback(scene);
		
		playback.beforeStepEvent.addListener(new SimpleCallbackListener<Playback, Playback.BeforeStepEventData>()
		{
			@Override public void raise(Playback source, Playback.BeforeStepEventData data) 
			{ 
				try {
					onBeforeStepEvent(data.now, data.previousNow, data.timePassed);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} 
			}
		});

		playback.onPlayStateChanged.addListener(new SimpleCallbackListener<PlaybackSensor, Object>()
		{
			@Override public void raise(PlaybackSensor source, Object obj) 
			{
				getTreeModel().nodeStructureChanged(getTreeRoot());
			}
		});
		
		
		viewSensors = new ArrayList<ViewSensor>(playback.getPlaybackSensors().size());
		for (PlaybackSensor ps : playback.getPlaybackSensors())
		{
			for (PlaybackReceiver recv : ps.getReceivers())
				timeBar.addSensorRecording(ps.getSensorModel(), recv);
			
			viewSensors.add(new ViewSensor(this, ps));
		}
				
		if (scene instanceof PlaybackScene)
		{
			PlaybackScene playbackScene = (PlaybackScene) scene; 

			for (TimeInterval interval : playbackScene.getTimeIntervalList())
			{
				TimeBarMarkerImpl m;
				
		        m = new JumpMarker(interval.startTime);
		        m.setDescription("Start");
		        timeBar.addMarker(m);
		        
		        m = new JumpMarker(interval.endTime);
		        m.setDescription("End");
		        timeBar.addMarker(m);
			}
			
			for (VisualizationConstraints view : playbackScene.getViewList())
			{
				for (Entry<String, String> entry : view.constraintMap.entrySet())
				{
					String sensorId = entry.getKey();
					String constraint = entry.getValue();
					
					if (sensorId.equals("Map"))
					{
						setMapConstraint(constraint);
					}
					else
						getViewSensorById(sensorId).setConstraintString(constraint);
				}
			}			
		}
		
		getTreeModel().nodeStructureChanged(getTreeRoot());
		playback.goToStart();
	}
		
	protected void onBeforeStepEvent(double now, double previousNow, double timePassed) throws IOException 
	{
		// TODO: Better implementation of jump markers, maybe only keep track of next jumpmarker
		// and refresh after every marker / jumpToTime-call ?
		
		if (getScene() instanceof PlaybackScene)
		{
			// Check time intervals if playback is running (timePassed == 0.0 happens when jumping in time. Ignore intervals then.)
			if (timePassed != 0.0)
			{
				List<TimeInterval> list = ((PlaybackScene)getScene()).getTimeIntervalList();
				Iterator<TimeInterval> iter = list.iterator();
				while (iter.hasNext())
				{
					TimeInterval m = iter.next();
						
					// passed over a jumpTimer-end
					if (previousNow < m.endTime && m.endTime < now)
					{
						if (iter.hasNext())
							playback.jumpToTime(iter.next().startTime);
						else
							playback.jumpToTime(list.get(0).startTime);
						break;
					}
				}
			}
		}
		
		if (this == Application.getMainFrame().getCurrentSession())
		{
			double fps = 0.0;
			if (timePassed != 0.0)
				fps = 1 / timePassed;
			
			String timeString = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(Utilities.dateFromDoubleTime(now));
			Application.getMainFrame().setTitle(String.format("%s (Time since start = %.3fs) @ %.2f FPS", timeString, now - playback.getStartTime(), fps));
		}
		
		if (!currentTimeMarkerDragged)
			currentTimeMarker.setDate(new JaretDate(Utilities.dateFromDoubleTime(now)));				
	}
	
	public void toggleFullscreen(boolean fullscreen)
	{
		super.toggleFullscreen(fullscreen);
		
		timeBar.setVisible(!fullscreen);		
	}
	
	@Override public void remove() 
	{
		playback.stop();		
		
		for (ViewSensor vs : viewSensors)
			vs.stopVisualization();
	}
	
	@Override
	public void debugBtn(int code) 
	{
		try
		{
			for (ViewSensor vs : viewSensors)
				vs.startVisualization();
			
			switch (code)
			{
				case 0:
				{
					playback.playPauseButton();
					break;
				}
				
				case 1:
				{
					playback.jumpToTime(playback.getStartTime());
					break;
				}
				
				case 2:
				{
					double timeStep = 0.02;
				
					playback.step(timeStep);
					break;
				}			
			}
		}
		catch (IOException e)
		{
			Application.showException(e);
		}
	}
	
	ViewSensor getViewSensorById(String id)
	{
		for (ViewSensor vs : viewSensors)
		{
			if (vs.getPlaybackSensor().getSensorModel().id.equals(id))
				return vs;
		}
		return null;
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
	
	public static void printTimeInMillis(int y, int m, int d, int h, int min, int s)
	{
		Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c2.set(y, m, d, h, min, s);
		System.out.println(Long.toString(c2.getTimeInMillis()));		
	}	
}
