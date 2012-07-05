package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class SampleDashboardPart extends AbstractDashboardPart {

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		Label label = new Label(parent, SWT.BORDER);
		label.setText("SampleDashboardPart with settings " + getConfiguration());
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		System.out.println(element);
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	@Override
	public Map<String, String> onSave() {
		return null;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		
	}

}
