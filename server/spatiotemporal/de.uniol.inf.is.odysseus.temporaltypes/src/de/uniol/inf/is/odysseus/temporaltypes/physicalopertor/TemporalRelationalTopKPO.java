package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalTopKPO;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.TopKDataStructure;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;

public class TemporalRelationalTopKPO<T extends Tuple<M>, M extends ITimeInterval> extends RelationalTopKPO<T, M> {

	public TemporalRelationalTopKPO(SDFSchema inputSchema, SDFSchema outputSchema,
			TemporalRelationalExpression<IValidTimes> setupFunction, SDFExpression preScoringFunction,
			SDFExpression scoringFunction, SDFExpression tearDownFunction, SDFExpression cleanupPredicate, int k,
			boolean descending, boolean suppressDuplicates, List<SDFAttribute> uniqueAttributes,
			IGroupProcessor<T, T> groupProcessor, boolean triggerOnlyByPunctuation, boolean addScore) {
		
		super(inputSchema, outputSchema, setupFunction.getExpression(), preScoringFunction, scoringFunction,
				tearDownFunction, cleanupPredicate, k, descending, suppressDuplicates, uniqueAttributes, groupProcessor,
				triggerOnlyByPunctuation, addScore);
	}

	@Override
	protected void updateTopKList(T object, TopKDataStructure<T, M> topK) {

		M metadata = object.getMetadata();
		if (!(metadata instanceof IValidTimes)) {
			return;
		}

		IValidTimes validTimes = (IValidTimes) metadata;

		SerializablePair<Double, T> scoredObject = calcScore(object);

		if (scoredObject.getE1() != null) {
			// add object to list
			topK.insertSorted(scoredObject);
		}
	}

	protected SerializablePair<Double, T> calcScore(T object) {

		if (preScoreExpression != null) {
			preScoreExpression.evaluate(object, getSessions(), null);
		}

		SerializablePair<Double, T> scoredObject = new SerializablePair<>();
		Double score = null;
		scoredObject.setE2(object);

		// TODO: Fill history
		LinkedList<Tuple<M>> history = null;

		try {
			score = ((Number) this.scoreExpression.evaluate(object, getSessions(), history)).doubleValue();

		} catch (Exception e) {
			if (!(e instanceof NullPointerException)) {
				sendWarning("Cannot calc result for " + object + " with expression " + scoreExpression, e);
			}
			e.printStackTrace();
		}

		if (score == null) {
			sendWarning("Cannot calc result for " + object + " with expression " + scoreExpression);
		}

		scoredObject.setE1(score);

		return scoredObject;
	}

}
