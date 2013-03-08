package de.offis.chart.charts;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.offis.chart.charts.datatype.ProbDataType;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;

public class Activator extends AbstractUIPlugin {
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		ViewableDatatypeRegistry.getInstance().register(new ProbDataType());
	}
}
