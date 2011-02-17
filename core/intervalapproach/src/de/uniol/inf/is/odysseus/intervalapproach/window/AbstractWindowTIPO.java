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
package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.IWindow;


public abstract class AbstractWindowTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractPipe<T, T> implements IWindow {
	protected final long windowSize;
	protected final long windowAdvance;
	protected final WindowType windowType;
	protected final boolean partitioned;
	
	public AbstractWindowTIPO(WindowAO ao) {
		this.windowSize = ao.getWindowSize();
		this.windowAdvance = ao.getWindowAdvance();
		//this.windowAO = ao;
		this.windowType = ao.getWindowType();
		this.partitioned = ao.isPartitioned();
		setName(getName()+" s="+windowSize+" a="+windowAdvance);
	}

	public AbstractWindowTIPO(AbstractWindowTIPO<T> window) {
		this.windowSize = window.windowSize;
		this.windowAdvance = window.windowAdvance;
		//this.windowAO = window.windowAO.clone();
		this.windowType = window.windowType;
		this.partitioned = window.partitioned;
	}

	@Override
	public long getWindowSize() {
		return windowSize;
	}

	public long getWindowAdvance() {
		return windowAdvance;
	}
	
//	public WindowAO getWindowAO() {
//		return windowAO;
//	}
	
	@Override
	public WindowContentType getWindowContentType() {
		switch (windowType) {
		case PERIODIC_TIME_WINDOW:
		case SLIDING_TIME_WINDOW:
		case JUMPING_TIME_WINDOW:
		case FIXED_TIME_WINDOW:
			return WindowContentType.TIME_BASED;
		case PERIODIC_TUPLE_WINDOW:
		case SLIDING_TUPLE_WINDOW:
		case JUMPING_TUPLE_WINDOW:
			return WindowContentType.ELEMENT_BASED;
		default:
			return WindowContentType.OTHER;
		}
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof AbstractWindowTIPO) || !this.getClass().toString().equals(ipo.getClass().toString())) {
			return false;
		}
		
		//System.out.println("SAME WINDOWS - Step 1");
		//System.out.println(this.getClass().toString());
		//System.out.println(ipo.getClass().toString());
		AbstractWindowTIPO<T> awtipo = (AbstractWindowTIPO<T>) ipo;
		
		// Falls die Operatoren verschiedene Quellen haben, wird false zur�ck gegeben
		if(!this.hasSameSources(awtipo)) {
			return false;
		}
		//System.out.println("SAME WINDOWS - Step 2");
		if(this.windowSize == awtipo.windowSize
				&& this.windowAdvance == awtipo.windowAdvance
				&& this.windowType.compareTo(awtipo.windowType) == 0) {
			//System.out.println("SAME WINDOWS - Step 3");
			return true;
		}
		return false;
	}
	
	public boolean isPartitioned() {
		return partitioned;
	}
}
