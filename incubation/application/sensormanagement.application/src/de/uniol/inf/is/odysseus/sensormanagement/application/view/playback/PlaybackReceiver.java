package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.io.IOException;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ReceiverListener;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;

public abstract class PlaybackReceiver extends Receiver
{
	private double startTime;
	private double endTime;
	
	protected LogMetaData logMetaData;	
	
	public double getStartTime()	{ return startTime; }
	public double getEndTime()	{ return endTime;   }	
	
	public PlaybackReceiver(LogMetaData logMetaData)
	{
		super(logMetaData.sensor);
		
		this.logMetaData = logMetaData;
		
		startTime = logMetaData.startTime / 1000.0;
		endTime   = logMetaData.endTime / 1000.0;
	}
	
	private Event nextEvent = null;
	private Event savedEvent = null;
	
	// Fetches events with getNextEvent till the timestamp of the returned event is in the future. This event gets saved as nextEvent 
	// Return true if the receiver is done playing (no more events for "now")
	public final boolean doStuffTill(double now) throws IOException
	{
		if (!isRunning()) return false;		
		
		if (nextEvent != null && nextEvent.getTimeStamp() > now)
		{
		
		}
		else
		{
			// TODO: Die Klammerung nach else wurde eingeführt, damit Sensordaten nicht doppelt
			// an die Visualisierung gesendet werden, falls es zu einem Zeitpunkt noch keine neuen Daten gibt
			do
			{
				savedEvent = nextEvent;
				nextEvent = getNextEvent();
				if (nextEvent == null)
					break;
			}
			while (nextEvent.getTimeStamp() < now);
				
/*			if (savedEvent != null)
				System.out.println("savedEvent.timeStamp = " + (long)(savedEvent.getTimeStamp() * 1000.0));*/
			
			for (ReceiverListener listener : listenerList)
			{
				listener.sensorDataReceived(getSensorModel(), savedEvent);			
			}		
		}
		
		return nextEvent != null;
	}

	public final void setPlaybackPosition(double now) throws IOException
	{
		if (!isRunning()) return;
		
		long time = System.nanoTime();
		try
		{
			setPlaybackPositionInternal(now);
		}
		catch (IOException e)
		{
			stop();
			throw e;
		}
		
		nextEvent = getNextEvent();
		System.out.println("Receiver " + this.toString() + " setPlaybackPosition + getNextEvent duration: " + (System.nanoTime() - time) / 1.0e6 + " ms");
		
		savedEvent = null;
	}
	
	protected abstract Event getNextEvent() throws IOException;	
	protected abstract void setPlaybackPositionInternal(double now) throws IOException;
}
