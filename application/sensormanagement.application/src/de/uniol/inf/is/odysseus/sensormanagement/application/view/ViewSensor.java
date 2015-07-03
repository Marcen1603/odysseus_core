package de.uniol.inf.is.odysseus.sensormanagement.application.view;

import javax.swing.Icon;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.visualization.AbstractMapRenderer;

public class ViewSensor extends ViewEntity
{
	private ViewInstance viewInstance;
	private AbstractSensor sensor;
	private Visualization visualization;
	private AbstractMapRenderer mapRenderer;
	private String constraintString = null;	
	
	public Visualization getVisualization() { return visualization; }
	public String getConstraintString() { return constraintString; }
	public AbstractSensor getSensor() { return sensor; }
	public ViewInstance getViewInstance() { return viewInstance; }
	public AbstractMapRenderer getMapRenderer() { return mapRenderer; }
	
	@Override public String getDisplayName() 
	{ 
		return sensor != null && sensor.getSensorModel() != null ? sensor.getSensorModel().displayName : ""; 	
	}

	@Override public Icon getIcon() 
	{
		return TreeCellRenderer.sensorIconPause;
	}
	
	public ViewSensor(Session session, AbstractSensor sensor, ViewInstance viewInstance) 
	{
		// TODO: Add sensor to correct instance node
		super(session, viewInstance.getNode());
		
		this.sensor = sensor;
		this.viewInstance = viewInstance;
		this.mapRenderer = sensor.getSensorEntry().createMapRenderer(session);
		
		if (getSession().getMap() != null)
			sensor.sensorDataReceived.addListener(getSession().getMap().getSensorDataListener());				
	}
	
	@Override public void remove()
	{
		if (getSession().getMap() != null)
			sensor.sensorDataReceived.removeListener(getSession().getMap().getSensorDataListener());

		super.remove();
	}	
	
	@Override public void startVisualization() 
	{
		if (visualization != null) return;
		if ("disable".equals(constraintString)) return;

		try
		{
			visualization = sensor.getSensorEntry().createVisualization(getSession(), sensor.getSensorModel().displayName);
			visualization.setDisplayConstraints(constraintString);
		
			sensor.sensorDataReceived.addListener(visualization.getSensorDataListener());
		
			getSession().addVisualization(visualization);
			getSession().getTreeModel().nodeStructureChanged(getNode());		
		}
		catch (Exception e)
		{
			visualization = null;
			throw new ViewException("Error while starting visualization", e);
		}
	}
	
	@Override public void stopVisualization()  
	{ 
		if (visualization == null) return;

		sensor.sensorDataReceived.removeListener(visualization.getSensorDataListener());
		
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
}
