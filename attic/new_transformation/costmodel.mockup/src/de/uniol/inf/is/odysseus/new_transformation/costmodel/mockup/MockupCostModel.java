package de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICostCalculator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICostModel;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.costcalcuation.AccessAOCostCalculator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.costcalcuation.JoinAOCostCalculator;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;

/**
 * This class represents a mockup implementation of the {@link ICostModel} of
 * the Transformation.
 * 
 */
public class MockupCostModel implements ICostModel {
	private static Logger logger = LoggerFactory.getLogger(MockupCostModel.class);
	private final Map<String, ICostCalculator> costCalculators = new HashMap<String, ICostCalculator>();

	public MockupCostModel() {
		ICostCalculator calculator = new AccessAOCostCalculator();
		costCalculators.put("AccessAOView", calculator);
		costCalculators.put("AtomicDataInputStreamAccessPO", calculator);
		costCalculators.put("ByteBufferReceiverPO", calculator);
		costCalculators.put("ExistingAccessAO", calculator);
		costCalculators.put("FixedSetPO", calculator);
		costCalculators.put("InputStreamAccessPO", calculator);

		costCalculators.put("JoinTIPO", new JoinAOCostCalculator());
	}

	@Override
	public StreamCharacteristicCollection mergeStreamMetadata(TempTransformationOperator transformator,
			ILogicalOperator logicalOperator, List<StreamCharacteristicCollection> incomingStreamMetadata)
			throws TransformationException {
		ICostCalculator costCalculator = costCalculators.get(transformator.getType());

		if (costCalculator == null) {
			logger.info("Costcalculator for TempTO <" + transformator.getType() + "> not found! Doing nothing");
			StreamCharacteristicCollection collection = incomingStreamMetadata.get(0);
			logger.info(collection.toString());
			return collection;
		}

		logger.info("Costcalculator for TempTO <" + transformator.getType() + "> found! Merging StreamCharacteristics");
		StreamCharacteristicCollection mergedStreamMetadata = costCalculator.mergeStreamMetadata(
				incomingStreamMetadata, logicalOperator);
		logger.info(mergedStreamMetadata.toString());
		return mergedStreamMetadata;
	}

	@Override
	public ICost calculateCost(TempTransformationOperator... operators) {
		// only "calculate" the costs for one operator - real costmodels should not have this constraint
		if (operators.length != 1) {
			return new MockupCost(0);
		}
		TempTransformationOperator operator = operators[0];
		ICostCalculator costCalculator = costCalculators.get(operator.getType());
		if (costCalculator == null) {
			return new MockupCost(0);
		}

		return costCalculator.calculateCost(operator);
	}
}
