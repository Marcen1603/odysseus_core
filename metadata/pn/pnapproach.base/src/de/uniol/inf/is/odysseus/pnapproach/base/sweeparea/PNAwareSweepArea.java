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
package de.uniol.inf.is.odysseus.pnapproach.base.sweeparea;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSweepArea;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;

/**
 * Diese SweepArea kann in einem Plan genutzt werden, der den PN Ansatz benutzt.
 * Zur normalen Version der {@link AbstractSweepArea} kann diese Klasse auskunft
 * darueber geben wieviele positive bzw. negative Elemente enthalten sind. Sie
 * verhaelt sich genau wie eine normale {@link AbstractSweepArea}, d.h. es werden
 * positive Elemente nicht geloescht, wenn ein zugehoeriges negatives Element
 * eingefuegt wird.
 * 
 * @author Bernd Hochschulz
 */
public class PNAwareSweepArea<T extends IMetaAttributeContainer<? extends IPosNeg>>
		extends AbstractSweepArea<T> implements IPNAwareSweepArea<T> {
	
	public PNAwareSweepArea(){};
	
	public PNAwareSweepArea(PNAwareSweepArea<T> pnAwareSweepArea){
		super(pnAwareSweepArea);
	}

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
	
	@Override
	public PNAwareSweepArea<T> clone(){
		return new PNAwareSweepArea<T>(this);
	}

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> it = this.elements.iterator();
		while(it.hasNext()){
			T next = it.next();
			if (next.getMetadata().getTimestamp().before(time)){
				result.add(next);
				it.remove();
			} else {
				break;
			}
		}
		return result.iterator();
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		Iterator<T> it = this.elements.iterator();
		while(it.hasNext()){
			T next = it.next();
			if (next.getMetadata().getTimestamp().before(time)){
				it.remove();
			} else {
				break;
			}
		}
	}
}
