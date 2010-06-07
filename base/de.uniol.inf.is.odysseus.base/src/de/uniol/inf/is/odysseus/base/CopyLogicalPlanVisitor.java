package de.uniol.inf.is.odysseus.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Copies every logical operator in a query plan and connects them with
 * subscriptions like in the given plan. Result is the root of the copied plan.
 * 
 * @author Tobias Witt
 * 
 */
public class CopyLogicalPlanVisitor	implements
		INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, ILogicalOperator> {

	private Logger logger;
	private ILogicalOperator root;
	private Stack<ILogicalOperator> last;
	private Stack<ILogicalOperator> lastOld;
	private boolean errorsOccured;
	private Map<ILogicalOperator, ILogicalOperator> replaced = new HashMap<ILogicalOperator, ILogicalOperator>();

	public CopyLogicalPlanVisitor() {
		this.logger = LoggerFactory.getLogger(CopyLogicalPlanVisitor.class);
		this.root = null;
		this.last = new Stack<ILogicalOperator>();
		this.lastOld = new Stack<ILogicalOperator>();
		this.errorsOccured = false;
	}
	
	public Map<ILogicalOperator, ILogicalOperator> getReplaced() {
		return replaced;
	}

	@Override
	public void ascend(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		ILogicalOperator source = this.last.pop();
		ILogicalOperator sink = this.last.peek();
		ILogicalOperator oldSource = this.lastOld.pop();
		ILogicalOperator oldSink = (ILogicalOperator) to;
		if (source==null || sink==null) {
			return;
		}
		LogicalSubscription sub = null;
		for (LogicalSubscription s : oldSink.getSubscribedToSource()) {
			if (s.getTarget() == oldSource) {
				sub = s;
				break;
			}
		}
		this.logger.debug("subscribe " + sink.getName() + " to "
				+ source.getName());
		source.subscribeSink(sink, sub.getSinkInPort(), sub.getSourceOutPort(),
				sub.getSchema());
	}

	@Override
	public void descend(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
	}

	@Override
	public ILogicalOperator getResult() {
		return errorsOccured ? null : this.root;
	}

	@Override
	public void node(ISubscriber<ILogicalOperator, LogicalSubscription> node) {
		ILogicalOperator op = (ILogicalOperator) node;
		this.logger.debug("copy " + op.getName());
		ILogicalOperator op2 = null;
			op2 = op.clone();
			op2.clearPhysicalSubscriptions();
			if (this.root == null) {
				this.root = op2;
			}
			replaced.put(op, op2);
			this.last.push(op2);
		this.lastOld.push(op);
	}

}
