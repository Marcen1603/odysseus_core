package de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;

/**
 * 
 * @author Jonas Jacobi, Marco Grawunder
 * 
 */
public abstract class AbstractBufferPlacementStrategy implements
		IBufferPlacementStrategy {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AbstractBufferPlacementStrategy.class);
		}
		return _logger;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addBuffers(IPhysicalOperator plan) {
		if (plan instanceof IBuffer) {
			return;
		}
		if (plan.isSink() && !plan.isSource()) {
			for (PhysicalSubscription<? extends ISource<?>> s : ((ISink<?>) plan)
					.getSubscribedToSource()) {
				addBuffers(s.getTarget());
			}
		}
		if (plan.isSource()) {
			addBuffers((ISource) plan);
		}
	}

	@SuppressWarnings("unchecked")
	protected void placeBuffer(IBuffer buffer, ISink<?> sink,
			PhysicalSubscription<? extends ISource<?>> s) {
		getLogger().debug("Place Buffer "+buffer+" sink "+sink );
		s.getTarget().unsubscribeSink((ISink) sink, s.getSinkInPort(),
				s.getSourceOutPort(), s.getSchema());
		buffer.subscribeSink(sink, s.getSinkInPort(), 0, s.getSchema());
		s.getTarget().subscribeSink(buffer, 0, s.getSourceOutPort(),
				s.getSchema());
		initBuffer(buffer);
	}

	@SuppressWarnings("unchecked")
	protected abstract void initBuffer(IBuffer buffer);

	@SuppressWarnings("unchecked")
	public void addBuffers(ISource<?> myplan) {
		if (myplan instanceof IBuffer){
			return;
		}
		Stack<ISink<?>> sinks = new Stack<ISink<?>>();
		if (myplan.isSink()) {
			sinks.add((ISink<?>) myplan);
		}

		while (!sinks.isEmpty()) {
			ISink<?> sink = sinks.pop();
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptionsOriginal = sink
					.getSubscribedToSource();
			Collection<PhysicalSubscription<? extends ISource<?>>> subscriptions = new ArrayList<PhysicalSubscription<? extends ISource<?>>>();

			for (PhysicalSubscription<? extends ISource<?>> s : subscriptionsOriginal) {
				subscriptions.add(s);
			}

			for (PhysicalSubscription<? extends ISource<?>> s : subscriptions) {
				if (s.getTarget().isSink()) {
					if (s.getTarget() instanceof IBuffer) {
						// if there are already buffers in the subplan
						// we don't want to insert additional ones
						continue;
					}
					ISink<?> childSink = (ISink<?>) s.getTarget();
					sinks.push(childSink);
					if (bufferNeeded(subscriptions, childSink, sink)) {
						IBuffer buffer = createNewBuffer();
						placeBuffer(buffer, sink, s);
					}
				} else {
					IBuffer<?> buffer = createNewSourceBuffer();
					placeBuffer(buffer, sink, s);
				}
			}
		}
	}

	protected IBuffer<?> createNewSourceBuffer() {
		return createNewBuffer();
	}

	abstract protected IBuffer<?> createNewBuffer();

	abstract protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink, ISink<?> sink);

}
