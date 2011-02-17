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
package de.uniol.inf.is.odysseus.priority.punctuation;

import java.util.Collection;
import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipe;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
/**
 * This strategy makes usage of direct-link buffers with punctuation extension instead of
 * normal BufferedPipes.
 * @author jan
 *
 */
@Component(immediate = true)
@Service(value = IBufferPlacementStrategy.class)
public class PunctuationBufferPlacementStrategy  extends
		AbstractBufferPlacementStrategy {

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	// add buffer, if we are a binary operator or if the bottom
	// operator is a binary one
	@Override
	protected boolean bufferNeeded(
			Collection<? extends PhysicalSubscription<? extends ISource<?>>> subscriptions,
			ISink<?> childSink) {
		return subscriptions.size() > 1
				|| childSink.getSubscribedToSource().size() > 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IBuffer<?> createNewBuffer() {
		return  new DirectInterlinkBufferedPipe();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {
		// do nothing. It's only for punctuations
	}	
	
}
