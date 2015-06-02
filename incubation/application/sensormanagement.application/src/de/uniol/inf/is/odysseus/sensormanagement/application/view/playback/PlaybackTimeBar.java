package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.util.List;

import de.jaret.examples.timebars.eventmonitoring.model.EventInterval;
import de.jaret.examples.timebars.eventmonitoring.swing.renderer.EventMonitorHeaderRenderer;
import de.jaret.examples.timebars.eventmonitoring.swing.renderer.EventRenderer;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;
import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.ITimeBarChangeListener;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.strategy.IIntervalSelectionStrategy;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTitleRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.Utilities;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel2;


public class PlaybackTimeBar extends TimeBarViewer
{
	private static final long serialVersionUID = -2581955168846134999L;

	private boolean autoAdjustDisplayRange = true; 
	
	public void setAutoAdjustDisplayRange(boolean autoAdjustDisplayRange) { this.autoAdjustDisplayRange = autoAdjustDisplayRange; }
	
	public boolean getAutoAdjustDisplayRange() { return autoAdjustDisplayRange; } 
	
	public PlaybackTimeBar()
	{	        
        setHierarchyWidth(0);
        setModel(new DefaultTimeBarModel());
        
        setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
        setYAxisWidth(100);
        
        setMarkerDraggingInDiagramArea(true);
        setRegionRectEnable(true);
        setDrawRowGrid(true);
        setHeaderRenderer(new EventMonitorHeaderRenderer());
        setName("Event log");
        setTitleRenderer(new DefaultTitleRenderer());        
        setDrawOverlapping(true);
        getTimeBarViewState().setUseVariableRowHeights(true);
        
        // selection strategy: shortest first
        getDelegate().setIntervalSelectionStrategy(new IIntervalSelectionStrategy() {
            public Interval selectInterval(List<Interval> intervals) {
                Interval result = null; 
                for (Interval interval : intervals) {
                    if (result == null || interval.getSeconds()<result.getSeconds()) {
                        result = interval;
                    }
                }
                return result;
            }
        });
        	        	            
        // change listener
        addTimeBarChangeListener(
        	new ITimeBarChangeListener() 
        	{
        		public void intervalChangeCancelled(TimeBarRow row, Interval interval) 
        		{
        			System.out.println("CHANGE CANCELLED " + row + " " + interval);
        		}

        		public void intervalChangeStarted(TimeBarRow row, Interval interval) {
        			System.out.println("CHANGE STARTED " + row + " " + interval);
        		}

        		public void intervalChanged(TimeBarRow row, Interval interval, JaretDate oldBegin, JaretDate oldEnd) {
        			System.out.println("CHANGE DONE " + row + " " + interval);
        		}

	            public void intervalIntermediateChange(TimeBarRow row, Interval interval, JaretDate oldBegin,
	                    JaretDate oldEnd) {
	                System.out.println("CHANGE INTERMEDIATE " + row + " " + interval);
	            }

	            public void markerDragStarted(TimeBarMarker marker) {
	                System.out.println("Marker drag started "+marker);
	            }

	            public void markerDragStopped(TimeBarMarker marker) {
	                System.out.println("Marker drag stopped "+marker);
	            }	
	        });

        // Do not allow any modifications - do not add an interval modificator!
        // addIntervalModificator(new DefaultIntervalModificator());

        // do not allow row selections
        getSelectionModel().setRowSelectionAllowed(false);

        // register additional renderer
        registerTimeBarRenderer(EventInterval.class, new EventRenderer());

        // do not show the root node
        setHideRoot(true);        
        
        // add a popup menu for EventIntervals
/*        Action action = new AbstractAction("IntervalAction") {
            public void actionPerformed(ActionEvent e) {
                System.out.println("run " + getValue(NAME));
            }
        };
        JPopupMenu pop = new JPopupMenu("Operations");
        pop.add(action);
        registerPopupMenu(EventInterval.class, pop);

        // add a popup menu for the body
        final Action bodyaction = new AbstractAction("BodyAction") {
            public void actionPerformed(ActionEvent e) {
                System.out.println("run " + getValue(NAME));
            }
        };
        pop = new JPopupMenu("Operations");
        pop.add(bodyaction);
        pop.add(new RunMarkerAction(_tbv));
        
        // add the zoom action
        pop.add(new ZoomAction(_tbv));
        // add the rem selection action
        pop.add(new ResetRegionSelectionAction(_tbv));
        
        setBodyContextMenu(pop);

        // sample: check enablement of action in a popup
//        pop.addPopupMenuListener(new PopupMenuListener() {
//            
//            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//                System.out.println(getPopUpInformation().getLeft());
//                System.out.println(getPopUpInformation().getRight().toDisplayString());
//                if (getPopUpInformation().getRight().getHours()>9) {
//                    bodyaction.setEnabled(false);
//                } else {
//                    bodyaction.setEnabled(true);
//                }
//            }
//            
//            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//                // TODO Auto-generated method stub
//                
//            }
//            
//            public void popupMenuCanceled(PopupMenuEvent e) {
//                // TODO Auto-generated method stub
//                
//            }
//        });
        
        
        
        // add a popup menu for the hierarchy
        action = new AbstractAction("HierarchyAction") {
            public void actionPerformed(ActionEvent e) {
                System.out.println("run " + getValue(NAME));
            }
        };
        pop = new JPopupMenu("Operations");
        pop.add(action);
        setHierarchyContextMenu(pop);

        // add a popup menu for the header
        action = new AbstractAction("HeaderAction") {
            public void actionPerformed(ActionEvent e) {
                System.out.println("run " + getValue(NAME));
            }
        };
        pop = new JPopupMenu("Operations");
        pop.add(action);
        setHeaderContextMenu(pop);

        // add a popup menu for the time scale
        action = new AbstractAction("TimeScaleAction") {
            public void actionPerformed(ActionEvent e) {
                System.out.println("run " + getValue(NAME));
            }
        };
        pop = new JPopupMenu("Operations");
        pop.add(action);
        setTimeScaleContextMenu(pop);

        // add a popup menu for the title area
        action = new AbstractAction("TitleAction") {
            public void actionPerformed(ActionEvent e) {
                System.out.println("run " + getValue(NAME));
            }
        };
        pop = new JPopupMenu("Operations");
        pop.add(action);
        setTitleContextMenu(pop);*/

        // add dnd support
/*	        DragSource dragSource = DragSource.getDefaultDragSource();
        DragGestureListener dgl = new TimeBarViewerDragGestureListener();
        DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(_diagram,
                DnDConstants.ACTION_COPY, dgl);

        // add the control panel
        EventMonitoringControlPanel cp = new EventMonitoringControlPanel(_tbv, _tm, 100); // TODO
        getContentPane().add(cp, BorderLayout.SOUTH);*/

        
        
        // make sure the marker is in a certain area when zooming
        // relative display range the marker should be in after zooming
//        final double min = 0.3;
//        final double max = 0.7;
        
//      addPropertyChangeListener(PROPERTYNAME_PIXELPERSECOND, new PropertyChangeListener(){
//                    public void propertyChange(PropertyChangeEvent evt) {
//                        // if not displayed set the viewer to display the marker at the min position
//                        if (!isInRange(_tm.getDate(), min, max)) {
//                            int secondsDisplayed = getSecondsDisplayed();
//                            JaretDate startDate = _tm.getDate().copy().advanceSeconds(-min*secondsDisplayed);
//     //                       setStartDate(startDate);
//                        }
//                    }
//                    
//                });        
	}
	
	public class SensorRow extends DefaultTimeBarRowModel
	{
		private SensorModel2 sensorInfo;

		SensorRow(SensorModel2 sensorInfo)
		{
			super(new DefaultRowHeader(sensorInfo.displayName + " (" + sensorInfo.type + ")"));
			this.sensorInfo = sensorInfo;
		}
		
		public SensorModel2 getSensorInfo() { return sensorInfo; }
	}

	public class PlaybackInterval extends EventInterval
	{
		private PlaybackReceiver recv;

		PlaybackInterval(PlaybackReceiver recv, String title)
		{
			super(	new JaretDate(Utilities.dateFromDoubleTime(recv.getStartTime())),
					new JaretDate(Utilities.dateFromDoubleTime(recv.getEndTime())));
		
			setTitle(title);
			
			this.recv = recv;
		}
		
		public PlaybackReceiver getPlaybackReceiver() { return recv; }
	}
	
	
	public void addSensorRecording(SensorModel2 sensorInfo, PlaybackReceiver recv)
	{
		DefaultTimeBarModel model = (DefaultTimeBarModel) getModel();

		DefaultTimeBarRowModel row = null;
		
		// Get row to sensorInfo
		for (int i=0; i<model.getRowCount(); i++)
		{	
			SensorRow curRow = (SensorRow)model.getRow(i);
			
			if (curRow.getSensorInfo().equals(sensorInfo))
			{
				row = curRow;
				break;
			}
		}
		
		if (row == null)
		{
			row = new SensorRow(sensorInfo);
			model.addRow(row);
		}
		
        EventInterval interval = new PlaybackInterval(recv, "");
        row.addInterval(interval);
        
        model.rowDataChanged(row);
        
        if (autoAdjustDisplayRange)
        {
	        double startTime = Utilities.doubleTimeFromDate(getStartDate().getDate());
	        double endTime   = startTime + getSecondsDisplayed();
	        
			final double startOffset = 5.0f;
			final double endOffset   = 5.0f;
			
			if (startTime > recv.getStartTime())
			{
				double oldStartTime = startTime;			
				startTime = recv.getStartTime();
				
				setStartDate(new JaretDate(Utilities.dateFromDoubleTime(startTime - startOffset)));						
				setSecondsDisplayed(getSecondsDisplayed() + (int)Math.ceil(oldStartTime - startTime), true);
			}
			
			if (endTime < recv.getEndTime())
			{
				endTime = recv.getEndTime();
				
				double secsDisplayed = endTime - startTime + startOffset + endOffset;
				setSecondsDisplayed((int)Math.ceil(secsDisplayed), true);
			}
        }
	}
}
