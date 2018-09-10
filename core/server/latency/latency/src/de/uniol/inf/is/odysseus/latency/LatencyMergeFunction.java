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
package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public class LatencyMergeFunction implements IInlineMetadataMergeFunction<ILatency> {

	@Override
	public void mergeInto(ILatency result, ILatency inLeft, ILatency inRight) {
		//only start timestamp get merged, 'cause the end timestamp should not be set,
		//when two elements get merged
		result.setMinLatencyStart(Math.max(inLeft.getLatencyStart(), inRight.getLatencyStart()));
		result.setMaxLatencyStart(Math.min(inLeft.getMaxLatencyStart(), inRight.getMaxLatencyStart()));
	}

	@Override
	public LatencyMergeFunction clone() {
		return new LatencyMergeFunction();
	}
	
	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return ILatency.class;
	}

}
