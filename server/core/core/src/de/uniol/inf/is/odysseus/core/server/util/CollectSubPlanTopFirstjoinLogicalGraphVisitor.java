package de.uniol.inf.is.odysseus.core.server.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

public class CollectSubPlanTopFirstjoinLogicalGraphVisitor<T extends ILogicalOperator> implements
		IGraphNodeVisitor<T, Pair<T, List<T>>> {

	private boolean finished = false;
	
	private List<T> operators;
	
	private Pair<T, List<T>> pair;
	
	public CollectSubPlanTopFirstjoinLogicalGraphVisitor() {
		this.operators = new ArrayList<T>();
	}
	
	@Override
	public void nodeAction(T node) {
		if(finished) {
			return;
		}
		// everything that is not a join will be added to the list
		if(!(node instanceof JoinAO)) {
			this.operators.add(node);
		} else {
			finished = true;
			this.pair = new Pair<T, List<T>>(node, this.operators);
		}
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Pair<T, List<T>> getResult() {
		return this.pair;
	}

}
