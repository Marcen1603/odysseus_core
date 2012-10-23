/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class TimeSplitTIPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {

	private long size;

	public TimeSplitTIPO(long size) {
		this.size = size;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		long start = object.getMetadata().getStart().getMainPoint();
		long end = object.getMetadata().getStart().getMainPoint();
		long i = start;
		for (; i + size < end; i += size) {
			T newObject = (T) object.clone();
			newObject.getMetadata().setStart(new PointInTime(i));
			newObject.getMetadata().setEnd(new PointInTime(i + size));
			transfer(newObject);
		}
		T newObject = (T) object.clone();
		newObject.getMetadata().setStart(new PointInTime(i));
		transfer(newObject);
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new TimeSplitTIPO<T>(size);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof TimeSplitTIPO)) {
			return false;
		}
		TimeSplitTIPO<T> tstipo = (TimeSplitTIPO) ipo;
		if(this.hasSameSources(tstipo) &&
				this.size == tstipo.size) {
			return true;
		}
		return false;
	}
}
