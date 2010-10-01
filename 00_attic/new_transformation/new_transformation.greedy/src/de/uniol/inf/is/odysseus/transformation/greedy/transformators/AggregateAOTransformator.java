package de.uniol.inf.is.odysseus.transformation.greedy.transformators;

import java.util.Set;

import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalTupleGroupingHelper;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;

public class AggregateAOTransformator implements IPOTransformator<AggregateAO> {
	@Override
	public boolean canExecute(AggregateAO logicalOperator, TransformationConfiguration config) {
		Set<String> metaTypes = config.getMetaTypes();
		return metaTypes.contains("de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval");
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransformedPO transform(AggregateAO aggregateAO, TransformationConfiguration config,
			ITransformation transformation) throws TransformationException {

		StreamGroupingWithAggregationPO po = new StreamGroupingWithAggregationPO(aggregateAO.getInputSchema(),
				aggregateAO.getOutputSchema(), aggregateAO.getGroupingAttributes(), aggregateAO.getAggregations());
		po.setMetadataType(MetadataRegistry.getMetadataType(config.getMetaTypes()));
		po.setOutputSchema(aggregateAO.getOutputSchema());
		// TODO relational Teile ausgliedern
		if (config.getDataType() == "relational") {
			po.setGroupingHelper(new RelationalTupleGroupingHelper(po.getInputSchema(), po.getOutputSchema(), po
					.getGroupingAttribute(), po.getAggregations()));
		}
		return new TransformedPO(po);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public TempTransformationOperator createTempOperator() {
		TempTransformationOperator to = new TempTransformationOperator("AggregateAO");
		return to;
	}
}
