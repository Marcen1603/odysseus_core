package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator calculates the top k elements from the input with a scoring
 * function. This operator is a special version that recalcs the scores for every new input 
 * 
 * @author Marco Grawunder
 * 
 * @param <T>
 * @param <M>
 */
public class RelationalDynamicScoreTopKPO<T extends Tuple<M>, M extends ITimeInterval>
		extends AbstractRelationalTopKPO<T, M> {

	public RelationalDynamicScoreTopKPO(SDFSchema inputSchema, SDFSchema outputSchema, SDFExpression setupFunction,
			SDFExpression preScoringFunction, SDFExpression scoringFunction, SDFExpression tearDownFunction,
			SDFExpression cleanupPredicate, int k, boolean descending, boolean suppressDuplicates,
			IGroupProcessor<T, T> groupProcessor, boolean triggerOnlyByPunctuation) {
		super(inputSchema, outputSchema, setupFunction, preScoringFunction, scoringFunction, tearDownFunction,
				cleanupPredicate, k, descending, suppressDuplicates, groupProcessor, triggerOnlyByPunctuation);
	}

	@Override
	protected void updateTopKList(T object, ArrayList<SerializablePair<Double, T>> topK) {
		@SuppressWarnings("unchecked")
		ArrayList<SerializablePair<Double, T>> oldTopK = (ArrayList<SerializablePair<Double, T>>) topK.clone();
		topK.clear();
		
		SerializablePair<Double, T> scoredObject = calcScore(object);
		topK.add(scoredObject);
		
		int elemsToProcess = Math.min(oldTopK.size()+1,k);
		
		// the first k-1 objects needs to be added to the list
		for (int i=0;i<elemsToProcess-1;i++){
		 	SerializablePair<Double, T> reScoredObject = calcScore(oldTopK.get(i).getE2());
		 	insertSorted(topK, reScoredObject);
		}

		double minScore = topK.get(topK.size()-1).getE1();
		
		// The other need only be added sorted if score is higher than min score
		for (int i=elemsToProcess;i<oldTopK.size();i++){
		 	SerializablePair<Double, T> reScoredObject = calcScore(oldTopK.get(i).getE2());
		 	if (reScoredObject.getE1()>minScore){
		 		minScore = reScoredObject.getE1();
		 		insertSorted(topK, reScoredObject);
		 	}else{
		 		topK.add(topK.size(),reScoredObject);
		 	}
		}
		
		
		
	}
}
