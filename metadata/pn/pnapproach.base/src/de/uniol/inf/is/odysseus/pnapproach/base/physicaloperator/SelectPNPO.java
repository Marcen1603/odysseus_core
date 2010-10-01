package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * Dieser Operator liefert negative Elemente weiter, ohne das Pr�dikat zu pr�fen. Das h�ngt damit
 * zusammen, dass bei negativen Elementen nur die ID geliefert wird und damit das Pr�dikat nicht
 * mehr ausgewertet werden kann. 
 * 
 * Dieser Operator kann f�r PN und PNID eingesetzt werden.
 * 
 * @author Andre Bolles
 *
 * @param <T>
 */
public class SelectPNPO<T extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPipe<T, T> {

	private IPredicate<? super T> predicate;

	public SelectPNPO(IPredicate<? super T> predicate)  {
		this.predicate = predicate.clone();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
		if ((object.getMetadata().getElementType() == ElementType.POSITIVE && predicate.evaluate(object)) || 
				object.getMetadata().getElementType() == ElementType.NEGATIVE) {
			transfer(object);
		}
	}
	
	@Override
	public SelectPNPO<T> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}


}
