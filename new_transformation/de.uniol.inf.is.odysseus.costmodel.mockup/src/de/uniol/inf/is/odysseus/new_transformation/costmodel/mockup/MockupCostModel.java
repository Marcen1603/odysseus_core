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
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.costcalcuation.AccessAOCostCalculator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup.costcalcuation.JoinAOCostCalculator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.AtomicDataInputStreamAccessPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.ByteBufferRecieverPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.FixedSetPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.relational.transformators.InputStreamAccessPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.JoinTIPOTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.AccessAOViewTransformator;
import de.uniol.inf.is.odysseus.transformation.greedy.transformators.accessAO.ExistingAccessAO;

public class MockupCostModel implements ICostModel {
	private static Logger logger = LoggerFactory.getLogger(MockupCostModel.class);
	private final Map<Class<? extends IPOTransformator<?>>, ICostCalculator> costCalculators = new HashMap<Class<? extends IPOTransformator<?>>, ICostCalculator>();

	public MockupCostModel() {
		ICostCalculator calculator = new AccessAOCostCalculator();
		costCalculators.put(AccessAOViewTransformator.class, calculator);
		costCalculators.put(AtomicDataInputStreamAccessPOTransformator.class, calculator);
		costCalculators.put(ByteBufferRecieverPOTransformator.class, calculator);
		costCalculators.put(ExistingAccessAO.class, calculator);
		costCalculators.put(FixedSetPOTransformator.class, calculator);
		costCalculators.put(InputStreamAccessPOTransformator.class, calculator);

		costCalculators.put(JoinTIPOTransformator.class, new JoinAOCostCalculator());
	}

	@Override
	public ICost calculateOperatorCost(IPOTransformator<? extends ILogicalOperator> transformator,
			List<StreamCharacteristicCollection> incomingStreamMetadata) throws TransformationException {
		if (transformator == null) {
			return new MockupCost(0);
		}

		ICostCalculator costCalculator = costCalculators.get(transformator.getClass());
		if (costCalculator == null) {
			return new MockupCost(0);
			// throw new TransformationException();
		}

		return costCalculator.calculateCost(incomingStreamMetadata);
	}

	@Override
	public StreamCharacteristicCollection mergeStreamMetadata(
			IPOTransformator<? extends ILogicalOperator> transformator, ILogicalOperator logicalOperator,
			List<StreamCharacteristicCollection> incomingStreamMetadata) throws TransformationException {
		ICostCalculator costCalculator = costCalculators.get(transformator.getClass());

		if (costCalculator == null) {
			StreamCharacteristicCollection collection = incomingStreamMetadata.get(0);
			logger.info(collection.toString());
			return collection;
		}

		StreamCharacteristicCollection mergedStreamMetadata = costCalculator.mergeStreamMetadata(
				incomingStreamMetadata, logicalOperator);
		logger.info(mergedStreamMetadata.toString());
		return mergedStreamMetadata;
	}

	// @Override
	// public StreamCharacteristicCollection mergeStreamMetadata(
	// IPOTransformator<? extends ILogicalOperator> transformator,
	// List<StreamCharacteristicCollection> incomingStreamMetadata) throws
	// TransformationException {
	// ICostCalculator costCalculator =
	// costCalculators.get(transformator.getClass());
	//		
	// if (costCalculator == null) {
	// StreamCharacteristicCollection collection =
	// incomingStreamMetadata.get(0);
	// logger.info(collection.toString());
	// return collection;
	// }
	//
	// StreamCharacteristicCollection mergedStreamMetadata =
	// costCalculator.mergeStreamMetadata(incomingStreamMetadata);
	// logger.info(mergedStreamMetadata.toString());
	// return mergedStreamMetadata;
	// }
}
