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
package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalTimestampAttributeTimeIntervalMFactory
		extends AbstractMetadataUpdater<ITimeInterval, Tuple<? extends ITimeInterval>> {

	final private int startAttrPos;
	final private int endAttrPos;
	final private boolean clearEnd;
	

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos, int endAttrPos, boolean clearEnd) {
		this.startAttrPos = startAttrPos;
		this.endAttrPos = endAttrPos;
		this.clearEnd = clearEnd;
	}

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos, boolean clearEnd) {
		this(startAttrPos, -1, clearEnd);
	}
	
	@Override
	public void updateMetadata(Tuple<? extends ITimeInterval> inElem) {
		PointInTime start = extractTimestamp(inElem, startAttrPos);
		if (clearEnd){
			inElem.getMetadata().setEnd(PointInTime.getInfinityTime());
		}
		inElem.getMetadata().setStart(start);
		if (endAttrPos > 0){
			PointInTime end = extractTimestamp(inElem, endAttrPos);
			inElem.getMetadata().setEnd(end);
		}
		
	}

	private static PointInTime extractTimestamp(
			Tuple<? extends ITimeInterval> inElem, int attrPos) {
		Number timeN = (Number) inElem.getAttribute(attrPos);
		PointInTime time = null;
		if (timeN.longValue() == -1){
			time = PointInTime.getInfinityTime();
		}else{
			time = new PointInTime(timeN);
		}
		return time;
	}

}