package de.uniol.inf.is.odysseus.sensormanagement.application.model.playback;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory.SensorFactoryEntry;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.CallbackListener;

public class PlaybackSensor extends Receiver
{
	private Playback playback;
	private SensorFactoryEntry sensorEntry;
	private TreeSet<PlaybackReceiver> playbackReceivers;
	private CallbackListener<Receiver, Event> receiverListener;
	private PlaybackReceiver currentReceiver = null;
	
	private double startTime = -1;
	private double endTime = -1;
	
	public TreeSet<PlaybackReceiver> getReceivers() { return playbackReceivers; }
	public double getStartTime() { return startTime; }
	public double getEndTime() { return endTime; }
	public SensorFactoryEntry getSensorEntry() { return sensorEntry; }
	
	public boolean isPlaying() { return currentReceiver != null && currentReceiver.isRunning() && playback.getNow() > currentReceiver.getStartTime(); }	
	
	public PlaybackSensor(Playback playback, SensorModel sensorInfo) 
	{			
		super(sensorInfo);
		
		assert(sensorInfo != null);
		
		this.playback = playback;
		
		sensorEntry = SensorFactory.getInstance().getSensorType(sensorInfo.type);
		playbackReceivers = new TreeSet<PlaybackReceiver>(
				new Comparator<PlaybackReceiver>()
				{
					@Override public int compare(PlaybackReceiver recv1, PlaybackReceiver recv2) 
					{
						return Double.compare(recv1.getStartTime(), recv2.getStartTime());
					}
					
				});
		
		receiverListener = new CallbackListener<Receiver, Event>()
		{
			@Override public void raise(Receiver source, Event event) 
			{
				PlaybackSensor.this.sensorDataReceived.raise(PlaybackSensor.this, event);
			}
			
			@Override public void listeningStarted(Receiver receiver) {}
			@Override public void listeningStopped(Receiver receiver) {}			
		};
	}		

	@Override
	protected void onStart() throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
	}	
	
	public void addReceiver(PlaybackReceiver recv) 
	{
		// TODO: Check if receivers overlap. Receivers for one PlaybackSensor may never overlap
		
		playbackReceivers.add(recv);
		
		if (startTime 	== -1 || startTime 	> recv.getStartTime()) 	startTime = recv.getStartTime();
		if (endTime 	== -1 || endTime 	< recv.getEndTime()) 	endTime   = recv.getEndTime();
	}		
		
	public void doEvent() throws IOException
	{
		if (playback.getNow() > endTime) return;
		
		// If there is no activeReceiver, find receiver which has data for "now", or the next receiver which has data for "now".			
		// --> Get the first receiver with endTime after "now"			
		if (currentReceiver == null || !currentReceiver.isRunning())
			if (!findCurrentReceiver()) return;
		
		if (!currentReceiver.doStuffTill(playback.getNow()))
		{
			currentReceiver.sensorDataReceived.removeListener(receiverListener);
			currentReceiver = playbackReceivers.higher(currentReceiver);
			
			if (currentReceiver != null)
				currentReceiver.sensorDataReceived.addListener(receiverListener);
			
			System.out.println("get next receiver for " + getSensorModel().displayName + " [" + getSensorModel().type +  "] ...");
		}
	}	
	
	private boolean findCurrentReceiver() throws IOException
	{
		if (currentReceiver != null)
			currentReceiver.sensorDataReceived.removeListener(receiverListener);
		
		System.out.println("get receiver for " + getSensorModel().displayName + " [" + getSensorModel().id +  "] ...");		
		
		Iterator<PlaybackReceiver> iter = playbackReceivers.iterator();
		while (iter.hasNext())
		{
			PlaybackReceiver recv = iter.next();
			if (recv.getEndTime() > playback.getNow())
			{
				currentReceiver = recv;
				break;
			}
		}				
		
		if (currentReceiver == null)
			return false;
		
		currentReceiver.sensorDataReceived.addListener(receiverListener);
		currentReceiver.setPlaybackPosition(playback.getNow());
		
		playback.onPlayStateChanged.raise(this, null);		
		
		return true;
	}
	
	public void nowChanged() throws IOException
	{
		if (currentReceiver == null) return;
		
		if (playback.getNow() < currentReceiver.getStartTime() || playback.getNow() >= currentReceiver.getEndTime())
		{
			findCurrentReceiver();
		}
		else
		{
			currentReceiver.setPlaybackPosition(playback.getNow());
		}
	}
}