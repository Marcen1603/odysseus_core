package de.offis.chart.charts;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.uniol.inf.is.odysseus.probabilistic.datatype.CovarianceMatrix;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;

public class ProbabilityMapper implements net.ericaro.surfaceplotter.Mapper {

	private ConcurrentHashMap<NormalDistributionFunctionND, Double> funcs = new ConcurrentHashMap<>();
	
	public void setup(NormalDistributionMixture mix){
		funcs.clear();
		for(Entry<NormalDistribution, Double> e : mix.getMixtures().entrySet()){
			double[] means = e.getKey().getMean();
			CovarianceMatrix m = e.getKey().getCovarianceMatrix();
			
			funcs.put(new NormalDistributionFunctionND(means, m), e.getValue());
		}		
	}

	@Override
	public float f1(float x, float y) {
		if (funcs == null)
			return 0f;
		
		double result = 0;
		
		for(Entry<NormalDistributionFunctionND, Double> e : funcs.entrySet()){
			result += e.getValue() * e.getKey().getValue(new double[]{x,y});
		}

		return (float) result;
	}

	@Override
	public float f2(float x, float y) {
		return 0;
	}
}
