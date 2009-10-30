package de.uniol.inf.is.odysseus.rewrite.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;

public class RestructHelper {
	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp remove, boolean reserveOutputSchema){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> fathers = remove.getSubscriptions();
		LogicalSubscription child = remove.getSubscribedTo(0);
		// remove Connection between child and op
		remove.unsubscribeTo(child);
		// Subscribe Child to every father of op
		for (LogicalSubscription father:fathers){
			remove.unsubscribe(father); 
			child.getTarget().subscribe(father.getTarget(), father.getSinkPort(), child.getSourcePort(), 
					reserveOutputSchema?remove.getOutputSchema():child.getTarget().getOutputSchema());
			ret.add(child.getTarget());
			ret.add(father.getTarget());
		}	
		
		return ret;
	}

	/**	Insert an operator in the tree at some special point and update all subscriptions
	 * i.e. the new Operator gets all subscriptions currently bound to the after operator
	 * and create a new subscription from toInsert to after
	 * 
	 * @param toInsert Operator that should be inserted as child of the after operator 
	 * @param after
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperator(ILogicalOperator toInsert, ILogicalOperator after, int sinkPort, int toInsertSinkPort, int toInsertSourcePort){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		LogicalSubscription source = after.getSubscribedTo(sinkPort);
		ret.add(source.getTarget());
		after.unsubscribeTo(source);
		source.getTarget().subscribe(toInsert, toInsertSinkPort, source.getSourcePort(), source.getTarget().getOutputSchema());
		toInsert.subscribe(after, sinkPort, toInsertSourcePort, toInsert.getOutputSchema());		
		ret.add(after);
		return ret;
	}
	
	
	public static Collection<ILogicalOperator> switchOperatorInternal(UnaryLogicalOp father, UnaryLogicalOp son){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		
		// unsubscribe a from b
		LogicalSubscription fatherToSon = father.getSubscribedTo(0);
		father.unsubscribe(fatherToSon);
		
		// Set Fathers of Projection to Fathers of Selection 
		Collection<LogicalSubscription> fatherSinks = father.getSubscriptions();
		for (LogicalSubscription s: fatherSinks){
			s.getTarget().unsubscribeTo(s);
			s.getTarget().subscribeTo(son, s.getSinkPort(), s.getSourcePort(), s.getInputSchema());			
			ret.add(s.getTarget());
		}
		// Set Children of Selection to Children of Projection
		Collection<LogicalSubscription> sonSources = son.getSubscribedTo();
		for (LogicalSubscription s: sonSources){
			s.getTarget().unsubscribe(s);
			s.getTarget().subscribe(father, s.getSinkPort(), s.getSourcePort(), s.getInputSchema());
			ret.add(s.getTarget());
		}

		// subscribe Select to Project
		son.subscribeTo(father, father.getOutputSchema());		
		
		return ret;
		
	}
}
