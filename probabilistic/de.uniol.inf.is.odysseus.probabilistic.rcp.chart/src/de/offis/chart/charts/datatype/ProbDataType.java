package de.offis.chart.charts.datatype;

import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.AbstractViewableDatatype;

public class ProbDataType extends AbstractViewableDatatype<NormalDistributionMixture> {

	public ProbDataType() {		
		super.addProvidedSDFDatatype(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE);
		super.addProvidedClass(ProbabilisticContinuousDouble.class);
	}
	
	@Override
	public NormalDistributionMixture convertToValue(Object value) {
		return (NormalDistributionMixture)value;
	}

}
