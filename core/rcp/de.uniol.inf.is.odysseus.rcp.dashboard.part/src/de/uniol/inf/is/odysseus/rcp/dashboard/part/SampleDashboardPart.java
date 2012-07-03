package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.nio.ByteBuffer;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class SampleDashboardPart extends AbstractDashboardPart {

	@Override
	public void createPartControl(Composite parent) {
		
	}

	@Override
	public void save(ByteBuffer buffer) {
	}

	@Override
	public void load(ByteBuffer buffer) {
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

}
