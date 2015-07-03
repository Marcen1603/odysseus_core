package de.uniol.inf.is.odysseus.sensormanagement.application.view.live;

import javax.swing.Icon;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.LiveInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.live.LiveSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewException;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;
import de.uniol.inf.is.odysseus.sensormanagement.client.executor.ILoggable;

public class LiveViewSensor extends ViewSensor implements ILoggable 
{
	@Override public LiveSensor getSensor() { return (LiveSensor) super.getSensor(); }
	
	public LiveViewSensor(Session session, AbstractSensor sensor, ViewInstance instance) 
	{
		super(session, sensor, instance);
	}

	@Override public void treeDblClick()
	{
		for (AbstractInstance instance : getSession().getSensorManager().getInstances())
			if (instance.getSensorById(getSensor().getSensorModel().id) != null)
			{
				new SensorBoxConfigDialog((LiveSession) getSession(), (LiveInstance) instance, getSensor().getRemoteSensor());
				return;
			}

		throw new ViewException("No box found for sensor");
	}

	@Override public Icon getIcon() 
	{
        if (getSensor() != null && getSensor().getRemoteSensor().isLogging()) 
           	return TreeCellRenderer.sensorIconRec;
        else
        	return TreeCellRenderer.sensorIcon;
	}

	@Override public void startLogging() { getSensor().getRemoteSensor().startLogging(); }
	@Override public void stopLogging()  { getSensor().getRemoteSensor().stopLogging();  }
}
