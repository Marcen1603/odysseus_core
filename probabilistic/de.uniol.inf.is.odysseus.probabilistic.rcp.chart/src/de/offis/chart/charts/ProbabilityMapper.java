package de.offis.chart.charts;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

public class ProbabilityMapper implements net.ericaro.surfaceplotter.Mapper {

	private ConcurrentHashMap<NormalDistributionFunctionND, Double> funcs = new ConcurrentHashMap<>();
	
	private double scale = 1.0;
	private Interval[] interval;
	
	public void setup(NormalDistributionMixture mix){
		funcs.clear();
		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
			double[] means = e.getKey().getMean();
			CovarianceMatrix m = e.getKey().getCovarianceMatrix();			
			funcs.put(new NormalDistributionFunctionND(means, m), e.getValue());
		}
		
		this.scale = mix.getScale();
//		this.scale = 5;
		this.interval = mix.getSupport();
//		this.interval = new Interval[]{new Interval(-1, 1),new Interval(-1, 1)};
	}

	@Override
	public float f1(float x, float y) {
		if (funcs == null || interval == null || interval.length < 2)
			return 0f;
		
		if(x < interval[0].inf() || x > interval[0].sup())
			return 0f;
		
		if(y < interval[1].inf() || y > interval[1].sup())
			return 0f;
		
		double result = 0;
		
		for(Entry<NormalDistributionFunctionND, Double> e : funcs.entrySet()){
			result += e.getValue() * e.getKey().getValue(new double[]{x,y});
		}

		return (float) (result * scale);
	}

	@Override
	public float f2(float x, float y) {
		return 0;
	}
}
