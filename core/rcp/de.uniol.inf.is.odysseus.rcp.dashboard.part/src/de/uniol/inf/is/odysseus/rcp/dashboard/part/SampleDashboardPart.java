package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;

public class SampleDashboardPart implements IDashboardPart {

	public SampleDashboardPart() {
	}

	@Override
	public boolean init(Configuration configuration) {
		return true;
	}

}
