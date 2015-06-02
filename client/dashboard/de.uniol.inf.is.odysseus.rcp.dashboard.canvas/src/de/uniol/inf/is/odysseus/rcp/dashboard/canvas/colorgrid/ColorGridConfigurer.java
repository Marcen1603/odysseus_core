package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorgrid;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class ColorGridConfigurer extends
		AbstractDashboardPartConfigurer<ColorGridDashboadPart> {

	ColorGridDashboadPart dashboardPart;
	private SDFSchema[] schemas;

	@Override
	public void init(ColorGridDashboadPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
		if (roots.size() == 0) {
			throw new IllegalArgumentException(
					"Insifficient physical operators " + roots.size());
		}
		this.dashboardPart = dashboardPartToConfigure;
		this.schemas = new SDFSchema[roots.size()];
		final Iterator<IPhysicalOperator> iter = roots.iterator();
		for (int i = 0; iter.hasNext(); i++) {
			final IPhysicalOperator sink = iter.next();
			this.schemas[i] = sink.getOutputSchema();
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
