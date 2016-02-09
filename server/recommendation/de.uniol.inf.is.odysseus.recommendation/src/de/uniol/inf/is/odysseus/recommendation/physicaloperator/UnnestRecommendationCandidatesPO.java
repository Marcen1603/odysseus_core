package de.uniol.inf.is.odysseus.recommendation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.TuplePunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class UnnestRecommendationCandidatesPO<M extends ITimeInterval, U, I> extends AbstractPipe<Tuple<M>, Tuple<M>>{
	private int attributePos;
	private SDFSchema inputSchema;

	public UnnestRecommendationCandidatesPO(int attributePos, SDFSchema inputSchema){
		this.attributePos = attributePos;
		this.inputSchema = inputSchema;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<M> tuple, int port) {
		List<M> list = tuple.getAttribute(this.attributePos);
        final int depth = list.size();
        for (int d = 0; d < depth; d++) {
            final Tuple<M> outputTuple = new Tuple<M>(this.getOutputSchema().size(), tuple.requiresDeepClone());
            outputTuple.setMetadata((M) tuple.getMetadata().clone());
            for (int i = 0; i < this.inputSchema.size(); i++) {
                if (i == this.attributePos) {
                    final List<?> nestedTuple = (List<?>) tuple.getAttribute(this.attributePos);
                    outputTuple.setAttribute(i, nestedTuple.get(d));
                }
                else {
                    outputTuple.setAttribute(i, tuple.getAttribute(i));
                }
            }
            this.transfer(outputTuple);
        }

		final int[] restr = new int[tuple.getAttributes().length - 1];
		for(int i = 0,j = 0; i<tuple.getAttributes().length;++i){
			if(i != attributePos){
				restr[j] = i;
				++j;
			}
		}
		sendPunctuation(new TuplePunctuation<Tuple<M>, M>(tuple.getMetadata().getStart().plus(1), tuple.restrict(restr, true)));

	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
}
