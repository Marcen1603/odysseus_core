/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class LeftJoinTITransferArea<R extends IStreamObject<? extends ITimeInterval>, W extends IStreamObject<? extends ITimeInterval>>
		extends TITransferArea<R, W> {

//	/**
//	 * removes all elements whose start timestamp is before heartbeat
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public void newHeartbeat(PointInTime heartbeat, int inPort) {
//		// FIXME: Why is this method overwritten??
//		PointInTime minimum = heartbeat;
//		if (minimum != null) {
//			synchronized (this.outputQueue) {
//				boolean wasElementSent = false;
//				// don't use an iterator, it does NOT guarantee ordered
//				// traversal!
//				IStreamable elem = this.outputQueue.peek();
//				while (elem != null) {
//					if (elem.isPunctuation()) {
//						if (((IPunctuation)elem).getTime().beforeOrEquals(minimum)){
//							this.outputQueue.poll();
//							po.sendPunctuation((IPunctuation) elem);
//							elem = this.outputQueue.peek();
//						}else{
//							elem = null;
//						}
//					} else {
//						if (((W) elem).getMetadata() != null
//								&& ((W) elem).getMetadata().getStart()
//										.beforeOrEquals(minimum)) {
//							this.outputQueue.poll();
//							wasElementSent = true;
//							po.transfer((W) elem);
//							elem = this.outputQueue.peek();
//						} else {
//							elem = null;
//						}
//					}
//				}
//				if (wasElementSent) {
//					po.sendPunctuation(new Heartbeat(minimum));
//				}
//			}
//		}
//	}
}
