/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.util;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * @author Merlin Wasmann
 *
 */
public class CollectOperatorInputSchemaLogicalGraphVisitor<T extends ILogicalOperator> implements
		IGraphNodeVisitor<T, Set<Pair<T, SDFSchema>>> {

	private Set<Pair<T, SDFSchema>> result;
	
	public CollectOperatorInputSchemaLogicalGraphVisitor() {
		this.result = new HashSet<Pair<T, SDFSchema>>();
	}
	
	@Override
	public void nodeAction(T node) {
		if(node instanceof AbstractAccessAO) {
			return;
		}
		if(node.getNumberOfInputs() > 1) {
			return;
		}
		SDFSchema input = node.getInputSchema(0);
		this.result.add(new Pair<T, SDFSchema>(node, input));
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
	public Set<Pair<T, SDFSchema>> getResult() {
		return this.result;
	}

}
