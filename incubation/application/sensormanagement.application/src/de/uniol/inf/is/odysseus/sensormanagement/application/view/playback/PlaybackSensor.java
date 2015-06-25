package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.Icon;

import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory.SensorFactoryEntry;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ReceiverListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewEntity;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewException;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public class PlaybackSensor extends ViewEntity implements ReceiverListener
{
	private SensorModel sensorInfo;		
	private SensorFactoryEntry sensorEntry;
	private Visualization visualization;
	private TreeSet<PlaybackReceiver> playbackReceivers;
	private PlaybackReceiver currentReceiver = null;
	private String constraintString = null;
	
	private double startTime = -1;
	private double endTime = -1;
	
	public Visualization getVisualization() { return visualization; }
	public TreeSet<PlaybackReceiver> getReceivers() { return playbackReceivers; }
	public SensorModel getSensorInfo() { return sensorInfo; }
	public double getStartTime() { return startTime; }
	public double getEndTime() { return endTime; }
	public String getConstraintString() { return constraintString; }
	
	public PlaybackSensor(PlaybackSession session, SensorModel sensorInfo) throws ViewException 
	{
		super(session, session.getTreeRoot());
		
		assert(sensorInfo != null);
		this.sensorInfo = sensorInfo;
		
		sensorEntry = SensorFactory.getInstance().getSensorType(sensorInfo.type);
		playbackReceivers = new TreeSet<PlaybackReceiver>(
				new Comparator<PlaybackReceiver>()
				{
					@Override public int compare(PlaybackReceiver recv1, PlaybackReceiver recv2) 
					{
						return Double.compare(recv1.getStartTime(), recv2.getStartTime());
					}
					
				});
	}		

	public void addReceiver(PlaybackReceiver recv) 
	{
		// TODO: Check if receivers overlap. Receivers for one PlaybackSensor may never overlap
		
		playbackReceivers.add(recv);
		
		if (startTime 	== -1 || startTime 	> recv.getStartTime()) 	startTime = recv.getStartTime();
		if (endTime 	== -1 || endTime 	< recv.getEndTime()) 	endTime   = recv.getEndTime();
	}		
	
	private double getNow()
	{
		return ((PlaybackSession) getSession()).getNow();
	}
	
	public void doEvent() throws IOException
	{
		if (getNow() > endTime) return;
		
		// If there is no activeReceiver, find receiver which has data for "now", or the next receiver which has data for "now".			
		// --> Get the first receiver with endTime after "now"			
		if (currentReceiver == null || !currentReceiver.isRunning())
			if (!findCurrentReceiver()) return;
		
		if (!currentReceiver.doStuffTill(getNow()))
		{
			currentReceiver.removeListener(this);
			currentReceiver = playbackReceivers.higher(currentReceiver);
			
			if (currentReceiver != null)
				currentReceiver.addListener(this);
			
			System.out.println("get next receiver for " + sensorInfo.displayName + " [" + sensorInfo.type +  "] ...");
		}
	}	
	
	private boolean findCurrentReceiver() throws IOException
	{
		if (currentReceiver != null)
			currentReceiver.removeListener(this);
		
		System.out.println("get receiver for " + sensorInfo.displayName + " [" + sensorInfo.id +  "] ...");		
		
		Iterator<PlaybackReceiver> iter = playbackReceivers.iterator();
		while (iter.hasNext())
		{
			PlaybackReceiver recv = iter.next();
			if (recv.getEndTime() > getNow())
			{
				currentReceiver = recv;
				break;
			}
		}				
		
		if (currentReceiver == null)
			return false;
		
		currentReceiver.addListener(this);
		currentReceiver.setPlaybackPosition(getNow());
		getSession().getTreeModel().nodeStructureChanged(getNode());
		
		return true;
	}
	
	public void nowChanged() throws IOException
	{
		if (currentReceiver == null) return;
		
		double now = getNow();
		if (now < currentReceiver.getStartTime() || now >= currentReceiver.getEndTime())
		{
			findCurrentReceiver();
		}
		else
		{
			currentReceiver.setPlaybackPosition(now);
		}
	}
	
	@Override public void startLogging() { throw new ViewException("Cannot log a playback sensor"); }
	@Override public void stopLogging()  { throw new ViewException("Cannot log a playback sensor"); }

	@Override public void startVisualization() 
	{ 
		if (visualization != null) return;
		if ("disable".equals(constraintString)) return;

		visualization = sensorEntry.createVisualization(getSession(), getSensorInfo().displayName);
		visualization.setDisplayConstraints(constraintString);
		getSession().addVisualization(visualization);
		getSession().getTreeModel().nodeStructureChanged(getNode());
	}
	
	@Override public void stopVisualization()  
	{ 
		if (visualization == null) return;

		visualization.remove();
		visualization = null;
		getSession().getTreeModel().nodeStructureChanged(getNode());
	}
	
	public void setConstraintString(String constraintString)
	{
		this.constraintString = constraintString;
		if (visualization != null)
			visualization.setDisplayConstraints(constraintString);
	}
	
	@Override public void treeDblClick()
	{
	}

	@Override public Icon getIcon() 
	{
		if (currentReceiver != null && currentReceiver.isRunning() && getNow() > currentReceiver.getStartTime())
			return TreeCellRenderer.sensorIconPlay;
		else
			return TreeCellRenderer.sensorIconPause;
	}
	
	@Override public void sensorDataReceived(SensorModel source, Event event) 
	{
		if (visualization != null) visualization.sensorDataReceived(source, event);
		
		if (getSession().getMap() != null)
			getSession().getMap().sensorDataReceived(source, event);
	}
	
	@Override public void listeningStarted(Receiver receiver) 
	{
	}

	@Override public void listeningStopped(Receiver receiver) 
	{
	}
	@Override public String getDisplayName() 
	{
		return (sensorInfo != null) ? sensorInfo.displayName : "";
	}		
}