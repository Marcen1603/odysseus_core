package de.uniol.inf.is.odysseus.scars.operator.jdvesink.physicaloperator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.NIOServer;

public class JDVESink<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer & ITimeInterval>
extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private NIOServer server;
	private int port;
	
	public JDVESink(int port) {
		this.port = port;
	}
	
	public JDVESink(JDVESink<M> sink) {
		this.port = sink.port;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.server = new NIOServer(this.port);
	}

	@Override
	protected void process_close() {
		super.process_close();
		this.server.close();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new JDVESink<M>(this);
	}

}
