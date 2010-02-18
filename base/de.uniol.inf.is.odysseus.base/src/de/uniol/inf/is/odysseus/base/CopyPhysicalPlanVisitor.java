package de.uniol.inf.is.odysseus.base;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.util.INodeVisitor;

/**
 * Copies every physical operator in a query plan and connects them with
 * subscriptions like in the given plan. Result is the root of the copied plan.
 * 
 * @author Tobias Witt
 * 
 */
public class CopyPhysicalPlanVisitor implements
		INodeVisitor<IPhysicalOperator, IPhysicalOperator> {

	private Stack<IPhysicalOperator> last;
	private Stack<IPhysicalOperator> lastOld;
	private boolean errorsOccured;
	private IPhysicalOperator root;
	
	private Logger logger;

	public CopyPhysicalPlanVisitor() {
		this.logger = LoggerFactory.getLogger(CopyPhysicalPlanVisitor.class);
		this.last = new Stack<IPhysicalOperator>();
		this.lastOld = new Stack<IPhysicalOperator>();
		this.errorsOccured = false;
		this.root = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ascend(IPhysicalOperator to) {
		ISource<?> source = (ISource<?>) this.last.pop();
		ISink sink = (ISink) this.last.peek();
		ISource<?> oldSource = (ISource<?>) this.lastOld.pop();
		ISink<?> oldSink = (ISink<?>) to;
		if (source == null || sink == null) {
			return;
		}
		PhysicalSubscription<?> sub = null;
		for (PhysicalSubscription<?> s : oldSink.getSubscribedToSource()) {
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
	public void descend(IPhysicalOperator to) {
	}

	@Override
	public IPhysicalOperator getResult() {
		return errorsOccured ? null : this.root;
	}

	@Override
	public void node(IPhysicalOperator op) {
		this.logger.debug("copy " + op.getName());
		try {
			this.last.push(op.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			this.last.push(null);
			errorsOccured = true;
		}
		if (this.root == null) {
			this.root = this.last.peek();
		}
		this.lastOld.push(op);
	}

}
