package de.uniol.inf.is.odysseus.peer.distribute.postprocessor.localsink;


import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.peer.distribute.postprocess.AbstractOperatorInsertionPostProcessor;

public class LocalSinkPostProcessor extends AbstractOperatorInsertionPostProcessor {
	
	@Override
	public String getName() {
		return "localsink";
	}
	
	@Override
	protected Collection<ILogicalOperator> insertOperator(ILogicalOperator relativeSink) {
		
		Collection<ILogicalOperator> operators = Lists.newArrayList(relativeSink);
		return operators;
		
	}
	
	@Override
	protected ILogicalOperator createOperator() {
		
		RenameAO renameAO = new RenameAO(); 
		renameAO.setNoOp(true);
		return renameAO;
		
	}

}
