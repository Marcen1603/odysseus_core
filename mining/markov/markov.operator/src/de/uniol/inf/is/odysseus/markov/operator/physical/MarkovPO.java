package de.uniol.inf.is.odysseus.markov.operator.physical;

import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.algorithm.IMarkovAlgorithm;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class MarkovPO extends AbstractPipe<RelationalTuple<ITimeInterval>,RelationalTuple<ITimeInterval>>{	
	
	private HiddenMarkovModel hmm;
	private IMarkovAlgorithm algorithm;

	public MarkovPO(HiddenMarkovModel hmm, IMarkovAlgorithm algorithm){
		this.hmm = hmm;
		this.algorithm = algorithm;
	}
	
	public MarkovPO(MarkovPO markovPO) {
		super(markovPO);
		this.hmm = markovPO.hmm;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;		
	}

	@Override
	protected void process_open() throws OpenFailedException {	
		super.process_open();
		System.out.println("Opening Markov PO");
		this.algorithm.create(this.hmm);
	}
	
	@Override
	protected void process_next(RelationalTuple<ITimeInterval> object, int port) {
		Observation o = new Observation(object.getAttribute(1).toString());
		System.out.println("input: "+o.getName());
		this.algorithm.next(o);		
	}

	@Override
	public MarkovPO clone() {
		return new MarkovPO(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {		
		
	}

}
