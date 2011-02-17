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
package de.uniol.inf.is.odysseus.priority.postpriorisation;

import java.util.Collection;
import java.util.Iterator;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.optimization.bufferplacement.AbstractBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationFunctionality;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationPipe;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;
import de.uniol.inf.is.odysseus.priority.PriorityPO;
import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipePostPriorisation;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

@Component(immediate = true)
@Service(value = IBufferPlacementStrategy.class)
public class PostPriorisationBufferPlacement extends
		AbstractBufferPlacementStrategy {

	@Override
	protected void activate(ComponentContext context) {
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
		return new DirectInterlinkBufferedPipePostPriorisation();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initBuffer(IBuffer buffer) {

		if (buffer instanceof DirectInterlinkBufferedPipePostPriorisation) {

			Iterator<PhysicalSubscription> it = buffer.getSubscribedToSource()
					.iterator();

			while (it.hasNext()) {
				PhysicalSubscription sub = it.next();
				updateBufferData(buffer, (IPhysicalOperator) sub.getTarget());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updateBufferData(IBuffer buffer, IPhysicalOperator father) {
		
		if(father.isSource() && father instanceof PostPriorisationPO) {
			PostPriorisationPO postPO = (PostPriorisationPO) father;

			DirectInterlinkBufferedPipePostPriorisation postBuf = (DirectInterlinkBufferedPipePostPriorisation) buffer;
			IPostPriorisationFunctionality functionality = postPO.getPostPriorisationFunctionality();
			postBuf.setPostPriorisationFunctionality(functionality.newInstance(postBuf));
			postBuf.setJoinFragment(postPO.getJoinFragment());	
			postBuf.setActive(true);
			PriorityPO prioPO = postPO.getPhysicalPostPriorisationRoot();
			Iterator itCopartners = prioPO.getCopartners().iterator();
			
			while(itCopartners.hasNext()) {
				IPostPriorisationPipe copartner = (IPostPriorisationPipe) itCopartners.next();
				if(copartner instanceof PostPriorisationPO) {
					postPO.setActive(false);
					itCopartners.remove();
				}
			}
			
			prioPO.getCopartners().add(postBuf);
		}
		
	}

}
