package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class NoSettingsDashboardPart extends AbstractDashboardPart {

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
		
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
		
	}
}
