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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class StreamTableTimeEditor extends StreamTableEditor {

	public StreamTableTimeEditor() {
		super(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateTuples(Tuple<?> element) {
		// element must provide time interval, cast and risk cast error
		// instead of testing each time;
		Tuple<? extends ITimeInterval> e = (Tuple<? extends ITimeInterval>) element;
			
		PointInTime start = e.getMetadata().getStart();
		for (Tuple<?> t:tuples){
			if (((Tuple<? extends ITimeInterval>)t).getMetadata().getEnd().beforeOrEquals(start)){
				tuples.remove(t);
			}
			
		}
		super.updateTuples(element);
	}

}
