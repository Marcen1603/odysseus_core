package de.uniol.inf.is.odysseus.receivevideostream;

import javax.swing.JOptionPane;

import org.bytedeco.javacv.FrameGrabber;

public class ReceiveVideoStreamApp 
{
	private static ImageFrame frame;
	
	public static void main(String[] args) 
	{	
		if (args.length < 1)
		{
			JOptionPane.showMessageDialog(null, "Provide video stream Url as command line parameter", "Usage", JOptionPane.ERROR_MESSAGE);
		}
		
		try 
		{
			frame = new ImageFrame(args[0]);
		} 
		catch (FrameGrabber.Exception e) 
		{
			showException(e);
			System.exit(1);
		}
	}
	
	public static void showException(Exception e)
	{
		String msg = e.getMessage();
		
		if (e.getCause() != null)
			msg += "\nCaused by: " + e.getCause().getMessage();
		
		JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
}