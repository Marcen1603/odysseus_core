package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;

public class TestDashboardPart implements IDashboardPart {

	@Override
	public boolean init(Configuration configuration) {
		return true;
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	@Override
	public Configuration getConfiguration() {
		return null;
	}

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		
	}

	@Override
	public void setQueryFile(IFile file) {
	}

	@Override
	public IFile getQueryFile() {
		return null;
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onUnpause() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public Map<String, String> onSave() {
		return null;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		
	}

}

