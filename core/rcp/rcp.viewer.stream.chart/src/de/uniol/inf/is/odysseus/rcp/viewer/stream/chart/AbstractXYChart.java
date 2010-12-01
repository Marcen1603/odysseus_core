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
	private int choosenXValue;
	private int choosenYValue;
	private int choosenSerie = -1;

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

					String key = "-";
					XYSeries currentserie;
					if (choosenSerie == -1) {
						key = "-";
					} else {
						key = tuple.getAttribute(choosenSerie).toString();
					}
					
					
					if (!containsSeriesWithKey(key)) {
						currentserie = new XYSeries(key);
						dataset.addSeries(currentserie);
					}else{
						currentserie = dataset.getSeries(key);
					}

					currentserie.add(Double.parseDouble(tuple.getAttribute(choosenXValue).toString()), Double.parseDouble(tuple.getAttribute(choosenYValue).toString()));

				} catch (SWTException e) {
					dispose();
					return;
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		});

	}

	
	private boolean containsSeriesWithKey(Comparable<?> key){
		for(Object o : dataset.getSeries()){
			XYSeries s = (XYSeries)o;
			if(s.getKey().equals(key)){
				return true;
			}
		}
		return false;
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

	@ChartSetting(name = "Value for Series", type = Type.OPTIONS)
	public List<String> getSeriesValues() {
		List<String> values = getValues();
		values.add(0, "");
		return values;
	}

	@ChartSetting(name = "Value for Series", type = Type.GET)
	public String getSeriesValue() {
		if (this.choosenSerie > -1) {
			return getSchema().get(this.choosenSerie).getQualName();
		} else {
			return "";
		}
	}

	@ChartSetting(name = "Value for Series", type = Type.SET)
	public void setSeriesValue(String value) {
		if (value.equals("")) {
			this.choosenSerie = -1;
		} else {
			for (int i = 0; i < getSchema().size(); i++) {
				if (getSchema().get(i).getQualName().equals(value)) {
					this.choosenSerie = i;
					return;
				}
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
