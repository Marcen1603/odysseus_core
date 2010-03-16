package de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Stack;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.physicaloperator.base.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

/**
 * TODO: sollte von Jonas oder Marco dokumentiert werden.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractBufferPlacementStrategy implements IBufferPlacementStrategy {

	/**
	 * Name of this strategy
	 */
	private String name = null;

	protected void activate(ComponentContext context) {
		setName(context.getProperties());
	}

	@SuppressWarnings("unchecked")
	protected void setName(Dictionary properties) {
		name = ((String) properties.get("component.readableName"));
		if (name == null) {
			name = ((String) properties.get("component.name"));
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public void addBuffers(IPhysicalOperator plan) {
		if (plan.isSink() && !plan.isSource()) {
			for (PhysicalSubscription<? extends ISource<?>> s : ((ISink<?>) plan).getSubscribedToSource()) {
				addBuffers(s.getTarget());
			}
		}
		if (plan.isSource()) {
			addBuffers((ISource) plan);
		}
	}

	@SuppressWarnings("unchecked")
	protected void placeBuffer(IBuffer buffer, ISink<?> sink, PhysicalSubscription<? extends ISource<?>> s) {
		// TODO Warnings
		s.getTarget().unsubscribeSink((ISink) sink, s.getSinkInPort(), s.getSourceOutPort(), s.getSchema());
		buffer.subscribeSink(sink, s.getSinkInPort(), 0, s.getSchema());
		s.getTarget().subscribeSink(buffer, 0, s.getSourceOutPort(), s.getSchema());
		initBuffer(buffer);
	}

	@SuppressWarnings("unchecked")
	protected abstract void initBuffer(IBuffer buffer);

	@SuppressWarnings("unchecked")
	public void addBuffers(ISource<?> myplan) {
		// TODO Warnings
		Stack<ISink<?>> sinks = new Stack<ISink<?>>();
		if (myplan.isSink()) {
			sinks.add((ISink<?>) myplan);
		}

		while (!sinks.isEmpty()) {
			ISink<?> sink = sinks.pop();
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions = sink.getSubscribedToSource();
			for (PhysicalSubscription<? extends ISource<?>> s : subscriptions) {
				if (s.getTarget().isSink()) {
					if (s.getTarget() instanceof IBuffer) {
						// if there are already buffers in the subplan
						// we don't want to insert additional ones
						continue;
					}
					ISink<?> childSink = (ISink<?>) s.getTarget();
					sinks.push(childSink);
					if (bufferNeeded(subscriptions, childSink)) {
						IBuffer buffer = createNewBuffer();
						placeBuffer(buffer, sink, s);
					}
				} else {
					// on bottom sources we don't have any priority information
					// so we have no need for a special buffer
					IBuffer buffer = new BufferedPipe();
					placeBuffer(buffer, sink, s);
				}
			}
		}
	}

	abstract protected IBuffer<?> createNewBuffer();

	abstract protected boolean bufferNeeded(Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions, ISink<?> childSink);

}
