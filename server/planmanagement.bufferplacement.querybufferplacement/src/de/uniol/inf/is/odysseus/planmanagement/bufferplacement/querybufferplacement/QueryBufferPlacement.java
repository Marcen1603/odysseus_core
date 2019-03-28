/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.planmanagement.bufferplacement.querybufferplacement;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryBufferPlacement implements IBufferPlacementStrategy {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(QueryBufferPlacement.class);
		}
		return _logger;
	}

	@Override
	public String getName() {
		return "Query Buffer Placement";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void placeBuffer(IBuffer buffer, ISource<?> source) {
		getLogger().debug("Place new buffer " + buffer + " source " + source);
		Collection<? extends AbstractPhysicalSubscription<?, ? extends ISink<?>>> subscriptions = source
				.getSubscriptions();
		for (AbstractPhysicalSubscription<?, ? extends ISink<?>> sub : subscriptions) {
			// Place only if not already buffer there
			if (!(sub.getSink() instanceof IBuffer)) {

				sub.getSink().unsubscribeFromSource((ISource) source, sub.getSinkInPort(), sub.getSourceOutPort(),
						sub.getSchema());
				buffer.subscribeToSource(source, 0, sub.getSourceOutPort(), sub.getSchema());
				sub.getSink().subscribeToSource(buffer, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addBuffers(IPhysicalOperator plan) {
		// Place a Buffer between MetaUpdate and every other Subscription
		if (plan instanceof ReceiverPO) {
			IBuffer buffer = new BufferPO<>();
			buffer.addOwner(plan.getOwner());
			buffer.setOutputSchema(plan.getOutputSchema());
			placeBuffer(buffer, (ISource) plan);
		} else {
			if (plan.isSink()) {
				ISink sink = (ISink) plan;
				Collection<? extends AbstractPhysicalSubscription<? extends ISource<?>, ?>> subscription = sink
						.getSubscribedToSource();

				for (AbstractPhysicalSubscription<? extends ISource<?>, ?> sub : subscription) {
					addBuffers(sub.getSource());
				}
			}
		}

	}

	@Override
	public void addBuffers(IPhysicalQuery plan) {
		for (IPhysicalOperator root : plan.getRoots()) {
			addBuffers(root);
		}
	}

}
