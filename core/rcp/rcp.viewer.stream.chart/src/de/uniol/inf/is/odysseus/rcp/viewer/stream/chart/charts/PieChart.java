package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts;

import java.util.List;

import org.eclipse.swt.SWTException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public class PieChart extends AbstractChart<Double, IMetaAttribute> {

	DefaultPieDataset dataset = new DefaultPieDataset();

	@Override
	protected JFreeChart createChart() {

		JFreeChart chart = ChartFactory.createPieChart(getTitle(), dataset, true, true, false);
		return chart;
	}

	@Override
	public String getViewID() {
		return VIEW_ID_PREFIX + ".piechart";
	}

	@Override
	public void chartSettingsChanged() {

	}

	@Override
	protected void processElement(final List<Double> tuple, IMetaAttribute metadata, int port) {		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {					
					for(int i=0;i<getChoosenAttributes().size();i++){
						double value = tuple.get(i);						
						dataset.setValue(getChoosenAttributes().get(i).getName(), value);																
					}										
				} catch (SWTException e) {								
					dispose();
					return;
				}
			}
		});

	}

	@Override
	protected void decorateChart(JFreeChart thechart) {
		PiePlot plot = (PiePlot) thechart.getPlot();
		plot.setBackgroundPaint(DEFAULT_BACKGROUND);
		plot.setLabelGenerator(null);

	}

	@Override
	public String isValidSelection(List<IViewableAttribute<Double>> selectAttributes) {
		if(selectAttributes.size()>0){
			return null;
		}
		return "The number of choosen attributes should be at least one!";
	}


}
