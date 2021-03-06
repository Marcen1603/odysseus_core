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
package de.uniol.inf.is.odysseus.core.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class TimeIntervalInlineMetadataMergeFunction implements
		IInlineMetadataMergeFunction<ITimeInterval> {
	
	
	public TimeIntervalInlineMetadataMergeFunction(
			TimeIntervalInlineMetadataMergeFunction timeIntervalInlineMetadataMergeFunction) {

	}
	
	public TimeIntervalInlineMetadataMergeFunction(){}

	@Override
	public void mergeInto(ITimeInterval result, ITimeInterval inLeft,
			ITimeInterval inRight) {
		result.setStart(PointInTime.max(inLeft.getStart(), inRight.getStart()));
		result.setEnd(PointInTime.min(inLeft.getEnd(), inRight.getEnd()));
	}

	@Override
	public TimeIntervalInlineMetadataMergeFunction clone()  {
		return new TimeIntervalInlineMetadataMergeFunction(this);
	}
	
	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return ITimeInterval.class;
	}
}
