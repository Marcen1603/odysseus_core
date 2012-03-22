/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.bufferplacement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

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
	public void addBuffers(IPhysicalQuery plan){
		for (IPhysicalOperator root: plan.getRoots()){
			addBuffers(root);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void addBuffers(IPhysicalOperator plan) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({"rawtypes" })
	protected abstract void initBuffer(IBuffer buffer);

	
	@SuppressWarnings("rawtypes")
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
