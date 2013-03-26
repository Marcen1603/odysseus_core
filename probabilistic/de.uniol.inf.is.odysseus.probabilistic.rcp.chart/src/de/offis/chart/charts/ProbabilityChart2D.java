/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package de.offis.chart.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.SWTException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.offis.chart.charts.datatype.ProbViewSchema;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.probabilistic.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings.ChartSetting.Type;

public class ProbabilityChart2D extends AbstractJFreeChart<Object, IMetaAttribute>  {
	
	private XYSeriesCollection lineDataset = new XYSeriesCollection();
	private XYSeriesCollection barDataset = new XYSeriesCollection();
	
	private XYBarRenderer barRenderer = new XYBarRenderer(0.2);
	private XYPlot plot;
	
	@Override
	protected JFreeChart createChart() {
		barRenderer.setBarPainter(new StandardXYBarPainter());
		barRenderer.setMargin(0.95);
		barRenderer.setShadowVisible(false);
		JFreeChart chart = ChartFactory.createXYLineChart(getTitle(), "X", "Y", lineDataset, PlotOrientation.VERTICAL, true, true, false);
		this.plot = chart.getXYPlot();
		plot.setDataset(1, barDataset);
		
		return chart;
	}

	@Override
	public String getViewID() {
		return "de.offis.chart.charts.probabilitychart2d";
	}

	@Override
	public void chartSettingsChanged() {
		lineDataset.removeAllSeries();
		barDataset.removeAllSeries();
	}

	@Override
	protected void initConnection(IStreamConnection<Object> streamConnection) {
		for (ISubscription<? extends ISource<?>> s : streamConnection
				.getSubscriptions()) {
			this.viewSchema.put(s.getSinkInPort(),
					new ProbViewSchema<Object>(s.getSchema(), s.getTarget()
							.getMetaAttributeSchema(), s.getSinkInPort()));
		}
		if (validate()) {
			streamConnection.addStreamElementListener(this);
			streamConnection.connect();
			chartSettingsChanged();
			init();
		}
	}
	
	@Override
	protected void processElement(final List<Object> tuple, IMetaAttribute metadata, final int port) {		
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < getChoosenAttributes(port).size(); i++) {
						String name = getChoosenAttributes(port).get(i).getName();
						SDFDatatype type = getChoosenAttributes(port).get(i).getSDFDatatype();
						
						if(tuple.size() == 0 ){
							// no data
							return;
						}
						
						if(type.getClass().equals(SDFProbabilisticDatatype.class)){
							
							SDFProbabilisticDatatype probType = (SDFProbabilisticDatatype)type;
							
							if(probType.isContinuous()){
								updateChart((NormalDistributionMixture)tuple.get(i), name);
							} else if (probType.isDiscrete()){
								updateChart((AbstractProbabilisticValue<?>)tuple.get(i), name);
							}
							
						}						
					}
				} catch (SWTException e) {
					dispose();
					return;
				}
			}
		});
	}
	
	private void updateChart(AbstractProbabilisticValue<?> value, String seriesKey){
	
		try {
			barDataset.removeSeries(barDataset.getSeries(seriesKey));			
		} catch (org.jfree.data.UnknownKeyException e) {
			// ignore
		}
		final XYSeries series = new XYSeries(seriesKey);
		
		for(Entry<?, Double> e : value.getValues().entrySet()){
			series.add(((Number)e.getKey()).doubleValue(), e.getValue());
		}
		
		barDataset.addSeries(series);
		
		plot.setRenderer(plot.indexOf(barDataset), barRenderer);
	}
	
	private void updateChart(NormalDistributionMixture mix, String seriesKey){
		if(mix.getDimension() < 1)
			return; // keine 2D Normaldistribution
		
		
		final HashMap<NormalDistributionFunction2D, Double> funcs = new HashMap<>();
		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
			double means = e.getKey().getMean()[0];
			double m = e.getKey().getCovarianceMatrix().getEntries()[0];
			
			funcs.put(new NormalDistributionFunction2D(means, m), e.getValue());
		}
		
		try {
			lineDataset.removeSeries(lineDataset.getSeries(seriesKey));			
		} catch (org.jfree.data.UnknownKeyException e) {
			// ignore
		}
		
		final Interval[] interval = mix.getSupport();
		final double scale = mix.getScale();
		
		Function2D f = new Function2D() {
			
			@Override
			public double getValue(double x) {
				if(x < interval[0].inf() || x > interval[0].sup())
					return 0.0;
				
				double sum = 0;
				for(Entry<NormalDistributionFunction2D, Double> func : funcs.entrySet()){
					sum += func.getKey().getValue(x) * func.getValue();
				}
				return sum * scale;
			}
		};
		lineDataset.addSeries(DatasetUtilities.sampleFunction2DToSeries(f, -10, 10, 100, seriesKey));
//		plot.setRenderer(lineDataset.indexOf(seriesKey),lineRenderer);
	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

	@ChartSetting(name = "BarMargin", type = Type.GET)
	public String getBarMarginValue() {
		return barRenderer.getMargin()+"";
	}

	@ChartSetting(name = "BarMargin", type = Type.SET)
	public void setBarMarginValue(String value) {
		barRenderer.setMargin(Double.parseDouble(value));
	}
}
