/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.ac.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

public abstract class AbstractAdmissionView extends ViewPart {

	private ChartComposite composite;

	@Override
	public final void createPartControl(Composite parent) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart(getChartTitle(), getXLabel(), getYLabel(), getTimeSeriesCollection(), true, false, false);

		chart.getXYPlot().getRangeAxis().setUpperBound(getUpperBound());
		chart.getXYPlot().getRangeAxis().setLowerBound(0);
		
		composite = new ChartComposite(parent, SWT.BORDER, chart);
	}


	@Override
	public final void setFocus() {
		composite.setFocus();
	}
	
	protected abstract TimeSeriesCollection getTimeSeriesCollection();
	protected abstract String getChartTitle();
	protected abstract String getXLabel();
	protected abstract String getYLabel();
	protected abstract double getUpperBound();
}
