package de.uniol.inf.is.odysseus.priority;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class PostPrioritizationPO<K extends IPriority & ITimeInterval, T extends IMetaAttributeContainer<? extends K>>
		extends AbstractPipe<T, T> {

	private IPostPrioritizationArea<T> prioritizedElements;

	public PostPrioritizationPO() {
	}

	@Override
	public PostPrioritizationPO<K, T> clone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T element, int port) {
		if (port == 0) {
			prioritizedElements.purgeElements(element);
			if (prioritizedElements.hasPartner(element)) {
				element.getMetadata().setPriority((byte) 1);
			}
			transfer(element);
		} else {
			prioritizedElements.insert(element);
		}
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if (port == 0) {
			sendPunctuation(timestamp);
		}
	}

	public void setPrioritizedElementsSweepArea(IPostPrioritizationArea<T> sa) {
		this.prioritizedElements = sa;
	}

}
