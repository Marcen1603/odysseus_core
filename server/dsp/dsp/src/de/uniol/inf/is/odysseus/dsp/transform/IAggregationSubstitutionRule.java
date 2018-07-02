package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.List;

import com.google.common.collect.ImmutableMap.Builder;

import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public interface IAggregationSubstitutionRule<T> {
	IAggregationFunctionFactory getAggregationFunctionFactory();

	List<String> getInputAttributes(T operator);

	List<String> getOutputAttributes(T operator);

	void setAdditionalParameters(T operator, Builder<String, Object> parameterMapBuilder);

	AbstractWindowAO getWindowAO(T operator);

	void replaceAO(T originalAO, AbstractWindowAO windowAO, AggregationAO aggregationAO,
			TransformationConfiguration config);

	void setAdditionalAggregationSettings(AggregationAO aggregationAO);
}