package de.uniol.inf.is.odysseus.p2p.splitting.sourcesplit;

import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;

public class SourceSplitExecutionHandler<F extends AbstractSplittingStrategy> extends AbstractSplittingExecutionHandler<F> {
	
	
	public SourceSplitExecutionHandler(
			SourceSplitExecutionHandler<F> sourceSplitExecutionHandler) {
		super(sourceSplitExecutionHandler);
	}

	public SourceSplitExecutionHandler() {
		super();
	}

	@Override
	public SourceSplitExecutionHandler<F> clone()  {
		return new SourceSplitExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "SourceSplitExecutionHandler";
	}	

}
