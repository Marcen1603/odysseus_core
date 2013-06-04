package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGeneratorPreProcessor;

public class RemoveTimestampAOPreProcess implements IPQLGeneratorPreProcessor {

	@Override
	public void preprocess(ILogicalOperator logicalPlan) {
		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperators(logicalPlan, operators);

		for (ILogicalOperator operator : operators) {
			if( operator instanceof TimestampAO ) {
				TimestampAO timestampAO = (TimestampAO)operator;
				ILogicalOperator prevOperator = determinePreviousOperator(timestampAO);
				
				if (prevOperator instanceof AccessAO) {
					AccessAO accessAO = (AccessAO) prevOperator;
					SDFSchema outputSchema = accessAO.getOutputSchema();
	
					if (!isTimestampAONeeded(timestampAO, outputSchema)) {
	
						timestampAO.unsubscribeFromAllSources();
						List<ILogicalOperator> nextOperators = determineNextOperators(timestampAO);
						for (LogicalSubscription sub : timestampAO.getSubscriptions()) {
							timestampAO.unsubscribeSink(sub);
						}
	
						int portCount = 0;
						for (ILogicalOperator nextOperator : nextOperators) {
							accessAO.subscribeSink(nextOperator, 0, portCount++, accessAO.getOutputSchema());
						}
					}
				}
			}
		}
	}

	private static List<ILogicalOperator> determineNextOperators(TimestampAO timestampAO) {
		List<ILogicalOperator> nextOperators = Lists.newArrayList();
		
		for( LogicalSubscription sub : timestampAO.getSubscriptions() ) {
			nextOperators.add(sub.getTarget());
		}
		
		return nextOperators;
	}

	private static boolean isTimestampAONeeded(TimestampAO timestampAO, SDFSchema outputSchema) {
		for( SDFAttribute attribute : outputSchema ) {
			if( attribute.getDatatype().equals(SDFDatatype.START_TIMESTAMP) ) {
				if( !attribute.getAttributeName().equals(timestampAO.getStartTimestamp().getAttributeName())) {
					return true;
				}
			} else if( attribute.getDatatype().equals(SDFDatatype.END_TIMESTAMP)) {
				if( !attribute.getAttributeName().equals(timestampAO.getEndTimestamp().getAttributeName())) {
					return true;
				}
			}
		}
		return false;
	}

	private static ILogicalOperator determinePreviousOperator(TimestampAO timestampAO) {
		Collection<LogicalSubscription> subscribedToSource = timestampAO.getSubscribedToSource();
		LogicalSubscription next = subscribedToSource.iterator().next();
		return next.getTarget();
	}
	
	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator) && !(currentOperator instanceof TopAO)) {
			
			list.add(currentOperator);

			for (LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperators(subscription.getTarget(), list);
			}

			for (LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperators(subscription.getTarget(), list);
			}
		}
	}
}
