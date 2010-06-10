package de.uniol.inf.is.odysseus.priority.postpriorisation;

import java.util.Collection;
import java.util.Iterator;

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

public class PostPriorisationBufferPlacement extends
		AbstractBufferPlacementStrategy {


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
	
	@Override
	public String getName() {
		return "A2 Post priorisation buffer placement";
	}

}
