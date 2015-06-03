package de.uniol.inf.is.odysseus.sensormanagement.application.view.live;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory;
import de.uniol.inf.is.odysseus.sensormanagement.application.SensorFactory.SensorFactoryEntry;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ChangeListener;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Receiver;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.SensorBox;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.SensorBoxConfigDialog;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewEntity;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewException;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Visualization;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.scene.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.RemoteSensor;

public class LiveSession extends Session
{
	private static final long serialVersionUID = 1L;
	
	private List<ViewSensorBox> sensorBoxes = new ArrayList<ViewSensorBox>();
	
	private Map<RemoteSensor, ViewSensor> 	viewSensors  	= new IdentityHashMap<>();
	private ChangeListener boxListener;
	
	public LiveSession(Scene scene) 
	{
		super(scene);
		
		boxListener = new ChangeListener() 
			{ 
				@Override public void onBoxChanged(SensorBox box)			{ LiveSession.this.onSensorBoxChanged(box); }			
				@Override public void onSensorAdded(RemoteSensor sensor)	{ LiveSession.this.onSensorAdded(sensor); } 
				@Override public void onSensorChanged(RemoteSensor sensor)	{ LiveSession.this.onSensorChanged(sensor); }
				@Override public void onSensorRemoved(RemoteSensor box) 	{ LiveSession.this.onSensorRemoved(box); }
			};		
	}
	
	public synchronized void remove()
	{		
		for (ViewSensor vs : viewSensors.values())
			vs.stopVisualization();
		
		for (ViewSensorBox box : sensorBoxes)
			if (box.box != null)
				box.box.close();
	}	
	
	public synchronized void addSensorBox(String ethernetAddr) throws MalformedURLException
	{
		ViewSensorBox viewBox = new ViewSensorBox(ethernetAddr, "System", "manager");
		sensorBoxes.add(viewBox);
	}
	
	@Override public double getNow() 
	{ 
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return calendar.getTimeInMillis() / 1000.0;		
	}

	private ViewSensorBox getViewSensorBox(SensorBox sensorBox)
	{
		for (ViewSensorBox vbox : sensorBoxes)
			if (vbox.box == sensorBox)
				return vbox;
		
		return null;		
	}
	
	protected synchronized void onSensorBoxChanged(SensorBox box) 
	{
		getViewSensorBox(box).changed();
	}

	public synchronized void removeSensorBox(ViewSensorBox vBox) 
	{
		vBox.remove();
		sensorBoxes.remove(vBox);		
	}
	
	private SensorBox getBoxBySensor(RemoteSensor sensor)
	{
		for (ViewSensorBox box : sensorBoxes)
		{
			if (box.box != null && box.box.getClient().getSensors().contains(sensor))
				return box.box;
		}
		return null;
	}
	
	protected synchronized void onSensorAdded(RemoteSensor sensor) 
	{
		ViewSensorBox viewBox = getViewSensorBox(getBoxBySensor(sensor));		
		viewSensors.put(sensor, new ViewSensor(viewBox, sensor));
		
		onSensorChanged(sensor);
	}	
	
	protected synchronized void onSensorChanged(RemoteSensor sensor) 
	{
		viewSensors.get(sensor).changed();
	}

	protected synchronized void onSensorRemoved(RemoteSensor sensor) 
	{
		ViewSensor 	viewSensor 	= viewSensors.get(sensor);
		viewSensor.remove();
		viewSensors.remove(sensor);
	}	
		
	private class ViewSensor extends ViewEntity
	{
		protected Visualization visualization = null;
		private RemoteSensor sensor;
		private Receiver receiver;

		public ViewSensor(ViewSensorBox viewBox, RemoteSensor sensor) 
		{
			super(LiveSession.this, viewBox.getNode());
		
			assert(sensor != null);
			
//			this.viewBox = viewBox;
			this.sensor = sensor;
		}
				
		@Override public void startLogging() { sensor.startLogging(); }
		@Override public void stopLogging()  { sensor.stopLogging();  }

		@Override public void startVisualization() 
		{ 
			if (visualization != null) return;
				
			SensorFactoryEntry entry = SensorFactory.getInstance().getSensorType(sensor.getSensorModel2().type);
			
			// TODO: When can this happen?
			// TODO: Use better exception type
			if (entry == null) throw new RuntimeException("Unknown sensor type: " + sensor.getSensorModel2().type);
			
			visualization = entry.createVisualization(LiveSession.this, sensor.getSensorModel2().displayName);
			receiver = entry.createReceiver(sensor);			
			
			try
			{
				receiver.addListener(visualization);				
				addVisualization(visualization);
			}
			catch (Exception e)
			{
				receiver = null;
				visualization = null;
				
				throw new ViewException("Error while starting visualization", e);
			}
		}
		
		@Override public void stopVisualization()  
		{ 
			if (visualization == null) return;
			
			receiver.removeListener(visualization);
			visualization.remove();
			visualization = null;
		}
		
		@Override public void treeDblClick()
		{
			for (ViewSensorBox box : sensorBoxes)
				if (box.box != null && box.box.getClient() == sensor.getClient())
				{
					new SensorBoxConfigDialog(LiveSession.this, box.box, sensor);
					return;
				}

			throw new ViewException("No box found for sensor");
		}

		@Override public Icon getIcon() 
		{
            if (sensor != null && sensor.isLogging()) 
               	return TreeCellRenderer.sensorIconRec;
            else
            	return TreeCellRenderer.sensorIcon;
		}

		@Override public String getDisplayName() 
		{			
			return (sensor != null) ? sensor.getSensorModel2().displayName : "";
		}
	}
	
	private class ViewSensorBox extends ViewEntity
	{
		private String ethernetAddr;
		private SensorBox box;

		public ViewSensorBox(String ethernetAddr, String userName, String password) 
		{
			super(LiveSession.this, getTreeRoot());

			this.ethernetAddr = ethernetAddr;
			this.box = null;
						
			connectToBox(ethernetAddr, userName, password);
			showInTree();
		}
		
		private void showInTree()
		{
			synchronized (LiveSession.this)
			{
				// Expand to first sensor, if sensors are available, expand to box otherwise
				TreePath path;
				if (box != null && box.getClient().getSensors().size() > 0) 
					path = new TreePath(viewSensors.get(box.getClient().getSensors().get(0)).getNode().getPath());
				else
					path = new TreePath(getNode().getPath());
				
				getEntityTree().expandPath(path);
				getEntityTree().scrollPathToVisible(path);
				
//				getEntityTree().setSelectionPath(path);
			}
		}
		
		private void updateTreePosition()
		{
			synchronized (LiveSession.this)
			{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getNode().getParent();
				
				for (int i=0; i<parent.getChildCount(); i++)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
					ViewSensorBox vBox = (ViewSensorBox) node.getUserObject();
					
					if ((box != null && vBox.box == null) || (this.toString().compareTo(vBox.toString()) <= 0))
					{
						parent.insert(getNode(), i);
						break;
					}
				}
				
				getTreeModel().nodeStructureChanged(parent);
			}
		}
		
		private void connectToBox(final String ethernetAddr, final String userName, final String password)
		{
			Thread connectThread = new Thread()
			{
				@Override public void run()
				{
					try 
					{
						SensorBox box = new SensorBox(ethernetAddr, userName, password);
						ViewSensorBox.this.box = box;
						
						box.addBoxListener(boxListener);						
						for (RemoteSensor s : box.getClient().getSensors())
							onSensorAdded(s);
						onSensorBoxChanged(box);
						
						updateTreePosition();
						showInTree();
					} 
					catch (Exception e) 
					{
//						Application.showException(e);
						System.err.println("Couldn't connect to " + ethernetAddr + ": " + e.getMessage());
						
						LiveSession.this.removeSensorBox(ViewSensorBox.this);
						return;
					}					
				}
			};
			connectThread.start();
		}

		@Override public void startLogging() { box.startLogging(); }
		@Override public void stopLogging()  { box.stopLogging();  }

		@Override public void startVisualization() {}
		@Override public void stopVisualization() {}
		
		@Override public void treeDblClick()
		{
//			SensorBoxConfigDialog dialog = 
			new SensorBoxConfigDialog(LiveSession.this, box);			
		}

		@Override public Icon getIcon() 
		{
			return TreeCellRenderer.sensorBoxIcon;
		}

		@Override
		public String getDisplayName() 
		{
			return (box != null) ? box.toString() : ("Connecting to " + ethernetAddr + "...");
		}
	}

	@Override
	public synchronized void debugBtn(int code) 
	{
	}
}
