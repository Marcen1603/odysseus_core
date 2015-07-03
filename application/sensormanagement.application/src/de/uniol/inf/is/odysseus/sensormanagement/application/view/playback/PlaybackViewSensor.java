package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import javax.swing.Icon;

import de.uniol.inf.is.odysseus.sensormanagement.application.model.AbstractSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.model.playback.PlaybackSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.Session;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewInstance;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.ViewSensor;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities.TreeCellRenderer;

public class PlaybackViewSensor extends ViewSensor
{
	public PlaybackViewSensor(Session session, AbstractSensor sensor, ViewInstance instance) 
	{
		super(session, sensor, instance);
	}
	
	@Override public PlaybackSensor getSensor() { return (PlaybackSensor) super.getSensor(); }

	@Override public Icon getIcon() 
	{
		return getSensor() != null && getSensor().isPlaying() ? TreeCellRenderer.sensorIconPlay : super.getIcon();
	}	
}
