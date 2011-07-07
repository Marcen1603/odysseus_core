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
package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.WindowCalculator;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper.IDataFactory;
import de.uniol.inf.is.odysseus.pnapproach.base.predicate.SlidingTimeWindowPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * Dieses Fenster kann sowohl fuer JumpingTimeWindow als auch fuer SlidingDeltaTimeWindow
 * benutzt werden. Lediglich SlidingTimeWindow laesst sich schneller berechnen, wenn man
 * es separat implementiert.
 * 
 * @author Andre Bolles
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class SlidingTimeWindowPNPO<M extends IPosNeg, T extends IMetaAttributeContainer<M>> extends
	AbstractNonBlockingWindowPNPO<M, T> {

	public SlidingTimeWindowPNPO(long windowSize, long windowAdvance, IDataFactory<M, M, T, T> dFac) {
		super(windowSize, windowAdvance, dFac);
		IPredicate<T> removePredicate = new SlidingTimeWindowPredicate(this.windowSize);
		this.init(removePredicate);
	}

	public SlidingTimeWindowPNPO(SlidingTimeWindowPNPO name) {
		super(name);
	}

	@Override
	protected PointInTime calcWindowEnd(PointInTime startTimestamp) {
		return WindowCalculator.calcSlidingWindowEnd(startTimestamp, this.windowSize);
	}

	@Override
	public SlidingTimeWindowPNPO<M, T> clone() {
		return new SlidingTimeWindowPNPO<M, T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

}
