package de.uniol.inf.is.odysseus.dsp;

import java.util.Collection;
import java.util.Collections;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

public class RelationalConstAggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends IStreamObject> getDatamodel() {
		return Tuple.class;
	}

	@Override
	public Collection<String> getFunctionNames() {
		return Collections.singleton("CONST");
	}

	@Override
	public IAggregateFunction<?, ?> createAggFunction(AggregateFunction e2, SDFSchema e1, int[] posArray,
			boolean partialAggregateInput, String datatype) {
		if (e2.getName().equalsIgnoreCase("CONST")) {
			return new RelationalConst(partialAggregateInput);
		}
		throw new IllegalArgumentException();
	}

}
