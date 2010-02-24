package de.uniol.inf.is.odysseus.base;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
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

	public CopyLogicalPlanVisitor() {
		this.logger = LoggerFactory.getLogger(CopyLogicalPlanVisitor.class);
		this.root = null;
		this.last = new Stack<ILogicalOperator>();
		this.lastOld = new Stack<ILogicalOperator>();
	}

	@Override
	public void ascend(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		ILogicalOperator source = this.last.pop();
		ILogicalOperator sink = this.last.peek();
		ILogicalOperator oldSource = this.lastOld.pop();
		ILogicalOperator oldSink = (ILogicalOperator) to;
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
		return this.root;
	}

	@Override
	public void node(ISubscriber<ILogicalOperator, LogicalSubscription> node) {
		ILogicalOperator op = (ILogicalOperator) node;
		this.logger.debug("copy " + op.getName());
		ILogicalOperator op2 = null;
		try {
			op2 = op.clone();
			op2.clearPhysicalSubscriptions();
			if (this.root == null) {
				this.root = op2;
			}
//			for (int i=op2.getSubscribedToSource().size()-1; i>=0; i--) {
//				op2.unsubscribeFromSource(op2.getSubscribedToSource(i));
//			}
			this.last.push(op2);
		this.lastOld.push(op);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
