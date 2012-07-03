package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.nio.ByteBuffer;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;

public class SampleDashboardPart implements IDashboardPart {

	public SampleDashboardPart() {
	}

	@Override
	public boolean init(Configuration configuration) {
		return true;
	}

	@Override
	public Configuration getConfiguration() {
		return null;
	}

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

	@Override
	public void setQueryFile(IFile file) {
	}

	@Override
	public IFile getQueryFile() {
		return null;
	}

}
