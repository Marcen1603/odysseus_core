package de.uniol.inf.is.odysseus.sensormanagement.application.view.sensors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewException;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackReceiver;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.LogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.logging.TextLogMetaData;
import de.uniol.inf.is.odysseus.sensormanagement.common.types.SensorModel;

public abstract class TextFilePlayback extends PlaybackReceiver 
{
	private FileInputStream 	fileStream;
	private BufferedReader 		rawFile;	
	private double 				lastEventTime = -1;
	
	public TextFilePlayback(SensorModel sensorModel, LogMetaData logMetaData) 
	{
		super(sensorModel, logMetaData);
	}

	@Override protected void onStart()
	{
		TextLogMetaData textLogMetaData = (TextLogMetaData) logMetaData;
		
		String rawFileName = textLogMetaData.path + textLogMetaData.rawFile;
		try 
		{
			fileStream = new FileInputStream(rawFileName);
			rawFile = new BufferedReader(new InputStreamReader(fileStream));
//			videoFile = new RandomAccessFile(videoFile, "r");
		} 
		catch (FileNotFoundException e) 
		{
			throw new ViewException(e);
		}		
		
		lastEventTime = getStartTime();
	}

	@Override protected void onStop()
	{
		try 
		{
			rawFile.close();
		} 
		catch (IOException e) 
		{
			throw new ViewException(e.getMessage());
		}
	}
	
	@Override
	// TODO: This method is public for testing purposes
	final public Event getNextEvent() throws IOException
	{
		if (rawFile == null) throw new ViewException("videoFile == null");
		
		String nextLine;
		Event ev = null;
		do
		{
			nextLine = rawFile.readLine();
			if (nextLine == null) return null;
			
			ev = parseLine(nextLine);
		}
		while (ev == null);
    
		lastEventTime = ev.getTimeStamp();
		return ev;
	}
		
	private void seek(long position) throws IOException
	{
		if (position >= fileStream.getChannel().size())
			return;
		
		fileStream.getChannel().position(position);
		rawFile = new BufferedReader(new InputStreamReader(fileStream));
	}
	
	@Override protected void setPlaybackPositionInternal(double now) throws IOException
	{
		long size = fileStream.getChannel().size();
		
		if (now <= getStartTime())
			seek(0);
		else
		if (now > getEndTime())
			seek(size);
		else
		{
			long startPos 	 = 0;
			long endPos   	 = size;
				
			if (lastEventTime < now)
				startPos = fileStream.getChannel().position();
			else
				endPos = fileStream.getChannel().position();					

			double diff = 0.0f;
			int steps = 1;
			while (steps < 5000)
			{
				long mid = startPos / 2 + endPos / 2;
					
				// seek the specified position. If getNextEvent returns null, we are too close to the end of the file 
				// and have to go back one line. A line is around 10k characters, so we rewind 5k characters.
				Event e = null;
				while (e == null)
				{
					seek(mid);
					rawFile.readLine();	// Discard next line since we may be in the middle of a line
					e = getNextEvent();
					if (e == null)
						mid -= 5000;
				}

				diff = lastEventTime - now; 
					
				if (diff > -2.0 && diff < 0.0)
					break;
					
				if (lastEventTime <= now)
					startPos  = mid;
				else
					endPos = mid;
				steps++;
			}
				
			System.out.println(steps + " steps to find time, last diff = " + diff);
		}
	}

	public abstract Event parseLine(String nextLine);
}
