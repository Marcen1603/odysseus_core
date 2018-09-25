package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator calculates the top k elements from the input with a scoring
 * function
 * 
 * @author Marco Grawunder
 * 
 * @param <T>
 * @param <M>
 */
public class RelationalTopKPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractRelationalTopKPO<T, M> {

	public RelationalTopKPO(SDFSchema inputSchema, SDFSchema outputSchema, SDFExpression setupFunction,
			SDFExpression preScoringFunction, SDFExpression scoringFunction, SDFExpression tearDownFunction,
			SDFExpression cleanupPredicate, int k, boolean descending, boolean suppressDuplicates,
			List<SDFAttribute> uniqueAttributes, IGroupProcessor<T, T> groupProcessor,
			boolean triggerOnlyByPunctuation, boolean addScore) {
		super(inputSchema, outputSchema, setupFunction, preScoringFunction, scoringFunction, tearDownFunction,
				cleanupPredicate, k, descending, suppressDuplicates, uniqueAttributes, groupProcessor,
				triggerOnlyByPunctuation, addScore);
	}

	@Override
	protected void updateTopKList(T object, TopKDataStructure<T, M> topK) {
		SerializablePair<Double, T> scoredObject = calcScore(object);

		if (scoredObject.getE1() != null) {
			// add object to list
			topK.insertSorted(scoredObject);
		}
	}

}
