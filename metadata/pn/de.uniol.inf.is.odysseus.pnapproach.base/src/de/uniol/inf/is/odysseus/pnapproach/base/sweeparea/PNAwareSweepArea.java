package de.uniol.inf.is.odysseus.pnapproach.base.sweeparea;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.SweepArea;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

/**
 * Diese SweepArea kann in einem Plan genutzt werden, der den PN Ansatz benutzt.
 * Zur normalen Version der {@link SweepArea} kann diese Klasse auskunft
 * darueber geben wieviele positive bzw. negative Elemente enthalten sind. Sie
 * verhaelt sich genau wie eine normale {@link SweepArea}, d.h. es werden
 * positive Elemente nicht geloescht, wenn ein zugehoeriges negatives Element
 * eingefuegt wird.
 * 
 * @author Bernd Hochschulz
 */
public class PNAwareSweepArea<T extends IMetaAttributeContainer<? extends IPosNeg>>
		extends SweepArea<T> implements IPNAwareSweepArea<T> {
	@Override
	public int getNegativeElementCount() {
		int count = 0;
		for (IMetaAttributeContainer<? extends IPosNeg> element : elements) {
			if (element.getMetadata().getElementType() == ElementType.NEGATIVE) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int getPositiveElementCount() {
		int count = 0;
		for (IMetaAttributeContainer<? extends IPosNeg> element : elements) {
			if (element.getMetadata().getElementType() == ElementType.POSITIVE) {
				count++;
			}
		}
		return count;
	}
}
