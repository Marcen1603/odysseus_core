package de.uniol.inf.is.odysseus.rcp.dashboard.test;

import java.nio.ByteBuffer;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
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
	public void createPartControl(Composite parent) {
		
	}

	@Override
	public void save(ByteBuffer buffer) {
	}

	@Override
	public void load(ByteBuffer buffer) {
	}

	@Override
	public void setQuery(List<String> queryLines) {
	}

	@Override
	public ImmutableList<String> getQuery() {
		return null;
	}

}

