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
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYSeriesCollection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.AbstractJFreeChart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public class ProbabilityChart2D extends AbstractJFreeChart<ProbabilisticTuple<IMetaAttribute>, IMetaAttribute>  {
	private XYSeriesCollection dataset = new XYSeriesCollection();
	
	
	@Override
	protected JFreeChart createChart() {		
		return ChartFactory.createXYLineChart(getTitle(), "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);
	}

	@Override
	public String getViewID() {
		return "de.offis.chart.charts.probabilitychart2d";
	}

	@Override
	public void chartSettingsChanged() {


	
	}

	@Override
	public void streamElementRecieved(final Object element, int port) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					
						ProbabilisticTuple<IMetaAttribute> value = (ProbabilisticTuple<IMetaAttribute>)element;
						Object o = value.getAttribute(0);
						updateChart(value.getDistribution(((ProbabilisticContinuousDouble) o).getDistribution()));
													
				} catch (SWTException e) {								
					dispose();
					return;
				}
			}
		});
	}
	
	@Override
	protected void processElement(final List<ProbabilisticTuple<IMetaAttribute>> tuple, IMetaAttribute metadata, final int port) {		
		

	}
	
	private void updateChart(NormalDistributionMixture mix){
		if(mix.getDimension() != 1)
			return; // keine 2D Normaldistribution
		
		
		final HashMap<NormalDistributionFunction2D, Double> funcs = new HashMap<>();
		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
			double means = e.getKey().getMean()[0];
			double m = e.getKey().getCovarianceMatrix().getEntries()[0];
			
			funcs.put(new NormalDistributionFunction2D(means, m), e.getValue());
		}
		
		dataset.removeAllSeries();
		Function2D f = new Function2D() {
			
			@Override
			public double getValue(double x) {
				double sum = 0;
				for(Entry<NormalDistributionFunction2D, Double> func : funcs.entrySet()){
					sum += func.getKey().getValue(x) * func.getValue();
				}
				return sum;
			}
		};
		dataset.addSeries(DatasetUtilities.sampleFunction2DToSeries(f, -10, 10, 100, "Normal"));
		
		
//		final HashMap<NormalDistributionFunctionND, Double> funcs = new HashMap<>();
//		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
//			double[] means = e.getKey().getMean();
//			CovarianceMatrix m = e.getKey().getCovarianceMatrix();
//			
//			funcs.put(new NormalDistributionFunctionND(means, m), e.getValue());
//		}
//		
//		dataset.removeAllSeries();
//		Function2D f = new Function2D() {
//			
//			@Override
//			public double getValue(double x) {
//				double sum = 0;
//				for(Entry<NormalDistributionFunctionND, Double> func : funcs.entrySet()){
//					sum += func.getKey().getValue(new double[]{x}) * func.getValue();
//				}
//				return sum;
//			}
//		};
//		dataset.addSeries(DatasetUtilities.sampleFunction2DToSeries(f, -10, 10, 100, "Normal"));
	}

	@Override
	protected void decorateChart(JFreeChart thechart) {

	}

	@Override
	public String isValidSelection(
			Map<Integer, Set<IViewableAttribute>> selectAttributes) {
		return checkAtLeastOneSelectedAttribute(selectAttributes);
	}

}
