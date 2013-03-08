package de.offis.chart.charts;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;

import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;

public class D2ProbabilityMapper implements net.ericaro.surfaceplotter.Mapper {
	final NormalDistributionFunction3D n3d1 = new NormalDistributionFunction3D(
			2, 2, 0, 0);
	final NormalDistributionFunction3D n3d2 = new NormalDistributionFunction3D(
			5, 5, 5, 5);

	ConcurrentHashMap<NormalDistributionFunction2D, Double> funcs = new ConcurrentHashMap<>();
	
	public void setup(NormalDistributionMixture mix){
		funcs.clear();
		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
			double means = e.getKey().getMean()[0];
			double m = e.getKey().getCovarianceMatrix().getEntries()[0];
			
			funcs.put(new NormalDistributionFunction2D(means, m), e.getValue());
		}
		
		
	}
	
	
	@Override
	public float f1(float x, float y) {
		double sum = 0;
		for(Entry<NormalDistributionFunction2D, Double> func : funcs.entrySet()){
			sum += func.getKey().getValue(x) * func.getValue();
		}
		return (float) sum;
	}

	@Override
	public float f2(float x, float y) {
		// double sum = 0;
		// for(Entry<NormalDistributionFunction2D, Double> func :
		// funcs.entrySet()){
		// sum += func.getKey().getValue(x) * func.getValue();
		// }
		// return (float) sum;
		return (float) (n3d1.getValue(x, y) + n3d2.getValue(x, y));
	}

}
