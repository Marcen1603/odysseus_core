package de.uniol.inf.is.odysseus.util;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;

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

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	public CopyPhysicalPlanVisitor() {
		this.last = new Stack<IPhysicalOperator>();
		this.lastOld = new Stack<IPhysicalOperator>();
		this.errorsOccured = false;
		this.root = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ascendAction(IPhysicalOperator to) {
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
		getLogger().debug("subscribe " + sink.getName() + " to "
				+ source.getName());
		source.subscribeSink(sink, sub.getSinkInPort(), sub.getSourceOutPort(),
				sub.getSchema());
	}

	@Override
	public void descendAction(IPhysicalOperator to) {
	}

	@Override
	public IPhysicalOperator getResult() {
		return errorsOccured ? null : this.root;
	}

	@Override
	public void nodeAction(IPhysicalOperator op) {
		getLogger().debug("copy " + op.getName());
		this.last.push(op.clone());
		if (this.root == null) {
			this.root = this.last.peek();
		}
		this.lastOld.push(op);
	}

}
