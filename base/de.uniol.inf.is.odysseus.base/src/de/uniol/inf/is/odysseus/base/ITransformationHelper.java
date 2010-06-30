package de.uniol.inf.is.odysseus.base;

import java.util.Collection;

import de.uniol.inf.is.odysseus.physicaloperator.base.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
@SuppressWarnings("unchecked")
public interface ITransformationHelper {
	
	public Collection<ILogicalOperator> replace(ILogicalOperator logical, IPipe physical);

	public Collection<ILogicalOperator> replace(ILogicalOperator logical, ISink physical);
	public Collection<ILogicalOperator> replace(ILogicalOperator logical, ISource physical);	
	/**
	 * Inserts a new operator between a physical and a logical operator.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public Collection<ILogicalOperator> insertNewFather(ISource oldFather, Collection<ILogicalOperator> children, IPipe newFather);
	
	/**
	 * Inserts a new operator into a completely transformed physical query plan.
	 * 
	 * @param oldFather The old lower operator (from the dataflow point of view)
	 * @param children The following operators of oldFather (from the dataflow point of view)
	 * @param newFather The new lower operator. oldFather becomes the father of newFather
	 * @return the modified children must be returned to update the drools working memory
	 */
	public Collection<ISink> insertNewFatherPhysical(ISource oldFather, Collection<ISubscription<ISink>> children, IPipe newFather);

	public boolean containsWindow(ILogicalOperator inputOp);

}
