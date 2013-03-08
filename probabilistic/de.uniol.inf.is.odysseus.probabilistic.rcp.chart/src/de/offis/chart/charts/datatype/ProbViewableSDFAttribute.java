package de.offis.chart.charts.datatype;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.ViewableSDFAttribute;

public class ProbViewableSDFAttribute extends ViewableSDFAttribute {

	public ProbViewableSDFAttribute(SDFAttribute attribute, String typeName,
			int index, int port) {
		super(attribute, typeName, index, port);
	}
	
	@Override
	public Object evaluate(Tuple<? extends IMetaAttribute> tuple) {
		ProbabilisticTuple<?> obj = (ProbabilisticTuple<?>)tuple;
		ProbabilisticContinuousDouble attr = (ProbabilisticContinuousDouble)obj.getAttribute(this.index);
		return obj.getDistribution(attr.getDistribution());
	}
}
