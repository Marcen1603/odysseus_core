package de.uniol.inf.is.odysseus.sensormanagement.application;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.MainFrame;

public class Application 
{
	static MainFrame view = null;
	
	public static MainFrame getMainFrame()
	{
		return view;
	}	
	
	public static void main(String[] args) 
	{
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
		System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
				
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					view = new MainFrame();					
					view.onCreate();
					view.setVisible(true);
				} 
				catch (Exception e) 
				{
					showException(e);
				}
			}
		});
	}
	
	public static void showException(Exception e)
	{
		String msg = e.getMessage();
		
		if (e.getCause() != null)
			msg += "\nCaused by: " + e.getCause().getMessage();
		
		JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	}
	
	public static class ExceptionHandler implements Thread.UncaughtExceptionHandler 
	{
		public void handle(Throwable thrown) 
		{
			// for EDT exceptions
			handleException(Thread.currentThread().getName(), thrown);
		}

		public void uncaughtException(Thread thread, Throwable thrown) 
		{
			// for other uncaught exceptions
			handleException(thread.getName(), thrown);
		}

		protected void handleException(String tname, Throwable thrown) 
		{
			System.err.println("Exception on " + tname);
			thrown.printStackTrace();

			String msg = thrown.getMessage();
			
			if (thrown.getCause() != null)
				msg += "\nCaused by: " + thrown.getCause().getMessage();			
			
			JOptionPane.showMessageDialog(view, "Exception on " + tname + "\n" + thrown.getClass().getName() + ": " + msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
