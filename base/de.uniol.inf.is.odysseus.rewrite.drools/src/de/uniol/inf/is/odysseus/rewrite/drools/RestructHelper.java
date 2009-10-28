package de.uniol.inf.is.odysseus.rewrite.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;

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
			child.getTarget().subscribe(father.getTarget(), father.getSinkPort(), child.getSourcePort(), reserveOutputSchema?remove.getOutputSchema():child.getTarget().getOutputSchema());
			ret.add(child.getTarget());
		}
		ret.add(child.getTarget());
		
		
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
	public static Collection<ILogicalOperator> insertOperator(ILogicalOperator toInsert, ILogicalOperator after, int sinkPort){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> sinks = after.getSubscriptions();
		LogicalSubscription source = after.getSubscribedTo(sinkPort);
		for (LogicalSubscription s: sinks){
			ret.add(s.getTarget());
			after.unsubscribeSubscriptionTo(s.getTarget(), s.getSinkPort(), s.getSourcePort());
			toInsert.subscribeTo(s.getTarget(), s.getSinkPort(), s.getSourcePort(),s.getInputSchema());
		}
		after.subscribeTo(toInsert, sinkPort, source.getSourcePort(), toInsert.getOutputSchema());
		return ret;
	}
	
	/**
	 * Switches two unary operators in a Plan (i.e. no new Operator need to be defined)
	 * Cannot be used if one of the operators is binary!
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static Collection<ILogicalOperator> switchOperator(UnaryLogicalOp a, UnaryLogicalOp b){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> subs = b.getSubscribtions(a);
		ret.addAll(removeOperator(a, true));
		for (LogicalSubscription l:subs){
			ret.addAll(insertOperator(a, b, l.getSinkPort()));
		}
		return ret;
	}
	
//	public static Collection<ILogicalOperator> switchOperator(ProjectAO a, SelectAO b){
//		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
//		// Set Fathers of Projection to Fathers of Selection 
//		Collection<LogicalSubscription> projectSinks = a.getSubscriptions();
//		for (LogicalSubscription s: projectSinks){
//			s.getTarget().unsubscribeTo(s);
//			s.getTarget().subscribeTo(b, s.getSinkPort(), s.getSourcePort(), s.getInputSchema());			
//			ret.add(s.getTarget());
//		}
//		// Set Children of Selection to Children of Projection
//		Collection<LogicalSubscription> selectSources = b.getSubscribedTo();
//		for (LogicalSubscription s: selectSources){
//			s.getTarget().unsubscribe(s);
//			s.getTarget().subscribe(a, s.getSinkPort(), s.getSourcePort(), s.getInputSchema());
//			ret.add(s.getTarget());
//		}
//		// Set 
//		Collection<LogicalSubscription> l = a.getSubscribedTo(); 
//		for (LogicalSubscription s: l){
//			
//		}
//		
//		b.subscribeTo(a, 0, 0, )
//		
//		
//		return ret;
//	}
	
	public static Collection<ILogicalOperator> removeOperator(RenameAO op){
		return removeOperator(op, true);
	}

	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp op){
		return removeOperator(op, false);
	}
}
