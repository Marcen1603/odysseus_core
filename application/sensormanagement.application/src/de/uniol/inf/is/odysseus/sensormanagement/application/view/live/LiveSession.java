package de.uniol.inf.is.odysseus.sensormanagement.application.view.live;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.tree.DefaultMutableTreeNode;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.LiveSensorManager;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewInstance;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.ILoggable;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.SimpleCallbackListener;

public class LiveSession extends Session
{
	private static final long serialVersionUID = 1L;
	
	@Override protected LiveSensorManager createSensorManager(Scene scene) 
	{ 
		return new LiveSensorManager(scene); 
	}
	
	@Override protected LiveViewSensor createViewSensor(Session session, AbstractSensor sensor, ViewInstance instance) 
	{ 
		return new LiveViewSensor(session, sensor, instance); 
	}
	
	@Override protected LiveViewInstance createViewInstance(Session session, String ethernetAddr, DefaultMutableTreeNode parentNode) 
	{ 
		return new LiveViewInstance(session, ethernetAddr, parentNode); 
	}
	
	@Override public LiveSensorManager getSensorManager() 
	{ 
		return (LiveSensorManager) super.getSensorManager(); 
	}
	
	public LiveSession(Scene scene) throws IOException 
	{
		super(scene);
		
		getSensorManager().onConnectFailed.addListener(new SimpleCallbackListener<LiveSensorManager, LiveSensorManager.ConnectData>() {
			@Override public void raise(LiveSensorManager sourceManager, LiveSensorManager.ConnectData connectData) { 
				onConnectFailed(connectData); 
			}});		
	}
	
	protected void onConnectFailed(LiveSensorManager.ConnectData connectData) 
	{
		System.err.println("Couldn't connect to " + connectData.ethernetAddr + ": " + connectData.e.getMessage());
				
		removeInstance(getViewInstance(connectData.ethernetAddr));
	}

	public synchronized void addInstance(String instanceName, String ethernetAddr) throws MalformedURLException
	{
		ViewInstance viewInstance = new LiveViewInstance(this, ethernetAddr, getTreeRoot());
		super.addInstance(viewInstance);
		
		getSensorManager().connectToInstance(instanceName, ethernetAddr, "System", "manager");		
	}
	
	@Override public double getNow() 
	{ 
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTimeInMillis() / 1000.0;		
	}	

	@Override
	public synchronized void debugBtn(int code) 
	{
	}
	
	public void startLoggingButtonClick() 
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (getEntityTree().getSelectionPath().getLastPathComponent());   		
		if (selectedNode == null) return;
			
		Object obj = selectedNode.getUserObject();
		if (obj instanceof ILoggable)
		{
			((ILoggable) obj).startLogging();
		}
	}

	public void stopLoggingButtonClick() 
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) (getEntityTree().getSelectionPath().getLastPathComponent());   		
		if (selectedNode == null) return;

		Object obj = selectedNode.getUserObject();
		if (obj instanceof ILoggable)
		{
			((ILoggable) obj).stopLogging();
		}
	}				
}
