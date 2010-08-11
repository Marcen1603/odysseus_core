package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.metadata.base.MetadataRegistry;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TStreamGroupingWithAggregationTIPORule extends AbstractTransformationRule<AggregateAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(AggregateAO aggregateAO, TransformationConfiguration transformConfig) {
		StreamGroupingWithAggregationPO po = new StreamGroupingWithAggregationPO(aggregateAO.getInputSchema(), aggregateAO.getOutputSchema(), aggregateAO.getGroupingAttributes(),
				aggregateAO.getAggregations());
		po.setOutputSchema(aggregateAO.getOutputSchema()); // Notwendig??
		po.setMetadataType(MetadataRegistry.getMetadataType(transformConfig.getMetaTypes()));

		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(aggregateAO, po);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}

		insert(po);
		retract(aggregateAO);

	}

	@Override
	public boolean isExecutable(AggregateAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.toString())) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateAO -> StreamGroupingWithAggregationPO";
	}

}
