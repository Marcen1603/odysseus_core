package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.nio.ByteBuffer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class SampleDashboardPart extends AbstractDashboardPart {

	@Override
	public void createPartControl(Composite parent) {
		Label label = new Label(parent, SWT.BORDER);
		label.setText("SampleDashboardPart with settings " + getConfiguration());
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
	}
	
	@Override
	public void save(ByteBuffer buffer) {
	}

	@Override
	public void load(ByteBuffer buffer) {
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		System.out.println(element);
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}


}
