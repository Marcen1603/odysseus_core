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
package de.uniol.inf.is.odysseus.planmanagement.bufferplacement.querybufferplacement;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.BufferedPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;

public class QueryBufferPlacement implements IBufferPlacementStrategy{


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
	
	@SuppressWarnings("unchecked")
	protected void placeBuffer(IBuffer buffer, ISource<?> source) {
		
		getLogger().debug("Place Buffer "+buffer+" sink "+source );
		Collection<? extends PhysicalSubscription<? extends ISink<?>>> subscriptions = source.getSubscriptions();
		for (PhysicalSubscription<? extends ISink<?>> sub: subscriptions){
		// Place only if not already buffer there
		if (!(sub.getTarget() instanceof IBuffer)){
			sub.getTarget().unsubscribeFromSource((ISource) source, sub.getSinkInPort(),
					sub.getSourceOutPort(), sub.getSchema());
			buffer.subscribeToSource((ISource)source, sub.getSinkInPort(), 0, sub.getSchema());
			sub.getTarget().subscribeToSource(buffer, sub.getSinkInPort(), sub.getSourceOutPort(),
					sub.getSchema());
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addBuffers(IPhysicalOperator plan) {
		// Place a Buffer between MetaUpdate and every other Subscription
		if (plan instanceof MetadataUpdatePO){
			placeBuffer(new BufferedPipe(), (ISource)plan);
		}else{
			if (plan.isSink()){
				ISink sink = (ISink)plan;
				Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscription = sink
						.getSubscribedToSource();

				for (PhysicalSubscription<? extends ISource<?>> sub:subscription){
					addBuffers(sub.getTarget());
				}
			}
		}
		
	}

}
