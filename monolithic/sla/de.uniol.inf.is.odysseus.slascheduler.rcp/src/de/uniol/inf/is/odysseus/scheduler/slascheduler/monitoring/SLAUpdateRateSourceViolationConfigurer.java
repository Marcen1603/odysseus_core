package de.uniol.inf.is.odysseus.scheduler.slascheduler.monitoring;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class SLAUpdateRateSourceViolationConfigurer extends AbstractDashboardPartConfigurer<SLAUpdateRateSourceViolationDashboardPart> {

	@Override
	public void init(SLAUpdateRateSourceViolationDashboardPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {}

	@Override
	public void createPartControl(Composite parent) {}

	@Override
	public void dispose() {}
}