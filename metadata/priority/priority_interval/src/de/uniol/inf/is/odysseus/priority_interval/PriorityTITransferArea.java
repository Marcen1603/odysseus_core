package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

//interface should be K extends IPriority & ITimeInterval, but suns compiler (1.6) is buggy and doesn't accept it
/**
 * @author Jonas Jacobi
 */
public class PriorityTITransferArea<K extends ITimeInterval, R extends IMetaAttributeContainer<K>, W extends IMetaAttributeContainer<K>>
		extends TITransferArea<R,W> {

	@Override
	public void newElement(R object, int port) {
		//cast is necessary because of a compiler bug, see above
		if (((IPriority)object.getMetadata()).getPriority() == 0) {
			super.newElement(object, port);
		}
	}

	@Override
	public void transfer(W object) {
		//cast is necessary because of a compiler bug, see above
		if (((IPriority)object.getMetadata()).getPriority() > 0) {
			po.transfer(object);
		} else {
			super.transfer(object);
		}
	}

}
