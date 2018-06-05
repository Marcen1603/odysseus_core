package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * Graph visitor for searching for logical operators with specific id
 * 
 * @author ChrisToenjesDeye
 *
 * @param <T extends ILogicalOperator>
 */
public class OperatorIdLogicalGraphVisitor<T extends ILogicalOperator>
		implements IGraphNodeVisitor<T, Map<String, T>> {

	private Map<String, T> idsWithOperator = new HashMap<String, T>();
	private List<String> operatorIds = new ArrayList<String>();

	public OperatorIdLogicalGraphVisitor(String operatorId) {
		this.operatorIds.add(operatorId);
	}

	public OperatorIdLogicalGraphVisitor(List<String> operatorIds) {
		this.operatorIds.addAll(operatorIds);
	}

	@Override
	public void nodeAction(T node) {
		if (node.getUniqueIdentifier() != null){
			for (String id : operatorIds) {
				if (id.equalsIgnoreCase(node.getUniqueIdentifier())){
					idsWithOperator.put(node.getUniqueIdentifier(), node);					
				}
			}		
		}
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// not needed
	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// not needed
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// not needed
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// not needed
	}

	@Override
	public Map<String, T> getResult() {
		return idsWithOperator;
	}

}
