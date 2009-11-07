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
				|| childSink.getSubscribedTo().size() > 1;
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

			Iterator<PhysicalSubscription> it = buffer.getSubscribedTo()
					.iterator();

			while (it.hasNext()) {
				PhysicalSubscription sub = it.next();
				updateBufferData(buffer, (IPhysicalOperator) sub.getTarget());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void updateBufferData(IBuffer buffer, IPhysicalOperator father) {
		if (father.isSink()) {
			Iterator<?> it = ((ISink) father).getSubscribedTo().iterator();

			while (it.hasNext()) {
				PhysicalSubscription<?> sub = (PhysicalSubscription<?>) it
						.next();
				if (((ISource<?>)sub.getTarget()).isSink()) {
					
					if((ISource<?>)sub.getTarget() instanceof PostPriorisationPO<?>) {
						
						PostPriorisationPO postPO = (PostPriorisationPO)sub.getTarget();

						DirectInterlinkBufferedPipePostPriorisation postBuf = (DirectInterlinkBufferedPipePostPriorisation) buffer;
						IPostPriorisationFunctionality functionality = postPO.getPostPriorisationFunctionality();
						postBuf.setPostPriorisationFunctionality(functionality.newInstance(postBuf));
						postBuf.setJoinFragment(postPO.getJoinFragment());	
						
						PriorityPO prioPO = postPO.getPhysicalPostPriorisationRoot();
						postBuf.setDefaultPriority(prioPO.getDefaultPriority());
						
						Iterator itCopartners = prioPO.getCopartners().iterator();
						
						while(itCopartners.hasNext()) {
							IPostPriorisationPipe copartner = (IPostPriorisationPipe) itCopartners.next();
							if(copartner.equals(postPO)) {
								postPO.setActive(false);
								itCopartners.remove();
							}
						}
						
						prioPO.getCopartners().add(postBuf);
						
						
					} else {
						updateBufferData(buffer, (ISink) sub.getTarget());
					}
				}
			}
		}
	}

}
