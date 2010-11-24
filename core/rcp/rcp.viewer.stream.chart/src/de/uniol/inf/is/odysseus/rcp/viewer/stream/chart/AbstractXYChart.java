package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWTException;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractXYChart extends AbstractChart {

	private XYSeriesCollection dataset = new XYSeriesCollection();
	private XYSeries serie = new XYSeries("defaultkey");
	private int choosenXValue;
	private int choosenYValue;

	@Override
	public String isValidSelection(SDFAttributeList selectAttributes) {
		if (selectAttributes.size() == 2) {
			return null;
		}
		return "The chart needs two selected attributes";
	}

	@Override
	protected void init() {
		choosenXValue = getSchema().indexOf(getAllowedSchema().get(0));
		choosenYValue = getSchema().indexOf(getAllowedSchema().get(1));
		dataset.addSeries(serie);
	}

	@Override
	public void chartSettingsChanged() {	
	}

	@Override
	protected void processElement(final RelationalTuple<? extends ITimeInterval> tuple, int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					serie.add(Double.parseDouble(tuple.getAttribute(choosenXValue).toString()), Double.parseDouble(tuple.getAttribute(choosenYValue).toString()));
				}catch (SWTException e) {				
					dispose();
					return;
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		});

	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	protected abstract JFreeChart createChart();

	@Override
	public abstract String getViewID();

	@ChartSetting(name = "Value for X-Axis", type = Type.OPTIONS)
	public List<String> getXValues() {
		return getValues();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.GET)
	public String getXValue() {
		return getSchema().get(this.choosenXValue).getQualName();
	}

	@ChartSetting(name = "Value for X-Axis", type = Type.SET)
	public void setXValue(String value) {
		for (int i = 0; i < getSchema().size(); i++) {
			if (getSchema().get(i).getQualName().equals(value)) {
				this.choosenXValue = i;
				return;
			}
		}
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.OPTIONS)
	public List<String> getYValues() {
		return getValues();
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.GET)
	public String getYValue() {
		return getSchema().get(this.choosenYValue).getQualName();
	}

	@ChartSetting(name = "Value for Y-Axis", type = Type.SET)
	public void setYValue(String value) {
		for (int i = 0; i < getSchema().size(); i++) {
			if (getSchema().get(i).getQualName().equals(value)) {
				this.choosenYValue = i;
				return;
			}
		}
	}

	private List<String> getValues() {
		List<String> values = new ArrayList<String>();
		for (SDFAttribute a : getAllowedSchema()) {
			values.add(a.getQualName());
		}
		return values;
	}

	public XYDataset getDataset() {
		return this.dataset;
	}

}
