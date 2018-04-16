package de.uniol.inf.is.odysseus.dsp;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AbstractAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

public class DigitalSignalProcessingAggregateFunctionBuilder extends AbstractAggregateFunctionBuilder {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends IStreamObject> getDatamodel() {
		return Tuple.class;
	}

	@Override
	public SDFDatatype getDatatype(String functionName, List<SDFAttribute> input) {
		if (functionName.equalsIgnoreCase("FFT")) {
			return SDFDatatype.LIST_DOUBLE;
		}
		return super.getDatatype(functionName, input);
	}

	@Override
	public Collection<String> getFunctionNames() {
		return Collections.singleton("FFT");
	}

	@Override
	public IAggregateFunction<?, ?> createAggFunction(AggregateFunction e2, SDFSchema e1, int[] posArray,
			boolean partialAggregateInput, String datatype) {
		if (e2.getName().equalsIgnoreCase("FFT")) {
			return new RelationalFFT(posArray[0], partialAggregateInput);
		}
		throw new IllegalArgumentException();
	}

}
