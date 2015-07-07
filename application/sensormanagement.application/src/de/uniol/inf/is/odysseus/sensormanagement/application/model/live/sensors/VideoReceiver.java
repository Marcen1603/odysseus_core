package de.uniol.inf.is.odysseus.sensormanagement.application.model.live.sensors;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import de.uniol.inf.is.odysseus.sensormanagement.application.Application;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Event;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.LiveSensor;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public class VideoReceiver extends LiveSensor
{
	private FFmpegFrameGrabber capture;
	private Thread thread;
	
	public VideoReceiver(RemoteSensor sensor) 
	{
		super(sensor);
	}
	
	@Override protected void onStart() throws IOException
	{
		super.onStart();
				
		capture = null;
		startReceiverThread();
	}

	private void startReceiverThread()
	{
		thread = new Thread()
		{
			@Override public void run() 
			{
				System.out.println("Listening to " + getStreamUrl());
				capture = new FFmpegFrameGrabber(getStreamUrl());
				
				try
				{
					capture.start();
					System.out.println("capture returned");
				
	                while (isRunning())
	                {
						IplImage image = capture.grab();
						Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));								
						long now = c.getTimeInMillis();
							
						if (image != null && !image.isNull())
						{                        
	                        double doubleTime = now / 1000.0;
	                        double eventSecondsValid = 1.0;//secondsPerFrame * 2.0;
		                        
	                        Event e = new Event(getSensorModel(), image.clone(), doubleTime, eventSecondsValid);
	                        sensorDataReceived.raise(VideoReceiver.this, e);
						}
		/*					
							while (System.nanoTime() - startTime < currentFrame * secondsPerFrame * 1.0e9 )
							{								
							}*/
					}
				}
                catch (Exception e)
                {
                	// TODO: Signal to application that an error occured in receiver. Do not call stop here!
//                	VideoReceiver.this.stop();
                	Application.showException(e);;
                }
				finally
				{
					releaseCapture();
				}				
			}
		};		
		thread.start();		
	}
	
	@Override protected void onStop()
	{
		if (thread != null && Thread.currentThread() != thread)
		{
			try { 
				thread.join(2000);
				if (thread.isAlive())
				{
					// TODO: FFMpegFrameGrabber hangs in a JNI call and it's not possible to force a thread stop in Java...
					thread.interrupt();
					Application.showException(new Exception("Could not stop receiving video thread!"));
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			thread = null;
		}

		super.onStop();
	}

	private synchronized void releaseCapture() 
	{
		if (capture != null)
		{
			try {
				capture.release();
				capture.stop();			
			} catch (Exception e) {			
				e.printStackTrace();
			}
			capture = null;
		}	
	}
}
