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
/** Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.rcp.wizard.output.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.AreaChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.BarChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.CurveChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.PieChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.PieChart3D;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.ScatterPlotChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.ThermometerChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.XYBarChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.XYLineChart;
import de.uniol.inf.is.odysseus.rcp.wizard.AbstractWizardPage;
import de.uniol.inf.is.odysseus.rcp.wizard.output.OutputWizard;

/**
 * 
 * @author Dennis Geesen
 * Created at: 29.11.2011
 */
public class CreateChartPage extends AbstractWizardPage<OutputWizard> {

	private Button barChartButton;
	private Button pieChartButton;
	private Button lineChartButton;
	private Button thermoChartButton;
	private Button areaChartButton;
	private Button curveChartButton;
	private Button xybarChartButton;
	private Button scatterChartButton;
	private Button threeDPieChartButton;

	public CreateChartPage(String title) {	
		super(title);
		setTitle(title);
		setMessage("Please choose chart type");
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);

	    // create the desired layout for this wizard page
		GridLayout gl = new GridLayout();
		int ncol = 1;
		gl.numColumns = ncol;
		composite.setLayout(gl);
				
		barChartButton = new Button(composite, SWT.RADIO);
		barChartButton.setText("Create a bar chart");
		barChartButton.addListener(SWT.Selection, this);
		
		pieChartButton = new Button(composite, SWT.RADIO);
		pieChartButton.setText("Create a pie chart");
		pieChartButton.addListener(SWT.Selection, this);
		
		thermoChartButton = new Button(composite, SWT.RADIO);
		thermoChartButton.setText("Create a thermometer chart");
		thermoChartButton.addListener(SWT.Selection, this);
		
		areaChartButton = new Button(composite, SWT.RADIO);
		areaChartButton.setText("Create an area chart");
		areaChartButton.addListener(SWT.Selection, this);
		
		curveChartButton = new Button(composite, SWT.RADIO);
		curveChartButton.setText("Create a curve (spline) chart");
		curveChartButton.addListener(SWT.Selection, this);
		
		lineChartButton = new Button(composite, SWT.RADIO);
		lineChartButton.setText("Create a XY line chart");
		lineChartButton.addListener(SWT.Selection, this);
		
		xybarChartButton = new Button(composite, SWT.RADIO);
		xybarChartButton.setText("Create a XY Bar chart");
		xybarChartButton.addListener(SWT.Selection, this);
		
		scatterChartButton = new Button(composite, SWT.RADIO);
		scatterChartButton.setText("Create a scatter plot chart");
		scatterChartButton.addListener(SWT.Selection, this);
		
		threeDPieChartButton = new Button(composite, SWT.RADIO);
		threeDPieChartButton.setText("Create a 3D pie chart");
		threeDPieChartButton.addListener(SWT.Selection, this);

		setControl(composite);
		setPageComplete(true);

	}
	
	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

	@Override
	public boolean canFinish() {
		if(barChartButton.getSelection() || 
			pieChartButton.getSelection() ||
			thermoChartButton.getSelection() ||
			areaChartButton.getSelection() ||
			curveChartButton.getSelection() ||
			lineChartButton.getSelection() ||
			xybarChartButton.getSelection() ||
			scatterChartButton.getSelection() ||
			threeDPieChartButton.getSelection()){
			performNext();
			return true;
		}
		return false;
	}

	@Override
	public void performNext() {
		if(barChartButton.getSelection()){
			setView(new BarChart());
			return;
		}
		if(pieChartButton.getSelection()){
			setView(new PieChart());
			return;
		}
		if(thermoChartButton.getSelection()){
			setView(new ThermometerChart());
			return;
		}
		if(areaChartButton.getSelection()){
			setView(new AreaChart());
			return;
		}
		if(curveChartButton.getSelection()){
			setView(new CurveChart());
			return;
		}
		if(lineChartButton.getSelection()){
			setView(new XYLineChart());
			return;
		}
		if(xybarChartButton.getSelection()){
			setView(new XYBarChart());
			return;
		}
		if(scatterChartButton.getSelection()){
			setView(new ScatterPlotChart());
			return;
		}
		if(threeDPieChartButton.getSelection()){
			setView(new PieChart3D());
			return;
		}
	}
	
	private void setView(AbstractJFreeChart<?,?> chart){
		getWizard().getOutputModel().setViewPart(chart);
	}	

}
