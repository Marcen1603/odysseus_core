package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

/**
 * Dieser Operator liefert negative Elemente weiter, ohne das Prï¿½dikat zu prï¿½fen. Das hï¿½ngt damit
 * zusammen, dass bei negativen Elementen nur die ID geliefert wird und damit das Prï¿½dikat nicht
 * mehr ausgewertet werden kann. 
 * 
 * Dieser Operator kann für PN und PNID eingesetzt werden.
 * 
 * @author Andre Bolles
 *
 * @param <T>
 */
public class SelectPNPO<T extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPipe<T, T> {

	private IPredicate<? super T> predicate;

	public SelectPNPO(IPredicate<? super T> predicate) {
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
	public SelectPNPO<T> clone() {
		SelectPNPO<T> spo = (SelectPNPO<T>) super.clone();
		spo.predicate = this.predicate;
		return spo;
	}

}
