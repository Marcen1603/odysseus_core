package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class StreamChartComposite extends ChartComposite {

	public StreamChartComposite(Composite comp, int style, JFreeChart chart) {
		super(comp, style, chart, true);	
	}
	
	@Override
	protected Menu createPopupMenu(boolean arg0, boolean arg1, boolean arg2, boolean arg3) {	
		return null;
	}

}
